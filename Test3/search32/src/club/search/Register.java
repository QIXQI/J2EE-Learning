/**
 * servialVersionUID 了解
 */
package club.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

@WebServlet("/Register")
public class Register extends HttpServlet{
    private static final long serialVersionUID = 1L;

    // 构造函数
    public Register(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);

        Connection conn = null;
        PreparedStatement pst = null;

        // 设置时间格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 请求解决乱码
        request.setCharacterEncoding("utf-8");

        // 响应解决中文乱码
        response.setContentType("text/html; charset=utf-8");

        // 接收表单数据
        String adminID = request.getParameter("adminID");
        String adminName = request.getParameter("adminName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Date register_time = new Date();
        Date last_login_time = register_time;
        int iconID = 0;         // 假定默认为0

        PrintWriter out = response.getWriter();
        String title = "管理员注册处理页面";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body>\n" +
            "<h1 align='center'>" + title + "</h1>\n");

        try{
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/ExamDB");
            conn = ds.getConnection();

            String sql;
            // 插入数据
            sql = "insert into administrators(id, name, email, password, register_time, last_login_time, iconID) values (?, ?, ?, ?, ?, ?, ?);";
            pst = conn.prepareStatement(sql);
        
            // 传入参数
            pst.setString(1, adminID);
            pst.setString(2, adminName);
            pst.setString(3, email);
            pst.setString(4, password);
            pst.setString(5, df.format(register_time));
            pst.setString(6, df.format(last_login_time));
            pst.setInt(7, iconID);

            // 执行数据库更新操作，不需要 sql
            pst.executeUpdate();

            out.println("<p align='center'>注册成功</p>");
            // 获取信息到 Session
            session.setAttribute("adminID", adminID);
            session.setAttribute("adminName", URLEncoder.encode(adminName, "UTF-8"));    // 中文编码
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("register_time", register_time);
            // out.println("<p align='center'>register_time " + register_time + "</p>");       // 输出到毫秒
            session.setAttribute("last_login_time", last_login_time);
            session.setAttribute("iconID", iconID);
            // 设置最大保持连接时间
            session.setMaxInactiveInterval(600);         // 600s
            out.println("<p align='center'>设置session成功</p></body></html>");


            // 完成后关闭
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            out.println("<p align='center'>注册失败</p>");
            out.println("<p align='center'>" + se.toString() +  "</p>");
            out.println("<p align='center'>" + se.getMessage() + "</p></body></html>");
            se.printStackTrace();
        } catch(NamingException e){
            // Class.forNaem() error
            out.println("<p align='center'>注册失败</p>");
            out.println("<p align='center'>" + e.toString() +  "</p>");
            out.println("<p align='center'>" + e.getMessage() + "</p></body></html>");
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

}

// a17411419150580