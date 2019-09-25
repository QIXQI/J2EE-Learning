import java.io.*;
import java.text.SimpleDateFormat;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Get")
public class GetSession extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(true);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        if(session.getAttribute("search") != null){
            String search = URLDecoder.decode((String)session.getAttribute("search"), "UTF-8");
            // int page = Integer.parseInt( (String)session.getAttribute("page"));
            int page = (Integer)session.getAttribute("page");
            // int itemsPerPage = Integer.parseInt( (String)session.getAttribute("itemsPerPage") );
            int itemsPerPage = (Integer)session.getAttribute("itemsPerPage");
            out.println("<center>");
            out.println("<h2>" + search + "</h2>");
            out.println("<h2>" + page + "</h2>");
            out.println("<h2>" + itemsPerPage + "</h2>");
            out.println("</center>");
        }else{
            out.println("<center>没有找到session</center>");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }
}