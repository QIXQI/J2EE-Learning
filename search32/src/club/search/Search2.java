/** 
 * 如何将precises, fuzzys, plen, flen 存储到session中
 * java参数传递值，如何修改原来的值
 */
package club.search;
 
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import java.net.URLDecoder;

import javax.servlet.annotation.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * 访问 WebContent 下的资源使用 ./
 *  对于Web / 相当于整个服务器的根目录 即，http://localhost:8080
 *  但是WEB-INF不可以使用 ./来访问里面的资源, WEB-INF，如果没有配置路径，只能使用绝对路径（好像是虚拟路径那个玩意），或者知道当前路径，转换位相对路径
 *  /WEB-INF/contact/test.xls 访问失败
 *  如何添加相对地址
 *          --> File file = new File(getServletContext().getRealPath("contact/test.xls"));      return "/Users/zhengxiang/Desktop/hello/j2ee/tomcat/apache-tomcat-8.5.42-5/webapps/search/contact/test.xls"
 * */


@WebServlet("/Search2")
public class Search2 extends HttpServlet{
    private static final long serialVersionUID = 1L;

    Person[] persons = new Person[100];     // persons 对象数组
    private final int search_num = 100;      // 最大搜索条数

    // private int[] precises = new int[search_num];       // 默认值是0，精确匹配 person下标  + 1
    // private int[] fuzzys = new int[search_num];         // 模糊匹配 person下标 + 1
    // int plen = 0;       // precises 当前长度
    // int flen = 0;       // fluzzys 当前长度
    // int itemsPerPage = 5;       // 默认值是5，默认值是8时每次刷新都是8
    // int page = 1;               // 默认首页

    // protected void searchResult(String search){
        // for(int i=0; i<persons.length && persons[i]!=null; i++){
            // int temp = persons[i].match(search);
            // if(temp == 2){
                // precises[plen ++] = i + 1;
            // }else if(temp == 1){
                // fuzzys[flen ++] = i + 1;
            // }
        // }
    // }

