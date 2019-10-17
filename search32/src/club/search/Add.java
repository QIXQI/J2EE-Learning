package club.search;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/Add")
public class Add extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String status = "failure";

        String ajaxId;
        String ajaxName;
        String ajaxPhone;
        String ajaxQQ;
        String ajaxEmail;
        if(request.getParameter("ajaxId") == null || request.getParameter("ajaxName") == null || request.getParameter("ajaxPhone") == null){
            out.println(status);
            return;
        }
        ajaxId = request.getParameter("ajaxId");
        ajaxName = request.getParameter("ajaxName");
        ajaxPhone = request.getParameter("ajaxPhone");
        ajaxQQ = request.getParameter("ajaxQQ");
        ajaxEmail = request.getParameter("ajaxEmail");
    
        // 处理 ajaxQQ, ajaxEmail 为空串的情况
        if(ajaxQQ != null && ajaxQQ.trim().equals("")){
            ajaxQQ = null;
        }
        if(ajaxEmail != null && ajaxEmail.trim().equals("")){
            ajaxEmail = null;
        }

        try{
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/ExamDB");
            Connection conn = ds.getConnection();

            String sql;
            PreparedStatement pst;
            if(ajaxQQ != null && ajaxEmail != null){
                sql = "insert into persons values (?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ajaxId);
                pst.setString(2, ajaxName);
                pst.setString(3, ajaxPhone);
                pst.setString(4, ajaxQQ);
                pst.setString(5, ajaxEmail);
            }else if(ajaxQQ != null && ajaxEmail == null){
                sql = "insert into persons(id, name, phone, qq) values (?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ajaxId);
                pst.setString(2, ajaxName);
                pst.setString(3, ajaxPhone);
                pst.setString(4, ajaxQQ);
            }else if(ajaxQQ == null && ajaxEmail != null){
                sql = "insert into persons(id, name, phone, email) values (?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ajaxId);
                pst.setString(2, ajaxName);
                pst.setString(3, ajaxPhone);
                pst.setString(4, ajaxEmail);
            }else{
                sql = "insert into persons(id, name, phone) values (?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ajaxId);
                pst.setString(2, ajaxName);
                pst.setString(3, ajaxPhone);
            }
            pst.executeUpdate();
            status = "success";
            // persons 数组重新获取数据
            DataManager.setPersons();
            pst.close();
            conn.close();
        } catch(NamingException ne){
            out.println("NamingException");
            ne.printStackTrace();
        } catch(SQLException se){
            out.println("SQLException: " + se.getMessage());
            se.printStackTrace();
        } finally{
            out.println(status);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }
}