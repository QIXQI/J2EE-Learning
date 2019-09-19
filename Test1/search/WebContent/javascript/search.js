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
};


// 去除没有结果时的上边框
var table = document.getElementsByTagName('table')[0];
var search_result_label = document.getElementsByClassName('search_result')[0].getElementsByTagName('label')[0];
// alert(table.rows.length);
if(table.rows.length == 0){
	table.style.border = 'none';
	search_result_label.style.marginLeft = '220px';
}