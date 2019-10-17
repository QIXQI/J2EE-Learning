<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.io.*, java.util.*" %>
<%@ page import="club.search.Person" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 获取session, request 中的内容 -->
<%
    // int page1 = Integer.parseInt((String)session.getAttribute("page");       // java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
    int page1 = Integer.parseInt(session.getAttribute("page").toString());
    int result_num = Integer.parseInt(request.getAttribute("result_num").toString());
    int page_num = Integer.parseInt(request.getAttribute("page_num").toString());
    int start_index = Integer.parseInt(request.getAttribute("start_index").toString());
    int len_index = Integer.parseInt(request.getAttribute("len_index").toString());
    int start = (page_num > 5)? Integer.parseInt(request.getAttribute("start").toString()):0;
    boolean flag_start = (page_num > 5)?Boolean.parseBoolean(request.getAttribute("flag_start").toString()):false;
    boolean flag_end = (page_num > 5)?Boolean.parseBoolean(request.getAttribute("flag_end").toString()):false;
%>




<html lang="zh">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>管理员页面</title>
        <link rel="stylesheet" href="css/search.css" type="text/css" />
    </head>
    <body>
        <form action="/search32/Search" method="GET">
            <div class="search_name">
                <label>
                    <span style="color: rgb(60, 122, 252)">软</span>
                    <span style="color: rgb(251, 38, 10)">院</span>
                    <span style="color: rgb(255, 190, 0)">找</span>
                    <span style="color: rgb(60, 122, 252)">人</span>
                </label>
            </div>
            <div id="search_father">
                <div id="search">
                    <div class="search_empty"></div>
                    <div class="search_content">
                        <input type="text" name="searchContent" />
                    </div>
                </div>
                <button type="submit" aria-label="软院查人" class="search_go">
                    <div class="search_icon">
                        <span>
                            <svg focusable="false" xmls="http://www.w3.org/2000/svg" viewbox="0 0 24 24">
                                <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"></path>
                            </svg>
                        </span>
                    </div>
                </button>
            </div>
        </form>
        <!-- end of form -->

        <div id="itemsPerPage">
            <label for="pageSelect"><span>每页条数：</span></label>
            <select name="itemsPerPage" id="pageSelect">
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
                <option value="13">13</option>
                <option value="14">14</option>
                <option value="15">15</option>
                <option value="16">16</option>
                <option value="17">17</option>
                <option value="18">18</option>
                <option value="19">19</option>
                <option value="20">20</option>
            </select>
        </div>
        <!-- end of itemsPerPage -->

        <div class="search_result">
            <% if(result_num > 0) { %>
                <label class="result_title"><span>查找结果如下所示(${result_num}条)</span></label><br />
            <% }else{ %>
                <label class="result_title"><span>Sorry! 没有找到合适的结果</span></label><br />
            <% } %>
            <table>
                <% if(result_num != 0) { %>
                    <tr><td>学号（教工号）</td><td>姓名</td><td>手机号</td><td>QQ</td><td>邮箱</td><td class="delete start_tr"></td></tr>
                    <!-- result_num = 0 则 len_index = 0 -->
                    <c:forEach var="i" begin="${start_index}" end="${start_index + len_index - 1}" step="1">
                        <tr>
                            <td>${precises.get(i).getId()}</td>
                            <td>${precises.get(i).getName()}</td>
                            <td>${precises.get(i).getPhone()}</td>
                            <td>${precises.get(i).getQQ()}</td>
                            <td>${precises.get(i).getEmail()}</td>
                            <td class="delete"><img src="images/delete.png" alt="点击删除" /></td>
                        </tr>
                    </c:forEach>
                <% } %>
            </table>
            <img src="images/add.png" alt="点击添加" class="add" /> 
        </div>
        <!-- end of result display -->

        <!-- start of 分页 -->
        <% if(page_num > 1) { %>
            <div id="pages">
                <table>
                    <tbody><tr>
                        <% if(page1 > 1) { %>
                            <td><a class="pageUpDown" aria-label="PrePage" href="./Search?page=${page-1}">上一页</a></td>
                        <% } %>
                        <% if(page_num <= 5){ %>
                            <c:forEach var="i" begin="0" end="${page_num-1}" step="1">
                                <td><a class="pageNum" aria-label="Page${i+1}" href="./Search?page=${i+1}">${i+1}</a></td>
                            </c:forEach>
                        <% } %>
                        <% if(page_num > 5){ %>
                            <% if(flag_start) { %>
                                <td><a class="pageFirst" aria-label="PageFirst" href="./Search?page=1">1</a></td>
                                <td><span class="pageLeave">...</span></td>
                            <% } %>
                            <c:forEach var="i" begin="${start}" end="${start+4}" step="1">
                                <td><a class="pageNum" aria-label="Page${i+1}" href="./Search?page=${i+1}">${i+1}</a></td>
                            </c:forEach>
                            <% if(flag_end) { %>
                                <td><span class="pageLeave">...</span></td>
                                <td><a class="pageLast" aria-label="PageLast" href="./Search?page=${page_num}">${page_num}</a></td>
                            <% } %>
                        <% } %>
                        <% if(page1 < page_num) { %>
                            <td><a class="pageUpDown" aria-label="NextPage" href="./Search?page=${page+1}">下一页</a></td>
                        <% } %>
                    </tr></tbody>
                </table>
            </div>
        <% } %>
        <!-- end of 分页 -->

        <% if(result_num <= 5) { %>
            <script type="text/javascript">
                document.getElementById('itemsPerPage').style.display = 'none';
            </script>
        <% } %>
        <script type="text/javascript">
            document.getElementById('itemsPerPage').getElementsByTagName('option')[${itemsPerPage-5}].selected = 'selected';

            for(var i=0; i<document.getElementById('pages').getElementsByTagName('a').length; i++){
                if(document.getElementById('pages').getElementsByTagName('a')[i].innerText == '${page}'){
                    document.getElementById('pages').getElementsByTagName('a')[i].style.color = '#fc6423';
                }
            }
        </script>
        <script type="text/javascript" src="./javascript/jquery-3.4.1.min.js"></script>
        <script type="text/javascript" src="./javascript/search.js"></script>
    </body>
</html>