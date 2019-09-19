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