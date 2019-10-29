/**
 * 1. 暂时将60岁划为50s
 * 2. 如果 vote, vote_ip 表还没有建立，如何处理
 * 3. ajax 同步执行才能实现结果
 * 4. ajax 中的url 如何使用相对路径
 * 5. $('body').everyTime: TypeError: $('body').everyTime is not a function. 
 */

// 全局配置
// var teachers = ['小明', '小红', '小华', '小蓝', '小波', '小李', '小杰', '小莉'];
// var vote_numbers = [20, 35, 10, 30, 25, 10, 25, 40];
// var sex = ['男', '女', '男', '女', '男', '男', '男', '女'];
// var age = [32, 32, 31, 29, 33, 35, 32, 28];
// var sex_numbers = [{name: 'male', value: 90},{name:'female', value: 105}];
// var age_numbers = [{name: '20s', value: 70}, {name: '30s', value: 125}, {name: '40s', value: 0}, {name: '50s', value: 0}];
var id;
var teachers;
var vote_numbers;
var sex;
var age;
var sex_numbers;
var age_numbers;


// 文档加载完毕
$(document).ready(function(){
    /* ajax 获取session中值 */
    function getSession(){
        console.log(window.location.host);
        $.ajax({
            url: 'https://localhost:8443/vote/getSession.do',
            type: 'GET',
            dataType: 'json',
            async: false,   // 同步执行
            success: function(data){
                id = JSON.parse(data.id);
                teachers = JSON.parse(data.teachers);
                vote_numbers = JSON.parse(data.vote_numbers);
                sex = JSON.parse(data.sex);
                age = JSON.parse(data.age);
                // 数组不解析的话识别不了
                sex_numbers = JSON.parse(data.sex_numbers);
                age_numbers = JSON.parse(data.age_numbers);
                console.log(id);
                console.log(teachers);
                console.log(vote_numbers);
                console.log(sex);
                console.log(age);
                console.log(sex_numbers);
                console.log(age_numbers);
            },
            error: function(err){
                window.location.href = './result_error.html';
                alert('出错，请查看日志');
                console.log(err.responseText);
            }
        });
    }

    function init_view(){
        /* 添加投票总结果柱转图 */
        echarts.init(document.getElementById('bar')).setOption({
            title: {
                text: '投票结果'
            },
            tooltip: {},
            legend: {
                data: ['vote']
            },
            xAxis: {
                data: teachers
            },
            yAxis: {},
            series: [{
                name: 'vote',
                type: 'bar',
                data: vote_numbers
            }]
        });


        /* 添加性别饼状图 */
        var sex_pie = echarts.init(document.getElementById('sex_pie'));
        sex_pie.setOption({
            tooltip: {      // 鼠标移动到扇形上的显示
                trigger: 'item',
                formatter: '{a} <br />{b}: {c}票 ({d}%)'
            },
            series: {
                name: '性别对比',
                type: 'pie',
                roseType: 'angle',      // 南丁格尔图
                radius: '55%',
                data: sex_numbers,
                itemStyle: {            // 百分比显示
                    normal: {
                        label:{
                            show: true,
                            formatter: '{b}: {c}票 ({d}%)'
                        },
                        labelLine: {
                            show: true
                        }
                    }
                }
            },
        });
        sex_pie.on('click', function(params){
            // alert(params.name);
            $("#sex_pie").css("display", "none");
            if(params.name === 'male'){
                $("#male_bar").css("display", "block");
            }else if(params.name === 'female'){
                $("#female_bar").css("display", "block");
            }
        });

        /* 获取性别有关信息 */
        var male_teachers = [];
        var male_vote_numbers = [];
        var female_teachers = [];
        var female_vote_numbers = [];
        $.each(sex, function(index, value){
            if(value === '男'){
                male_teachers.push(teachers[index]);
                male_vote_numbers.push(vote_numbers[index]);
            }else if(value === '女'){
                female_teachers.push(teachers[index]);
                female_vote_numbers.push(vote_numbers[index]);
            }
        });

        /* 添加男性结果条状图 */
        var male_bar = echarts.init(document.getElementById('male_bar'));
        male_bar.setOption({
            title: {
                text: 'male'
            },
            tooltip: {},
            legend: {
                data: ['male']
            },
            xAxis: {
                data: male_teachers
            },
            yAxis: {},
            series: [{
                name: 'male', 
                type: 'bar',
                data: male_vote_numbers
            }]
        });
        male_bar.on('legendselectchanged', function(params){      // legend 改变事件
            $("#male_bar").css("display", "none");
            $("#sex_pie").css("display", "block");
            // alert(params.name);
            var option = this.getOption();
            option.legend[0].selected[params.name] = true;
            this.setOption(option);
        });

        /* 添加女性结果条状图 */
        var female_bar = echarts.init(document.getElementById('female_bar'));
        female_bar.setOption({
            title: {
                text: 'female'
            },
            tooltip: {},
            legend: {
                data: ['female']
            },
            xAxis: {
                data: female_teachers
            },
            yAxis: {},
            series: [{
                name: 'female', 
                type: 'bar',
                data: female_vote_numbers
            }]
        });
        female_bar.on('legendselectchanged', function(params){
            $("#female_bar").css("display", "none");
            $("#sex_pie").css("display", "block");
            // alert(Object.keys(params.selected).length)
            // alert(params.name);
            var option = this.getOption();
            option.legend[0].selected[params.name] = true;
            this.setOption(option);
        });


        /* 添加年龄饼状图 */
        var age_pie = echarts.init(document.getElementById('age_pie'));
        age_pie.setOption({
            tooltip: {      // 鼠标移动到扇形上的显示
                trigger: 'item',
                formatter: '{a} <br />{b}: {c}票 ({d}%)'
            },
            series: {
                name: '年龄对比',
                type: 'pie',
                roseType: 'angle',      // 男丁格尔图
                radius: '55%',
                data: age_numbers,
                itemStyle: {            // 百分比显示
                    normal: {
                        label:{
                            show: true,
                            formatter: '{b}: {c}票 ({d}%)'
                        },
                        labelLine: {
                            show: true
                        }
                    }
                }
            }
        });
        age_pie.on('click', function(params){
            // alert(params.name);
            $("#age_pie").css("display", "none");
            if(params.name === '20s'){
                $("#age_20s_bar").css("display", "block");
            }else if(params.name === '30s'){
                $("#age_30s_bar").css("display", "block");
            }else if(params.name === '40s'){
                $("#age_40s_bar").css("display", "block");
            }else if(params.name === '50s'){
                $("#age_50s_bar").css("display", "block");
            }
        });

        /* 获取年龄相关信息 */
        var age_20s_teachers = [];
        var age_30s_teachers = [];
        var age_40s_teachers = [];
        var age_50s_teachers = [];
        var vote_20s_numbers = [];
        var vote_30s_numbers = [];
        var vote_40s_numbers = [];
        var vote_50s_numbers = [];
        $.each(age, function(index, value){
            if(value >= 20 && value < 30){
                age_20s_teachers.push(teachers[index]);
                vote_20s_numbers.push(vote_numbers[index]);
            }else if(value >=30 && value < 40){
                age_30s_teachers.push(teachers[index]);
                vote_30s_numbers.push(vote_numbers[index]);
            }else if(value >= 40 && value < 50){
                age_40s_teachers.push(teachers[index]);
                vote_40s_numbers.push(vote_numbers[index]);
            }else if(value >= 50 && value <= 60){
                age_50s_teachers.push(teachers[index]);
                vote_50s_numbers.push(vote_numbers[index]);
            }
        });

        /* 添加20s条状图 */
        var age_20s_bar = echarts.init(document.getElementById('age_20s_bar'));
        age_20s_bar.setOption({
            title: {
                text: '20s'
            },
            tooltip: {},
            legend: {
                data: ['20s']
            },
            xAxis: {
                data: age_20s_teachers
            },
            yAxis: {},
            series: [{
                name: '20s', 
                type: 'bar',
                data: vote_20s_numbers
            }]
        });
        age_20s_bar.on('legendselectchanged', function(params){
            $("#age_20s_bar").css("display", "none");
            $("#age_pie").css("display", "block");
            var option = this.getOption();
            option.legend[0].selected[params.name] = true;
            this.setOption(option);
        });

        /* 添加30s条状图 */
        var age_30s_bar = echarts.init(document.getElementById('age_30s_bar'));
        age_30s_bar.setOption({
            title: {
                text: '30s'
            },
            tooltip: {},
            legend: {
                data: ['30s']
            },
            xAxis: {
                data: age_30s_teachers
            },
            yAxis: {},
            series: [{
                name: '30s', 
                type: 'bar',
                data: vote_30s_numbers
            }]
        });
        age_30s_bar.on('legendselectchanged', function(params){
            $("#age_30s_bar").css("display", "none");
            $("#age_pie").css("display", "block");
            var option = this.getOption();
            option.legend[0].selected[params.name] = true;
            this.setOption(option);
        });

        /* 添加40s条状图 */
        var age_40s_bar = echarts.init(document.getElementById('age_40s_bar'));
        age_40s_bar.setOption({
            title: {
                text: '40s'
            },
            tooltip: {},
            legend: {
                data: ['40s']
            },
            xAxis: {
                data: age_40s_teachers
            },
            yAxis: {},
            series: [{
                name: '40s', 
                type: 'bar',
                data: vote_40s_numbers
            }]
        });
        age_40s_bar.on('legendselectchanged', function(params){
            $("#age_40s_bar").css("display", "none");
            $("#age_pie").css("display", "block");
            var option = this.getOption();
            option.legend[0].selected[params.name] = true;
            this.setOption(option);
        });

        /* 添加50s条状图 */
        var age_50s_bar = echarts.init(document.getElementById('age_50s_bar'));
        age_50s_bar.setOption({
            title: {
                text: '50s'
            },
            tooltip: {},
            legend: {
                data: ['50s']
            },
            xAxis: {
                data: age_50s_teachers
            },
            yAxis: {},
            series: [{
                name: '50s', 
                type: 'bar',
                data: vote_50s_numbers
            }]
        }); 
        age_50s_bar.on('legendselectchanged', function(params){
            $("#age_50s_bar").css("display", "none");
            $("#age_pie").css("display", "block");
            var option = this.getOption();
            option.legend[0].selected[params.name] = true;
            this.setOption(option);
        });
    }   

    // 调用函数
    getSession();
    init_view();

    // 刷新
    // 点击刷新
    $("#refresh").click(function(){
        // alert('refresh');
        getSession();
        init_view();
    });
    // 定时刷新
    // $('body').everyTime('1s', 'time_refresh', function(){       // 计时器名称：time_refresh
    //     console.log('refresh');
    // });
    setInterval(function(){         // 每s执行一次
        // console.log('everyTime');
        getSession();
        init_view();
    }, 10000);   
});