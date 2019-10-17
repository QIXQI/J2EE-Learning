/**
 * 获取 table 某一单元格的 width, height 使用 offsetWidth, offsetHeight
 * 如何使用 jquery 来处理 ajax 请求
 * jquery 中 find 与 children 的寻找子元素的区别
 * ajax 传递的信息 trim()
 */

// xhr 放在这里，每次响应返回的data 条数累加
// var xhr = new XMLHttpRequest();

var form = document.getElementsByTagName('form')[0]; 
var search = document.getElementsByClassName('search_content')[0].getElementsByTagName('input')[0];

function checkSearch(){
    if(search.value.trim() == ''){
        return false;
    }
    return true;
}

form.onsubmit = function(){
    if(!checkSearch()){
        return false;
    }
    // return false;
};


// 去除没有结果时的上边框
// var table = document.getElementsByTagName('table')[0];
var table = document.getElementsByClassName('search_result')[0].getElementsByTagName('table')[0];
var search_result_label = document.getElementsByClassName('search_result')[0].getElementsByTagName('label')[0];
// alert(table.rows.length);
if(table.rows.length == 0){
	table.style.border = 'none';
	search_result_label.style.marginLeft = '220px';
}


// 避免尴尬，将姓名打码
var table_temp = document.getElementsByClassName('search_result')[0];
var tr_temp = table_temp.getElementsByTagName('tr');
/*for(var i=1; i<tr_temp.length; i++){
    tr_temp[i].getElementsByTagName('td')[1].innerText = '***********';
}*/
 

var select = document.getElementById('itemsPerPage').getElementsByTagName('select')[0];
select.onchange = function(){
    // alert(select.value);
    window.location.href = 'http://localhost:8080/search32/Search?itemsPerPage=' + select.value;
};


// 双击修改表单项的值，弹出框是否保存提交
var editMsg = document.getElementById('editMsg');
var tr_len = tr_temp.length;
var td_len = tr_temp[0].getElementsByTagName('td').length;
var saveMsg;
for(let i=1; i<tr_len; i++){
    for(let j=0; j<td_len-1; j++){ 
        tr_temp[i].getElementsByTagName('td')[j].ondblclick = function(){
            if(tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input').length > 0){
                tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].focus();
                return;
            }
            saveMsg = tr_temp[i].getElementsByTagName('td')[j].innerText;
            // alert(saveMsg);
            // alert(tr_temp[i].getElementsByTagName('td')[j].offsetWidth);
            // alert(tr_temp[i].getElementsByTagName('td')[j].offsetHeight);
            var td_width = tr_temp[i].getElementsByTagName('td')[j].offsetWidth;
            var td_height = tr_temp[i].getElementsByTagName('td')[j].offsetHeight;
            // alert(td_width + ' ' + td_height);

            tr_temp[i].getElementsByTagName('td')[j].innerHTML = '<input type="text" class="editMsg" name="editMsg" />';
            tr_temp[i].getElementsByTagName('td')[j].width = td_width - 3;      // 减三后与以前一致
            tr_temp[i].getElementsByTagName('td')[j].height = td_height - 3;
            // alert(td_width + ' ' + td_height);
            // tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].style.backgroundColor = 'red';
            // alert(tr_temp[i].getElementsByTagName('td')[j].offsetWidth + ' ' + tr_temp[i].getElementsByTagName('td')[j].offsetHeight);
            tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].style.width = td_width - 3 - 6 + 'px';    // 减去6单元格才可以长宽不变
            tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].style.height = td_height - 3 - 6 + 'px';
            // alert(tr_temp[i].getElementsByTagName('td')[j].offsetWidth + ' ' + tr_temp[i].getElementsByTagName('td')[j].offsetHeight);

            // 输入框提示信息
            tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].placeholder = saveMsg;
            tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].focus();
            // tr_temp[i].getElementsByTagName('td')[j].innerText = '';
            // alert('第' + i + '行' + '第' + j + '列');


            
            // 焦点消失事件处理
            // tr_temp[t1].getElementsByTagName('td')[t2].onclick = function(){
            //     alert('点击元素第' + t1 + '行，第' + t2 + '列');
            // };
            // 输入框焦点消失事件
            tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].onblur = function(){
                // alert('失去焦点：第' + i + '行，第' + j + '列');
                if(tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].value.trim() == '' || tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].value == saveMsg){
                    // 不用提交数据
                    tr_temp[i].getElementsByTagName('td')[j].innerHTML = saveMsg;
                }else{
                    // 提交数据
                    // alert('提交数据到 Edit 页面处理');
                    var ajaxId = tr_temp[i].getElementsByTagName('td')[0].innerText;
                    var ajaxIndex = j;
                    var ajaxVal = tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].value;
                    var xhr = new XMLHttpRequest();
                    xhr.open('GET', '/search32/Edit?ajaxId='+ajaxId+'&ajaxIndex='+ajaxIndex+'&ajaxVal='+ajaxVal, true);
                    xhr.send();
                    xhr.addEventListener('load', function(){
                        console.log(xhr.status);
                        if((xhr.status >= 200 && xhr.status < 300) || (xhr.status == 304)){
                            // 处理正确
                            var data = xhr.responseText;
                            alert('响应数据: ' + data);

                            // todo 判断响应数据是否正确
                            tr_temp[i].getElementsByTagName('td')[j].innerHTML = ajaxVal;
                        }else{
                            // 处理失败
                            alert('出错了');
                            tr_temp[i].getElementsByTagName('td')[j].innerHTML = saveMsg;
                        }
                    });

                }
            };
        };

    }
}

