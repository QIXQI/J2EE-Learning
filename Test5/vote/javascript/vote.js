/**
 * 1. 复选框change事件中，每次改变 alert($(this).index()); 都弹出2
 */



// 全局配置（可扩展性）
var max_choose_num = 3;         // 用户最多选择3个，但至少选择一个


// 文档加载完毕
$(document).ready(function() {
    // 填充显示 max_choose_num 提示
    // alert($("#thead .max_choose_num").text());
    $("#thead .max_choose_num").text(max_choose_num);


    // 复选框选中与取消
    $("input[type=checkbox]").change(function(){
        if($("input[type=checkbox]:checked").length > max_choose_num){
            alert('最多选择三个');
            // $(this).removeAttr('checked');    // 无效
            $(this).prop('checked', false);      // 标准写法
            // alert($(this).val());
            // alert($(this).index());           // 奇怪，每次都输出2
            // alert($(this).html());
            // alert($("input[type=checkbox]:checked").length);
        }
    });

    // 提交事件处理
    $("#vote_form").submit(function(){
        if($("input[type=checkbox]:checked").length < 1){
            alert('最少选择一个');
            return false;
        }
    });
});