// 进入增删改查页面之前身份验证
// 普通用户：  游客登录
// 管理员用户：密码登录
package club.search;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebFilter(dispatcherTypes = {
    DispatcherType.REQUEST
}, urlPatterns = ("/Search"))
public class AuthFilter implements Filter{
    // 由于是实现接口，所以接口中的抽象函数要全部实现

    private FilterConfig config;

    public void init(FilterConfig config) throws ServletException{
        this.config = config;
    }

    public void destroy(){
        // pass
    }


    // ServletRequest, ServletResponse 处理任何类型的协议
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException{
        // 转换为 http 请求
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(true);

        String adminID = (String)session.getAttribute("adminID");
        if(adminID == null || adminID.length() == 0){
            // session.setAttribute("lastRequestURL", request.getRequestURL().toString());
            session.setAttribute("lastRequestURL", request.getPathTranslated());

            RequestDispatcher rd = request.getRequestDispatcher("./login.html");
            rd.forward(request, response);
            return ;
        }

        chain.doFilter(request, response);
    }
}