// alert(document.getElementById('pages').getElementsByTagName('td')[1].innerText)
/* for(var i=0; i<document.getElementById('pages').getElementsByTagName('a').length; i++){
    if(document.getElementById('pages').getElementsByTagName('a')[i].innerText == '1'){
        alert(i);
        document.getElementById('pages').getElementsByTagName('a')[i].style.color = '#fc6423';
    }
}*/


// add.png 点击添加表单输入框事件
$(".add").click(function(){
    // table 添加一行
    $(".search_result table").append('<tr><td></td><td></td><td></td><td></td><td></td><td class="add_submit"><img src="images/add_submit.png" alt="点击提交添加信息" /></td></tr>');
    
    // 添加输入框 jquery 不太好支持 offsetWidth, offsetHeight
    // alert($(".search_result table").find("tr").last().html());
    // for(var i=0; i<$(".search_result table").find("tr").last().find("td").length - 1; i++){
        // alert($(".search_result table").find("tr").last().find("td").eq(i).attr("offsetWidth"));
        // alert($(".search_result table").find("tr").last().find("td").eq(i).offset().left);
    // }

    // javascript 方法
    var table_len = table.getElementsByTagName('tr').length;
    var add_tr = table.getElementsByTagName('tr')[table_len - 1];
    var start_tr = table.getElementsByTagName('tr')[0];
    for(var i=0; i<add_tr.getElementsByTagName('td').length - 1; i++) {
        var td_width = add_tr.getElementsByTagName('td')[i].offsetWidth;
        var td_height = add_tr.getElementsByTagName('td')[i].offsetHeight;
        add_tr.getElementsByTagName('td')[i].innerHTML = '<input type="text" class="addMsg" name="addMsg" />';
        add_tr.getElementsByTagName('td')[i].width = td_width - 3;
        add_tr.getElementsByTagName('td')[i].height = td_height - 3;
        add_tr.getElementsByTagName('td')[i].getElementsByTagName('input')[0].style.width = td_width - 3 - 6 +'px';
        add_tr.getElementsByTagName('td')[i].getElementsByTagName('input')[0].style.height = td_height - 3 - 6 + 'px';
        // 提示信息
        add_tr.getElementsByTagName('td')[i].getElementsByTagName('input')[0].placeholder = start_tr.getElementsByTagName('td')[i].innerText;
    }

    // 提交添加的信息
    $(".add_submit").last().click(function(){
        alert('提交数据到 Add 页面');
        var ajaxId = $(this.parentNode).children('td').eq(0).find('input').eq(0).val().trim();
        var ajaxName = $(this.parentNode).children('td').eq(1).find('input').eq(0).val().trim();
        var ajaxPhone = $(this.parentNode).children('td').eq(2).find('input').eq(0).val().trim();
        var ajaxQQ = $(this.parentNode).children('td').eq(3).find('input').eq(0).val().trim();
        var ajaxEmail = $(this.parentNode).children('td').eq(4).find('input').eq(0).val().trim();
        if(ajaxId == '' || ajaxName == '' || ajaxPhone == ''){
            alert('关键字段不能为空');
            return;
        }
        var add_url = '/search32/Add?ajaxId=' + ajaxId + '&ajaxName=' + ajaxName + '&ajaxPhone=' + ajaxPhone;
        if(ajaxQQ != ''){
            add_url += '&ajaxQQ=' + ajaxQQ;
        }
        if(ajaxEmail != ''){
            add_url += '&ajaxEmail=' + ajaxEmail;
        }
        var xhr = new XMLHttpRequest();
        xhr.open('GET', add_url, true);
        xhr.send();
        xhr.addEventListener('load', function(){
            console.log(xhr.status);
            if((xhr.status >= 200 && xhr.status < 300) || (xhr.status == 304)){
                // 处理正确
                var data = xhr.responseText;
                alert('响应数据: ' + data);

                // todo 判断响应内容是否正确
            }else{
                // 处理失败
                alert('出错了');
            }
        });
    });

});



// 删除用户事件处理
$(".delete:not(.start_tr)").click(function(){
    // alert(this.parentNode.innerHTML);           // 获取父节点信息
    // ajax 提交信息
    alert('提交数据到 Delete 页面');
    var ajaxId = $(this.parentNode).children('td').eq(0).text();
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/search32/Delete?ajaxId=' + ajaxId, true);
    xhr.send();
    xhr.addEventListener('load', function(){
        console.log(xhr.status);
        if((xhr.status >= 200 && xhr.status < 300) || (xhr.status == 304)){
            // 处理正确
            var data = xhr.responseText;
            alert('响应数据: ' + data);

            // todo 判断响应内容是否正确
        }else{
            // 处理失败
            alert('出错了');
        }
    });
});





