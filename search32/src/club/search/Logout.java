package club.search;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Logout")
public class Logout extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public Logout(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException{
        HttpSession sessoin = request.getSession(true);

        response.setContentType("text/html; charset=utf-8");

        PrintWriter out = response.getWriter();
        String title = "退出账号";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType + 
            "<html lang='zh'\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body><h1 align='center'>" + title + "</h1>\n");
        if(sessoin.getAttribute("adminID") != null){
            sessoin.invalidate();
            out.println("<p align='center'>退出账号成功</p></body></html>");
        }else{
            out.println("<p align='center'>亲，你还没有登录呢</p></body></html>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException{
        doGet(request, response);
    }
}