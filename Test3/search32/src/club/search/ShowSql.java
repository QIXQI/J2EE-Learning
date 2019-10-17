/**
 * properties 配置文件应该放到 WEB-INF/classes/ 下
 */
package club.search;

import java.io.IOException;
import java.io.PrintWriter;
// import java.io.InputStream;
import java.sql.*;
// import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@WebServlet("/ShowSql")
public class ShowSql extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 读取 jdbc.properties
        // Properties props = new Properties();

        // response.getWriter().println("<p align='center'>path = " + getServletContext().getRealPath("jdbc.properties") + "</p>\n");
        // InputStream in = this.getClass().getResourceAsStream("jdbc.properties");
        // InputStream in = ShowSql.class.getResourceAsStream("jdbc.properties"); 
        
        // InputStream in = null;
        // in = this.getClass().getResourceAsStream("/conf/jdbc.properties");
        // props.load(in);
        // String driverClassName = props.getProperty("driverClassName");
        // String username = props.getProperty("username");
        // String password = props.getProperty("password");

        // 创建命名服务
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        try{
            Context cxt = new InitialContext();
            
            // 创建连接池
            DataSource ds = (DataSource) cxt.lookup("java:comp/env/jdbc/ExamDB");
            
            // 获取连接池中的连接
            Connection conn = ds.getConnection();
            
            String sql = "select id, name, phone, qq, email from persons";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int number = 0;
            while(rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String qq = rs.getString("qq");
                String email = rs.getString("email");
                out.println("<p align='center'>id = " + id + ", name = " + name + ", phone = " + phone + ", qq = " + qq + ", email = " + email + "</p>");
                number ++;
            }
            out.println("<p align=\"center\">共" + number + "条</p>\n");
            rs.close();
            pst.close(); 
            conn.close();
        }catch(NamingException ne){
            out.println("NamingException");
            ne.printStackTrace();
        }catch(SQLException se){
            out.println("SQLException");
            se.printStackTrace();
        }
    }

}