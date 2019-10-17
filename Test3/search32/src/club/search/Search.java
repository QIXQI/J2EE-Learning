/** 
 * 如何将precises, fuzzys, plen, flen 存储到session中
 * java参数传递值，如何修改原来的值
 * 访问 WebContent 下的资源使用 ./
 *  对于Web / 相当于整个服务器的根目录 即，http://localhost:8080
 *  但是WEB-INF不可以使用 ./来访问里面的资源, WEB-INF，如果没有配置路径，只能使用绝对路径（好像是虚拟路径那个玩意），或者知道当前路径，转换位相对路径
 *  /WEB-INF/contact/test.xls 访问失败
 *  如何添加相对地址
 *          --> File file = new File(getServletContext().getRealPath("contact/test.xls"));      return "/Users/zhengxiang/Desktop/hello/j2ee/tomcat/apache-tomcat-8.5.42-5/webapps/search/contact/test.xls"
 *  request.getRequestDispatcher("/") 中的根目录是项目根目录，不是网站根目录
 *  DataManager.persons 数组管理更加灵活，提高效率，不用每次都清空更新
 */
package club.search;
 
import java.io.*;
import java.sql.*;
import java.util.*;
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

import club.search.DataManager;

@WebServlet("/Search")
public class Search extends HttpServlet{
    private static final long serialVersionUID = 1L;

    
    /**
     * 从连接池中连接数据库，获取数据库中的人员信息
     */
    
    @Override
    public void init() throws ServletException{
        DataManager.setPersons();
    } 
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        // persons 数组放在方面为了同步，不同管理员的persons数组是同一个
        List<Person> persons = DataManager.getPersons(); 
        for(int i=0; i<persons.size(); i++){
            System.out.print(persons.get(i).getId());
            System.out.print(" " + persons.get(i).getName());
            System.out.print(" " + persons.get(i).getPhone());
            System.out.print(" " + persons.get(i).getQQ());
            System.out.println(" " + persons.get(i).getEmail());
        }
        List<Person> precises = new ArrayList<Person>();        // 精确匹配数组
        List<Person> fuzzys = new ArrayList<Person>();          // 模糊匹配数组
        String search;
        int itemsPerPage = 5;   // 默认值是5
        int page = 1;       // 默认首页

        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);

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
        
        for(int i=0; i<persons.size(); i++){
            int temp = persons.get(i).match(search);
            if(temp == 2){      // 精确匹配
                precises.add(persons.get(i));
            }else if(temp == 1){    // 模糊匹配
                fuzzys.add(persons.get(i));
            }
        }

        int result_num = precises.size() + fuzzys.size();      // 实际查找条数
        int page_num = result_num / itemsPerPage + 1;       // 需着重考虑只有一页的情况
        int start_index;
        int len_index;
        if(result_num % itemsPerPage == 0){
            page_num --;
        }
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

        // 将page, search, itemsPerPage 写入session
        search = "20";
        if(search != null && !search.equals("")){
            precises.addAll(fuzzys);        // 合并结果
            session.setAttribute("search", URLEncoder.encode(search, "UTF-8"));
            session.setAttribute("page", page);
            session.setAttribute("itemsPerPage", itemsPerPage);
            session.setAttribute("precises", precises);
            // session.setAttribute("fuzzys", fuzzys);
            session.setMaxInactiveInterval(600);
        }

        /**
         * page_num = 1 不分页
         * 1 < page_num <=5 没有lastpage
         * page_num >5 有lastpage
         */
        if(page_num > 1){
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
                request.setAttribute("start", start);
                request.setAttribute("flag_start", flag_start);
                request.setAttribute("flag_end", flag_end);
            }
        }
        request.setAttribute("result_num", result_num);
        request.setAttribute("page_num", page_num);
        request.setAttribute("start_index", start_index);
        request.setAttribute("len_index", len_index);

        // 转到 admin.jsp
        request.getRequestDispatcher("/admin.jsp").forward(request, response);     
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

}