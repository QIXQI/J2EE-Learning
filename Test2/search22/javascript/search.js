/**
 * 获取 table 某一单元格的 width, height 使用 offsetWidth, offsetHeight
 */

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
    window.location.href = 'http://localhost:8080/search22/Search?itemsPerPage=' + select.value;
};


// 双击修改表单项的值，弹出框是否保存提交
var editMsg = document.getElementById('editMsg');
var tr_len = tr_temp.length;
var td_len = tr_temp[0].getElementsByTagName('td').length;
var saveMsg;
for(let i=1; i<tr_len; i++){
    for(let j=0; j<td_len; j++){
        tr_temp[i].getElementsByTagName('td')[j].ondblclick = function(){
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
            tr_temp[i].getElementsByTagName('td')[j].getElementsByTagName('input')[0].focus();
            // tr_temp[i].getElementsByTagName('td')[j].innerText = '';
            // alert('第' + i + '行' + '第' + j + '列');
            
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