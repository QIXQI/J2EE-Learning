/**
 * ajax 提交信息时，修改 id 有问题
 */
package club.search;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.annotation.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


@WebServlet("/Edit")
public class Edit extends HttpServlet{
    private static final long serialVersionUID = 1L;

    enum Persons {id, name, phone, qq, email;}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String status = "failure";

        String ajaxId;
        int ajaxIndex;
        String ajaxVal;
        if(request.getParameter("ajaxId") == null || request.getParameter("ajaxIndex") == null || request.getParameter("ajaxVal") == null){
            out.println(status);
            return;
        }
        ajaxId = request.getParameter("ajaxId");
        ajaxIndex = Integer.parseInt(request.getParameter("ajaxIndex"));
        ajaxVal = request.getParameter("ajaxVal");

        try{
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/ExamDB");
            Connection conn = ds.getConnection();

            String ajaxAttribute = Persons.values()[ajaxIndex].toString();
            String sql = "update persons set " + ajaxAttribute + " = ? where id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, ajaxVal);
            pst.setString(2, ajaxId);
            pst.executeUpdate();
            status = "success";
            // persons 重新获取数据
            DataManager.setPersons();
            pst.close();
            conn.close();
        }catch(NamingException ne){
            out.println("NamingException");
            ne.printStackTrace();
        }catch(SQLException se){
            out.println("SQLException: " + se.getMessage());
            se.printStackTrace();
        }finally{
            out.println(status);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }
}