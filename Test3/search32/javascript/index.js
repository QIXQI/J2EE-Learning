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


// 个人中心登录
var info = document.getElementById('info');
info.getElementsByTagName('img')[0].onclick = function(){
    window.location.href = './login.html';
};


// 图片上传
var file_uploader = document.getElementById('camera').getElementsByTagName('img')[0];
var browserFile = document.getElementById('empty_file').getElementsByClassName('fileupload')[0];    // 浏览文件
file_uploader.onclick = function(e){
    browserFile.click();
    e.stopPropagation();    // 阻止冒泡
};
browserFile.onchange = function(e){
    // alert('hello');
    document.getElementById('upload').submit();
};