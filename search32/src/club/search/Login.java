/**
 * Get 方式处理游客登录
 * Post 方式处理管理员登录
 */
package club.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;


import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/Login")
public class Login extends HttpServlet{
    private static final long serialVersionUID = 1L;

    // 构造函数
    public Login(){
        super();
    }

    // 处理游客登录
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(true);
        // response.setContentType("text/html; charset=utf-8");
        // PrintWriter out = response.getWriter();

        // String lastRequestURL = request.getRequestURL().toString();
        // String lastRequestURL = (String)session.getAttribute("lastRequestURL");

        int tempSubmit = 0;
        if(request.getParameter("tempSubmit") != null){
            tempSubmit = Integer.parseInt(request.getParameter("tempSubmit"));
        }
        String title = "游客登录处理页面";
        String docType = "<!DOCTYPE html>\n";
        // out.println(docType +
            // "<html lang='zh'>\n" +
            // "<head><title>" + title + "</title></head>\n" +
            // "<body>\n" +
            // "<h1 align='center'>" + title + "</h1>\n");
        
        // out.println("<p align='center'>" + lastRequestURL + "</p>\n");
        if(tempSubmit == 1){    // 写入 session
            // out.println("<p align='center'>游客登录成功</p>\n");
            session.setAttribute("adminID", "null");        // adminID 表示游客
            session.setMaxInactiveInterval(600);            // 10分钟
            // out.println("<p align='center'>设置session成功</p></body></html>\n");
            request.getRequestDispatcher("./Search").forward(request, response);
        }else{
            // out.println("<p align='center'>游客登录失败</p></body></html>\n");
            response.getWriter().println("<p align='center'>游客登录失败</p></body></html>\n");
        }
    }
    
    // 处理管理员登录
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);

        // String lastRequestURL = request.getRequestURL().toString();
        // String lastRequestURL = (String)session.getAttribute("lastRequestURL");

        // 设置日期输出的格式
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Connection conn = null;
        PreparedStatement pst = null;

        // 请求解决乱码
        request.setCharacterEncoding("utf-8");

        // 响应解决乱码
        // response.setContentType("text/html; charset=utf-8");

        // 接收表单数据
        String admin = request.getParameter("login_field");
        String password = request.getParameter("password");

        // PrintWriter out = response.getWriter();
        // String title = "管理员登录处理页面";
        // String docType = "<!DOCTYPE html>\n";
        // out.println(docType +
            // "<html lang='zh'>\n" +
            // "<head><title>" + title + "</title></head>\n" +
            // "<body>\n" +
            // "<h1 align='center'>" + title + "</h1>\n");

        // out.println("<p align='center'>" + lastRequestURL + "</p>\n");
        try{
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/ExamDB");
            conn = ds.getConnection();

            // 执行 sql 查询
            String sql;
            sql = "select id, name, email, password, register_time, last_login_time, iconID from administrators where (id = '" + admin + "' or email = '" + admin + "') and password = '" + password + "';";
            
            pst = conn.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                // out.println("<p align='center'>登录成功</p>");
                // 读取信息到 Session
                String Rid = rs.getString("id");
                String Rname = rs.getString("name"); 
                String Remail = rs.getString("email");
                String Rpassword = rs.getString("password");
                Date Rregister_time = rs.getTimestamp("register_time");
                Date Rlast_login_time = rs.getTimestamp("last_login_time");
                int RiconID = rs.getInt("iconID");
                // 设置 session
                session.setAttribute("adminID", Rid);
                session.setAttribute("adminName", URLEncoder.encode(Rname, "UTF-8"));    // 中文编码
                session.setAttribute("email", Remail);
                session.setAttribute("password", Rpassword);
                session.setAttribute("register_time", Rregister_time);
                // out.println("<p align='center'>register_time " + Rregister_time + "</p>");       // 输出到毫秒
                session.setAttribute("last_login_time", Rlast_login_time);
                session.setAttribute("iconID", RiconID);
                // 设置最大保持连接时间
                session.setMaxInactiveInterval(600);         // 600s
                // out.println("<p align='center'>设置session成功</p>");
                Date now = updateLogin(Rid);
                if(now != null){
                    // out.println("<p align='center'>最后一次登录信息更新成功</p></body></html>");
                    // session.setAttribute("last_login_time", now);        // now 是本次登录时间
                    request.setAttribute("searchContent", "201792179");
                    request.getRequestDispatcher("./Search").forward(request, response);
                }else{
                    // out.println("<p align='center'>最后一次登录信息更新失败</p></body></html>");
                    response.getWriter().println("<p align='center'>最后一次登录信息更新失败</p></body></html>");
                }
            }else{
                // out.println("<p align='center'>登录失败，未找到匹配信息</p></body></html>");
                response.getWriter().println("<p align='center'>登录失败，未找到匹配信息</p></body></html>");
                response.getWriter().println("<p align='center'>searchContent = " + request.getAttribute("searchContent") + "</p></body></html>");
            }
            rs.close();
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            response.getWriter().println("<p align='center'>" + se.getMessage() + "</p></body></html>");
            se.printStackTrace();
        } catch(NamingException e){
            // Class.forName() error
            response.getWriter().println("<p align='center'>" + e.getMessage() + "</p></body></html>");
            e.printStackTrace();
        } finally{
            // 最后关闭资源
            try{
                if(pst != null){
                    pst.close();
                }
            }catch(SQLException se2){
                // 不处理
            }
            try{
                if(conn != null){
                    conn.close();
                }
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
    
    }


    private Date updateLogin(String id){             // 更新最后一次登录时间
        // 设置日期输出的格式
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Connection conn = null;
        PreparedStatement pst = null;
        Date now = null;

        try{
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/ExamDB");
            conn = ds.getConnection();

            // 执行 sql 查询
            String sql;
            sql = "update administrators set last_login_time = ? where id = ?";
            pst = conn.prepareStatement(sql);
            now = new Date();
            pst.setString(1, df.format(now));
            pst.setString(2, id);
            pst.executeUpdate();
            
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            now = null;
            se.printStackTrace();
        } catch(NamingException e){
            // Class.forName() error
            now = null;
            e.printStackTrace();
        } finally{
            // 关闭资源
            try{
                if(pst != null){
                    pst.close();
                }
            } catch(SQLException se2){
                // 不处理
            }
            try{
                if(conn != null){
                    conn.close();
                }
            } catch(SQLException se){
                se.printStackTrace();
            }
            return now;
        }
    }

}

// a17411419150580