    @Override
    public void init() throws ServletException{
        String id;
        String name;
        String phone;
        String qq;
        String email;

        try{
            Context ctx  = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ExamDB");
            Connection conn = ds.getConnection();

            String sql = "select * from persons";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int t = 0;
            while(rs.next()){
                id = rs.getString("id");
                name = rs.getString("name");
                phone = rs.getString("phone");
                qq = rs.getString("qq") == null ? "" : rs.getString("qq");
                email = rs.getString("email") == null ? "" : rs.getString("email");
                persons[t] = new Person(id, name, phone, qq, email);
                t ++;
            }
        } catch(NamingException ne){
            ne.printStackTrace();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int[] precises = new int[search_num];   // 默认值0, 精确匹配 (person下标 + 1)
        int[] fuzzys = new int[search_num];     // 模糊匹配，(person下标 + 1)
        int plen = 0;       // precises 当前长度
        int flen = 0;       // fluzzys  当前长度
        String search;
        int itemsPerPage = 5;   // 默认值是5
        int page = 1;       // 默认首页


        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);

        // session.setAttribute("search", URLEncoder.encode("你好", "UTF-8"));
        // session.setAttribute("page", "5");
        // session.setAttribute("pageSize", "100");
        // session.setMaxInactiveInterval(60);

        // 设置日期输出格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        request.setCharacterEncoding("utf-8");      // Post方式
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        // server.xml   中默认设置位 UTF-8
        // String search = new String(request.getParameter("search").getBytes("ISO8859-1"), "UTF-8");
        search = request.getParameter("searchContent");
        if(request.getParameter("itemsPerPage") != null && !request.getParameter("itemsPerPage").equals("")){
            itemsPerPage = Integer.parseInt(request.getParameter("itemsPerPage"));
            if(itemsPerPage < 5){
                itemsPerPage = 5;
            }else if(itemsPerPage > 20){
                itemsPerPage = 20;
            }
            if(session.getAttribute("search") != null && search == null){
                search = URLDecoder.decode((String)session.getAttribute("search"), "UTF-8");
            }
            if(session.getAttribute("search") != null && (request.getParameter("page") == null || request.getParameter("page").equals(""))){
                // page = Integer.parseInt((String)session.getAttribute("page"));
                page = (Integer)session.getAttribute("page");
            }
        }
        if(request.getParameter("page") != null && !request.getParameter("page").equals("")){
            page = Integer.parseInt(request.getParameter("page"));
            if(session.getAttribute("search") != null && search == null){
                search = URLDecoder.decode((String)session.getAttribute("search"), "UTF-8");  
            }
            if(session.getAttribute("search") != null && (request.getParameter("itemsPerPage") == null || request.getParameter("itemsPerPage").equals(""))){
                // itemsPerPage = Integer.parseInt((String)session.getAttribute("itemsPerPage"));
                itemsPerPage = (Integer)session.getAttribute("itemsPerPage");
            }
        }
        
        // if(search != null){
            // plen = 0;		// 很奇怪，再次查找时值不是0，有上次查找的结果
            // flen = 0;
        // }
        // out.println("<p>plen = " + plen + ",  flen = " + flen + "</p>\n");
        
        // searchResult(search);
        for(int i=0; i<persons.length && persons[i] !=null; i++){
            int temp = persons[i].match(search);
            if(temp == 2){
                precises[plen ++] = i + 1;
            }else if(temp == 1){
                fuzzys[flen ++] = i + 1;
            }
        }





        int result_num = plen + flen;      // 实际查找条数
        out.println("<!DOCTYPE html>\n" +
            "<html lang=\"zh\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\" />\n" +
            "<title>search</title>\n" +
            "<link rel=\"stylesheet\" href=\"./css/search.css\" type=\"text/css\" />\n" +
            "</head>\n" +
            "<body>\n" + 
            "<form action=\"http://localhost:8080/search3/Search\" method=\"GET\">\n" +
            "<div class=\"search_name\">\n" +
            "<label>\n" +
            "<span style=\"color: rgb(60, 122, 252);\">软</span>\n" +
            "<span style=\"color: rgb(251, 38, 10);\">院</span>\n" +
            "<span style=\"color: rgb(255, 190, 0);\">找</span>\n" +
            "<span style=\"color: rgb(60, 122, 252);\">人</span>\n" +
            "</label>\n" +
            "</div>\n" +
            "<div id=\"search_father\">\n" +
            "<div id=\"search\">\n" +
            "<div class=\"search_empty\"></div>\n" +
            "<div class=\"search_content\">\n" +
            "<input type=\"text\" name=\"searchContent\" />\n" +
            "</div>\n" +
            "</div>\n" +
            "<button type=\"submit\" aria-label=\"软院查人\" class=\"search_go\">\n" +
            "<div class=\"search_icon\">" +
            "<span>\n" +
            "<svg focusable=\"false\" xmls=\"http://www.w3.org/2000/svg\" viewbox=\"0 0 24 24\">\n" +
            "<path d=\"M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 " + 
            "3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 " + 
            "5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z\"></path>\n" +
            "</svg>\n" +
            "</span>\n" +
            "</div>\n" +
            "</button>\n" +
            "</div>\n" +
            "</form>\n" +
            "<div id=\"itemsPerPage\">" +
            "<label for=\"pageSelect\"><span>每页条数：</span></label>\n" +
            "<select name=\"itemsPerPage\" id=\"pageSelect\">\n" +
            "<option value=\"5\">5</option>\n" +
            "<option value=\"6\">6</option>\n" +
            "<option value=\"7\">7</option>\n" +
            "<option value=\"8\">8</option>\n" +
            "<option value=\"9\">9</option>\n" +
            "<option value=\"10\">10</option>\n" +
            "<option value=\"11\">11</option>\n" +
            "<option value=\"12\">12</option>\n" +
            "<option value=\"13\">13</option>\n" +
            "<option value=\"14\">14</option>\n" +
            "<option value=\"15\">15</option>\n" +
            "<option value=\"16\">16</option>\n" +
            "<option value=\"17\">17</option>\n" +
            "<option value=\"18\">18</option>\n" +
            "<option value=\"19\">19</option>\n" +
            "<option value=\"20\">20</option>\n" +
            "</select>\n" +
            "</div>\n" +
            "<div class=\"search_result\">\n"
        );
        // out.println("<h2>" + search + page + itemsPerPage + "</h2>");
        // out.println(getServletContext().getRealPath("contact/test.xls"));        // return "/Users/zhengxiang/Desktop/hello/j2ee/tomcat/apache-tomcat-8.5.42-5/webapps/search/"
        if(result_num > 0){
            out.println("<label class=\"result_title\"><span>查找结果如下所示(" + result_num + "条)</span></label><br />\n");
        }else{
            out.println("<label class=\"result_title\"><span>Sorry! 没有找到合适的结果</span></label><br />\n");
        }
        out.println("<table>\n");
        if(result_num != 0) {
            out.println("<tr><td>学号（教工号）</td><td>姓名</td><td>手机号</td><td>QQ</td><td>邮箱</td><td class=\"delete start_tr\"></td></tr>\n");
        }
        // boolean flag1 = false;      // 是否需要分页 page = 1
        // boolean flag2 = false;      // 是否页数大于5 page > 5
        // boolean flag3 = false;      // 是否需要跳转页面中包含最后一页   1 < page <=5
        int page_num = result_num / itemsPerPage + 1;       // 需着重考虑只有一页的情况
        if(result_num % itemsPerPage == 0){
            page_num --;
        }
        int start_index;
        int len_index;
        // 处理 search 改变后，实际page_num发生改变，但是page没变，溢出
        if(page > page_num || page < 1){
            page = 1;
        }
        if(page < page_num){           // 不是最后一页 
            start_index = (page - 1) * itemsPerPage;
            len_index = itemsPerPage;
        }else{                         // 最后一页
            start_index = (page - 1) * itemsPerPage;
            len_index = result_num - (page - 1) * itemsPerPage;
        }
        // out.println("<h2>" + start_index +" " + len_index + "</h2>"); 
        // out.println("<h2>" + plen + " " + flen + "</h2>");
        for(int i=start_index; i<start_index + len_index; i++){
            if(i < plen){
                // out.println("<label><span>");
            	out.println("<tr>"); 
                out.println("<td>" + persons[precises[i] - 1].getId() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getName() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getPhone() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getQQ() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getEmail() + "</td>");
                out.println("<td class=\"delete\"><img src=\"images/delete.png\" alt=\"点击删除\" /></td>");
                // out.println("</span></label><br />\n");
                out.println("</tr>\n");
            }else if(i - plen < flen){
                // out.println("<label><span>");
            	out.println("<tr>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getId() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getName() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getPhone() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getQQ() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getEmail() + "</td>");
                out.println("<td class=\"delete\"><img src=\"images/delete.png\" alt=\"点击删除\" /></td>");
                // out.println("</span></label><br />\n");
                out.println("</tr>\n");
            }
        }
        out.println("</table>\n");
        out.println("<img src=\"images/add.png\" alt=\"点击添加\" class=\"add\" />\n");
        out.println("</div>\n");


        // 将page, search, itemsPerPage 写入session
        if(search != null && !search.equals("")){
            session.setAttribute("search", URLEncoder.encode(search, "UTF-8"));
            session.setAttribute("page", page);
            session.setAttribute("itemsPerPage", itemsPerPage);
            session.setMaxInactiveInterval(600);
        }

        /**
         * page_num = 1 不分页
         * 1 < page_num <=5 没有lastpage
         * page_num >5 有lastpage
         */
        if(page_num > 1){
            out.println(
                "<div id=\"pages\">\n" +
                "<table>\n" +
                "<tbody><tr>\n"
            );
            if(page > 1){
                out.println("<td><a class=\"pageUpDown\" aria-label=\"PrePage\" href=\"./Search?page=" + (page-1) + "\">上一页</a></td>\n");
            }
            
            if(page_num <= 5){
                /*out.println(
                    "<td><a class=\"pageNum\" aria-label=\"Page1\" href=\"./Search?page=1\">1</a></td>\n" +
                    "<td><a class=\"pageNum\" aria-label=\"Page2\" href=\"./Search?page=2\">2</a></td>\n" +
                    "<td><a class=\"pageNum\" aria-label=\"Page3\" href=\"./Search?page=3\">3</a></td>\n" +
                    "<td><a class=\"pageNum\" aria-label=\"Page4\" href=\"./Search?page=4\">4</a></td>\n" +
                    "<td><a class=\"pageNum\" aria-label=\"Page5\" href=\"./Search?page=5\">5</a></td>\n"
                );*/
                for(int i=0; i<page_num; i++){
                    out.println("<td><a class=\"pageNum\" aria-label=\"Page" + (i+1) + "\" href=\"./Search?page=" + (i+1) + "\">" + (i+1) + "</a></td>\n");
                }
            }
             
            if(page_num > 5){
                int start;
                boolean flag_start = false;      // 显示首页链接
                boolean flag_end = false;        // 显示尾页链接
                if(page <= 3){
                    start = 0;
                    flag_end = true;
                }else if(page_num - page > 2){
                    start = page - 3;
                    flag_start = true;
                    flag_end = true;
                }else{
                    start = page_num - 5;
                    flag_start = true;
                }
                if(flag_start){
                    out.println(
                        "<td><a class=\"pageFirst\" aria-label=\"PageFirst\" href=\"./Search?page=" + 1 + "\">" + 1 + "</a></td>\n" +
                        "<td><span class=\"pageLeave\">...</span></td>\n"
                    );
                }
                for(int i=start; i<start+5; i++){
                    out.println("<td><a class=\"pageNum\" aria-label=\"Page" + (i+1) + "\" href=\"./Search?page=" + (i+1) + "\">" + (i+1) + "</a></td>\n");
                }
                if(flag_end){
                    out.println(
                        "<td><span class=\"pageLeave\">...</span></td>\n" +
                        "<td><a class=\"pageLast\" aria-label=\"PageLast\" href=\"./Search?page=" + page_num + "\">" + page_num + "</a></td>\n"
                    );
                }




                /*for(int i=0; i<5; i++){
                    out.println("<td><a class=\"pageNum\" aria-label=\"Page" + (i+1) + "\" href=\"./Search?page=" + (i+1) + "\">" + (i+1) + "</a></td>\n");
                }*/

                /*out.println(
                    "<td><span class=\"pageLeave\">...</span></td>\n" +
                    "<td><a class=\"pageLast\" aria-label=\"PageLast\" href=\"./Search?page=" + page_num + "\">" + page_num + "</a></td>\n"
                );*/
            }
            
            
            if(page < page_num){
                out.println("<td><a class=\"pageUpDown\" aria-label=\"NextPage\" href=\"./Search?page=" + (page+1) + "\">下一页</a></td>\n");
            }

            out.println(
                "</tr></tbody>\n" +
                "</table>\n" +
                "</div>\n"
            );
        }

        if(result_num <= 5){         

            // result_num <= 5时不显示下拉框
            out.println(
                "<script type=\"text/javascript\">\n" +
                "document.getElementById('itemsPerPage').style.display = 'none';\n" +
                "</script>\n"
            );
        }

        // 改变 select选择
        out.println(
            "<script type=\"text/javascript\">\n" +
            "document.getElementById('itemsPerPage').getElementsByTagName('option')[" + (itemsPerPage-5) + "].selected = 'selected';\n" +
            "</script>\n"
        );

        // 修改 page 的样式
        out.println(
            "<script type=\"text/javascript\">\n" +
            "for(var i=0; i<document.getElementById('pages').getElementsByTagName('a').length; i++){\n" +
            "if(document.getElementById('pages').getElementsByTagName('a')[i].innerText == '" + page + "'){\n" +
            "document.getElementById('pages').getElementsByTagName('a')[i].style.color = '#fc6423';\n" +
            "}\n" +
            "}\n" +
            "</script>\n"
        );

        out.println("<script type=\"text/javascript\" src=\"./javascript/jquery-3.4.1.min.js\"></script>");

        out.println(
            "<script type=\"text/javascript\" src=\"./javascript/search.js\"></script>\n" +
            "</body>\n" +
            "</html>"
        );

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

}