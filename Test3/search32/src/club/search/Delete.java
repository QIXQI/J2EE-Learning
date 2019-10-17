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


@WebServlet("/Delete")
public class Delete extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String status = "failure";
        String ajaxId;
        
        if(request.getParameter("ajaxId") == null){
            out.println(status);
            return;
        }
        ajaxId = request.getParameter("ajaxId");

        try{
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/ExamDB");
            Connection conn = ds.getConnection();

            String sql = "delete from persons where id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, ajaxId);
            pst.executeUpdate();
            status = "success";
            // 更新 persons 列表
            DataManager.setPersons();
            pst.close();
            conn.close();
        }catch(NamingException ne){
            out.println("NamingException");
            ne.printStackTrace();
        }catch(SQLException se){
            out.println("SQLException: " + se.getMessage());
        }finally{
            out.println(status);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }
}