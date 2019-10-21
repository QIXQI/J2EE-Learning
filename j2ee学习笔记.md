```java
/*
    1.使用过滤器进行登录验证
    2.实现任何协议 ServletRequest, ServletResponse
    3.pageContext包中的函数和属性一定要会
    4.（必考，大分值）PageContext, HttpServletRequest, HttpSession 等四个 总包含setAttribute 和 getAttribute 函数。
    5.static 静态，反对面向对象
    6.ServletContext 
    7.HttpSession
    8. attribute, context, config
    9.getServletConfig().getInitParameter("cnofig");
    10.org.apache.struts.actions.DispatchAction(源代码）
    11.考试题：两个代码性能分析对比（简单题）
    12.考试重点: Servlet, Listener, Filter, JSP, Tag, ServletConfig, ServletContext;
    13. 重重点： Servlet, ServletConfig
    14. 声明周期： getInitParameter()
    15. request.getParameter()
    16. 10月12日 周四 上传文件（反射机制），要考
    17. 选择题、填空题、问答题
    18. 听众接口（考试）listener 位置
    19. Servlet 读取初始化参数：ServletContext  context.getInitParaemeter(String);
    20. Servlet, ServletContext, ServletConfig, request, response
    21. 获取servlet组件的初始化值
    22. struts1.0  vs  struts2.0
    23. spring 容器, Ioc, DI选择题
    24. singleton vs prototype 必考（spring bean作用域）
*/

```



## 第二讲
url: 协议 域名 端口

url 包含: 协议 + 主机 + 文件路径

文件路径 = web应用的上下文路径 + 文件在当前web应用中路径

url 中❓ 后是查询字符串，名值对区分大小写

http://www.baidu.com/s?wd=hello&encoding=utf-8&page=15


后台语言: JAVA  C#  PHP Node.js


javax.servlet.Servlet       Servlet 中的函数必须会

javax.servlet.ServletRequest

javax.servlet.ServletResponse


javax.servlet.http.HttpServlet


javax.servlet.ServletConfig

javax.servlet.ServletContext


javax.servlet.http.HttpServletRequest

javax.servlet.http.HttpServletResponse


javax.servlet.http.Cookie

javax.servlet.http.HttpSession


javax.servlet.Filter


国标： GB2312   GBK    GB18030

UTF -->  UCF（四个字节 2^31）

编码推荐使用 utf-8

getParameter() 函数必考

Web 传文档之前：

    Content-Type: text/html
    
    Content-Length: 13708   文档长度，显示下载速度

smtp 

    -- 简单邮件传输协议，起初只支持7位 ascii码，现在支持8位数据传输

base64 编码

    -- 在24位缓冲区中，每6比特转换位一个 ascii 字符，空间大致变为原来的 4/3

swift 语言

    -- 用空学学

http 缓存原理（浏览器缓存）

[原理](https://www.cnblogs.com/chenqf/p/6386163.html)

http 响应代码

    -- https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
    -- 1**  信息响应
    -- 2**  成功响应
    -- 3**  重定向
    -- 4**  客户端响应
    -- 5**  服务端响应
    
Calendar (MIME)
    
    - text/plain 纯文本
    - text/html
    - text/xml   可扩展自标语言
    - text/json  传输数据
    
    - application/pdf
    - application/octet-stream  下载文件包
    - application/x-download    同上
    - Content-Disposition: filename=a.zip   自动保存文件名
   
   
   
***
## 第四讲

web.xml 中的session配置时间是分钟

    -- <session-config>
    --    <session-timeout>30</session-timeoiut>
    -- </session-config>
    
javax.servlet.http.Session 中的设置时间是秒

    -- getMaxInactiveInterval()
    

javax.servlet.http.Session 中的函数
    
    -- getAttribute(String name)
    -- getAttributeNames()
    -- getLastAccessedTime()    --> long
    -- getMaxInactiveInterval()
    -- invalidate()
    -- removeAttribute(String name)
    -- setAttribute(String name, Object value)
    -- setMaxInactiveInterval(int interval)
    
Web 应用中自定义的类最后支持序列化

```java
pubilc class Cart implements java.io.Serializable{
    // 没有方法，仅用于 标示
}
```
可序列化类的所有子类型都是可序列化的

使用 session 的三种手段

    -- Cookie(客户端)
    -- url 重写
    -- 隐藏域
    
没有Cookie，就没有Session

#### RequestDispatcher

forward 在 response uncommited 前使用，只处理头信息，不能包含响应信息

forward 前后都不能处理 response，转发给新页面处理

include 中的编码可以被 /servlet/b 中的编码覆盖
```java
RequestDispatcher rd = request.getRequestDispatcher("/servlet/b");

//          /servlet 前的/ 是当前web应用中的根，不一定是服务器的根

request.setAttribute("data", ...);

rd.forward(request, reponse);       // 在新页面获取rquest 中的信息

response.rd.include(request, response)      // 后续代码继续处理
```






    








***
List, Set, Map

ServletConfig   ServletContext

Cookie

分布式内存

Maven 安装 jar包





***

## 第5讲
url = http://localhost:8080/j2ee1/servlet/a?abc=123&a=456

String contextPath = request.getContextPath();  上下文地址

    --> 结果不唯一
    --> servlet url为/servlet/a, 则返回 "/j2ee1"
    --> 部署在根下，返回 ""
    

get 方式提交数据通常是检索，改变数据可以用post


get 方式处理 和 post 方式处理(编码问题)


```html
<!-- 表单action的地址服务器解析，所以要相对服务器的根 -->
<form action="/j2ee1/servlet/a" method="post"
    <input type="text" name="wd" />
    <input type="submit" value="search" />
</form>
```

#### filter
静态网页和动态网页都可以过滤

filter 对于Servlet中的request 和 response 都是同一个对象（内存地址一样）

过滤器的功能：

    --> 1.认证(下载过滤，要登录时，下载的链接放到 request.getSession() 中)
    --> 2.日志与审计
    --> 3.图像转换
    --> 4.压缩
    
```java
public void doGet(request, response){
    long start = System.currentMillis();
    ...
    long end = System.currentMillis();
    getServletContext().log("cost: " + (end-start));
}
```


Filter 接口
    --> init(FilterConfig filterConfig)
    --> doFilter(request, response, FilterChain chain)


FilterChain
    
    --> doFilter(request, response)  参数没有过滤链了
    
```java
// filter

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MyFilter implements Filter{
    private FilterConfig config;

    public void destroy(){}
    
    public void init(FilterConfig config) throws       ServletExcepion{
        this.config = config;
    }
    
    public void doFilter(ServletRequest req, ServletResponse response res, FilterChain chain) throws ServletException, IOException{
        // 处理 request
        
        chain.doFilter(req, res);
        
        // 处理 response
        
    }
}


```

#### filter 处理请求乱码
```java
public class EncodingFilter implements Filter{
    public void doFilter(request, response, FilterChain chain) throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        
        // 千万别忘了
        chain.doFilter(request, response)
    }
    
}
```

#### filter 组件部署
web.xml配置：过滤器的过滤顺序与在web.xml 中filter-mapping 出现的顺序对应

注解配置：过滤器顺序按照类名的字符串比较规则比较，较小的先执行

```xml
<!-- 一个sevlet 可以分配很多 servlet-mapping ，也就是有多个 url 地址 -->
<!-- filter 也可以有多个 filter-mapping -->
<web-app>
    <filter>
        <filter-name></filter-name>
        </filter-class></filter-class>
        <!-- 初始化参数，名值对存在 -->
        <init-param>
            <param-name>a</param-name>
            <param-value>abc</param-value>
        </init-param>
    </filter>
    
    
    
    <filter-mapping>
        <filter-name></filter-name>
        <!-- 过滤 /servert 下的所有网页 -->
        <!-- *.jpg 可以过滤所有jpg文件-->
        <url-pattern>/servlet/*</url-pattern>
    </filter-mapping>
    
    
    <filter-mapping>
        <filter-name>f1</filter-name>
        <!-- 只过滤指定servlet -->
        <servlet-name>s1</servlet-name>
        <!-- 用户输入的拦截，容器内派发来的不拦截，默认是这样的，派发的不拦截 -->
        <dispatcher>REQUEST</dispatcher>
        <!-- 拦截派发中的 forward(request, response) -->
        <dispatcher>FORWARD</dispatcher>
        <!-- 拦截派发中的 include(request, response) -->
        <dispatcher>INCLUDE</dispatcher>
        <!-- 拦截出错的信息 -->
        <dispatcher>ERROR</dispatcher>
    </filter-mapping>
</web-app>
```

#### 过滤器如何写
```java
public class AuthFilter implements Filter{

    // ServletRequest, ServletResponse 处理任何协议
    // HttpServletRequest, HttpServletResponse 处理HTTP协议
    
    pubic void doFilter(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        HttpSession session = request.getSession();
        
        String username = (String) session.getAttribute("username");
        if(username == null || username.length() == 0){
            request.getRequestDispatcher("/login.html");
            forward(request, response);
            return;
        }
        
        chain.doFilter(request, respones);
        
    }
}
```

#### 过滤器注解写法
多多少少 少一点，haha
```java
@WebFilter(dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.FORWARD,
        DispatcherType.INCLUDE,
        DispatcherType.ERROR
    },
    urlPatterns = ("/TimeFilter"))
public class TimeFilter implements Filter{
    
}
```

#### 过滤器示例1
```java
@WebFilter(value = "/*",dispatcherTypes = {DispatcherType.FORWARD,DispatcherType.REQUEST})
...
System.out.println("filter---in");
filterChain.doFilter(servletRequest, servletResponse);
System.out.println("filter---out");
...

// ----------------------  //

@WebServlet("/forward")
...
System.out.println("/forward");
request.getRequestDispatcher("/index.jsp").forward(request, response);
...

// ------------ //
inex.jsp
...
<% System.out.println("index.jsp"); %>
...
```

执行结果

```java
filter---in   // 第一次拦截器入栈
/forward      // ...
filter---in   // 第二次拦截器入栈
index.jsp     // ...
filter---out  // 第二次拦截器出栈
filter---out  // 第一次拦截器出栈
```

#### 过滤器示例2
```java
@WebFilter(value = "/*",dispatcherTypes = {DispatcherType.FORWARD,DispatcherType.REQUEST})
public class FilterImplA implements Filter {
...
System.out.println("A---in");
filterChain.doFilter(servletRequest, servletResponse);
System.out.println("A---out");
...

// ----------------------  //

@WebFilter(value = "/*",dispatcherTypes = {DispatcherType.FORWARD,DispatcherType.REQUEST})
public class FilterImplB implements Filter {
...
System.out.println("B---in");
filterChain.doFilter(servletRequest, servletResponse);
System.out.println("B---out");
...

// ----------------------  //

@WebServlet("/forward")
...
System.out.println("/forward");
request.getRequestDispatcher("/index.jsp").forward(request, response);
...

// ------------ //
inex.jsp
...
<% System.out.println("index.jsp"); %>
```

结果

```java
A---in    // A1入栈
B---in    // B1入栈
/forward
A---in    // A2入栈
B---in    // B2入栈
index.jsp
B---out   // B2出栈
A---out   // A2出栈
B---out   // B1出栈
A---out   // A1出栈
```

#### 过滤器登录验证实例
```java
// 将req进行强转
HttpServletRequest request = (HttpServletRequest) req;

// 设置编码
request.setCharacterEncoding("utf-8");
resp.setContentType("text/html;charset=utf-8");

// 获取uri
String uri = request.getRequestURI();

// 判断uri
if(request.getSession().getAttribute("login") != null ||
        uri.contains("/login") ||
        uri.contains("/loginVerifyCodeServlet") ||
        uri.contains("/index.jsp") ||
        uri.contains("/css/") ||
        uri.contains("/js/") ||
        uri.contains("/img/") ||
        uri.contains("/fonts/")
) {
    chain.doFilter(req, resp);
} else {
    ((HttpServletResponse)resp).setStatus(308);
    request.getRequestDispatcher("/index.jsp").forward(req, resp);
}
```


#### 登录Servlet

```java
public class LoginServlet extends HttpServlet{

    // 密码一般使用 post 方法传数据

    public void doPost(HttpServletRequest request, HttpServletResponse) throws ServletExceptoin, IOException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 明文散列：MessageDigest, MD5, SHA1, SHA256
        
        request.getSession().setAttribute("username", username);
    }
}

```



#### tomcat 提交数据出现乱码
"中国梦"

post

    --> post 方式不出错, 在body中，服务器不处理
    --> request.getParameter()

get
    
    --> get 方式出错，在header中
    --> 数据包含在 url 中，转换为ascii码，效率低，传输数据少
    --> 一个中文转两个字节(utf-8)
    --> 服务器首先接收到 url 地址，tomcat用iso8859-1转码（一个字节一个字节的转码）
    
```java
// 对get请求有效，post应该也有效吧
String wd = request.getParameter("wd");
wd = new String(wd.getBytes("ISO=8859-1"), "UTF-8")

// 方式一，每次都写
// 只对post请求有效，对get请求无效
// 在setCharacterEncoding 之前，不能执行request.getParameter(), 否则，setCharacterEncoding 无效
request.setCharacterEncoding("utf-8");

// 方式二，Filter

// 对get请求有效
// 方式三，在 /config/servlet.xml 中的 Connector 组件中添加
    URLEncoding="UTF-8"
```

response.setCharacterEncoding() vs response.setContentType()

    --> 一般使用response.setContentType()方法来设置HTTP 响应的编码，同时指定了浏览器显示的编码；
    --> 使用此方法，会自动调用response.setCharacterEncoding()方法来通知浏览器以指定编码来解;
    --> 使用此方法要在response.getWriter()执行之前或response提交之前；

[get, post乱码请求链接](https://blog.csdn.net/xiazdong/article/details/7217022/)

#### ServletConfig 读取 web.xml 中的初始化数据
```java
// javax.servlet.*


// javax.servlet.http.* 中已经重写好了
String value = getInitParameter("servlet");
```

#### init 
```java
// 第一种，重载时不需要调用 super.init(config)
public void init() throws ServletException
// 最终被 GenericServlet.init(ServletConfig config)调用
// 但是 config仍然可以由getServletConfig() 获取

// 第二种
public void init(ServletConfig config) throws ServletException
// 重载时需要调用 super.init(config)
// 实现在 Servlet.init(javax.Servlet.ServletConfig)


// 综上，推荐使用 init()

```




***
如何获取 request 的ip 等信息
    
    --> uri = request.getRequestURI();
    --> URI = Universal Resource Identifier 统一资源标志符
    --> URL = Universal Resource Locator 统一资源定位符
    --> URN = Universal Resource Name 统一资源名称
    --> URI 包含 URL + URN
    --> 但一般区别不大，技术多用URI
    --> A URI 可以进一步被分为定位符、名字或两者都是. 术语“Uniform Resource Locator” (URL) 是URI的子集, 除了确定一个资源,还提供一种定位该资源的主要访问机制(如其网络“位置”);

明文散列：MessageDigest, MD5, SHA1, SHA256

实现关键字过滤

    --> 关键字替代为 ***
    
    
***
第一次上机作业：
        
    --> servlet
    --> jsp
    --> jquery, ajax
    --> redius
    --> 加载资源文件使用相对地址
    
    
    
## jsp
类库：
    
    --> javax.servlet.jsp

在服务器运行，输出html到客户端

可以包含动态的js代码，动态的css代码

jsp 文件可以部署，可以声明配置文件

第一次访问jsp 文件慢，第二次就快了，（刷新后又慢了），有以下过程：

    time.jsp    -->   time_jsp.java     -->     time_jsp.class
    

    
#### jsp 指令
```jsp
// page 指令
// session 默认是true，可以不写
// 必考：isErrorPage, 默认是false，只有在错误处理页面 /error.jsp 中才是 true
<%@ page session="true"  %>
<%@ page import="java.util.*, java.io.*"
         // contentType="text/html; charset=utf-8" // 不推荐 contentType, 因为肯定是 html，推荐pageEncoding
         pageEncoding="utf-8"
         language="java"
         isErrorPage="true" errorPage="/error.jsp"
         // buffer="none"   不推荐none，默认8KB
%>
```


```jsp
// include 指令，输出其他文件的内容
<$@ include file="a.html" %>
```

```jsp
// 自定义标记，这个指令必考
<%@ taglib uri="/WEB-INF/a.tld" prefix="t" %>
```


#### jsp 动作
```jsp
<jsp:include page="/servlet/a"></jsp:include>
// 或者
<jsp:include page="/servlet/a" />
```

// 重点重点重点重点重点重点重点重点重点重点重点重点（局部变量创建）
```jsp
<jsp:useBean id="a" class="com.abc.Student"
    scope="page|request|session|application" />
// page，作用域只在当前页面
// request，每次new一个对象
// session，作用域session，先访问session，没有新建，写入session
// application，共享数据




// 相当于 com.abc.Student  a
// 如果没有，则创建
```

// 获取age
```jsp
<jsp:getProperty name="a" property="age" />
// 这个name 与 id 一样
```

// 重设age
```jsp
<jsp:setProperty name="a" property="age" value="20" />
// 或
// 从客户端获取 age，写入变量age， 似乎不需要考虑null
<jsp:setProperty name="a" property="age" param="age" />
```

// 匹配
```jsp
<jsp:setProperty name="a" property="*" />   // 根据property和param相同值自动匹配
class Student{
    private age, name, height;
    public void setAge(int age){this.age = age;}
    public int getAge(){return age;}
}
// 易错 javaBean 首字母全小写
<form>
    <input type="text" name="PageSize" />   // 因为首字母大写，自动匹配时出错,所以要
    // <jsp:setProperty name="a" property="pageSize" param="PageSize" />
</form>

```

#### jsp 
表达式
```jsp
<%= 5 + 6 %>        // 表达式，不能有分号
<%= new String() %>
```
声明
```jsp
<%! int i; %>       // 一般不声明成员变量，多线程并发危险
<%! function() %>   // 一般声明函数
```

注释
```jsp
<!-- -->
```

##### include动作 vs include指令
include指令(转换时) 只能包含静态文本文件(html, txt)

include动作(运行时) 既能包含静态文件，又能包含动作文件(/servlet/a)



#### 提交参数空串与null
```java
String pageSize = request.getParameter("pageSize");

// http://localhost:8080/a/abc.jsp?pageSize=&page=1
// 结果是空串

// http://localhost:8080/a/abc.jsp?pagesize=123
// 返回null

// 解决
if(pageSize == null){
    pageSize = "";
}
```
    
#### jsp 输出信息
```jsp
// 方法一：嵌套
now : <%= new java.util.Date() %>

// 方法二：引入java代码
<%
    response.getWriter().println("now: ");
    response.getWriter(java.util.Date());
%>

```

#### jsp 变量(必考)
```jsp
HttpServletRequest  request
HttpServletResponse response
HttpSession session
ServletConfig config
ServletContext application
**JspWriter out**
**PageContext pageContext**     // 页面上下文   获取request: pageContext.getRequest();
page this
Exception exception     // 正常页面中不存在，只在错误处理页面中存在这个对象
```

#### JspWriter vs PrintWriter
JspWriter 抛出IOException

JspWriter 流缓存满了，再到 response.getWriter()，默认8KB
```jsp
<%@ page buffer="none" %>   // 没有缓存
```


#### 设计模式
常用的大约20种

fascade 门面模式（要求）

adapter 适配器模式（要求）

mvc 设计模式（模型，，适配器）

mui 设计模式


#### pageContext(重点)
```jsp
<%
// 法一
request.getRequestDispatcher("/b.jsp").forward(request, response);

// 法二
pageContext.forward("b.jsp");
%>
```


***
标记编程

el表达式语言


## 自定义标记
将 jsp 中的 java 代码分离出

自定义标记包：javax.servlet.jsp.tagext.*;

pageContext 包中的东西一定要会

#### 分页程序
```java
// PageBean 
// view
//      --> s.jsp
//      --> <jsp:useBean id="page" class="PageBean" scope="session" />
//          --> 这里scope 的值是必考的
//      --> <jsp:setProperty name="page" property="*" />

// jsp 就是展示的，最好不要有业务逻辑



public class PageBean{
    private list data;
    private int pageSize;
    private int currentPage;
    
    public void setData(List data){
        this.data = data;
    }
    
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    
    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }
    
    public list getCurrentPageData(){
        int start = pageSize * (currentPage - 1);
        int end = start + pageSize;
        if(end > data.size()){
            end = data.size();
        }
        return data.subList(start, end);
    }
    
    public int getTotalPages(){
        
    }
    
    public int getNextPage(){
        return ...;
    }
}


```


#### 实例
```jsp
<% numguess.reset(); %>
<t: reset />

now: <%= new java.util.Date() %>
<t:time>

</t:time>
```

#### javax.servlet.jsp.tagext
文档：
```
// 初始化
void setPageContext(PageContext pc);

// 释放（不被销毁），现在又变成销毁的了
void release();

// 起始标记
int doStartTag() throws JspException

// 结束标记
int doEndTag() throws JspException

// 父亲标记
Tag getParent()
void setParent(Tag t)
```

实例
```java
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import javax.servlet.jsp.tagext.*;

public class TimeTag extends TagSupport{
    public int doStartTag() throws JspException{
        // out.println 抛出异常
        // 但是不能异常声明，因为是继承的 TagSupport, 原函数就是 throws JspException
        try{
            // Fascade 设计模式
            JspWriter out = pageContext.getOut();
            out.println(new java.util.Date());
        }catch(IOException e){
            
        }
        
        // return SKIP_BODY;
        // return EVAL_BODY_INCLUDE;
        // return SKIP_PAGE;
        return EVAL_PAGE;
    }
}


```

返回值:
    
    --> return SKIP_BODY;
    --> return EVAL_BODY_BUFFERED;
    --> return EVAL_BODY_INCLUDE;
    
    --> return EVAL_PAGE;
    --> return SKIP_PAGE;
    
#### 将数据写到 request, session
```java
request: context...get...setAttribute()
session: 
```

#### 新版本 tag（tag handler标记处理程序）
```java
now: <t:time />

public class TimeTag extends SimpleTagSupport{
    // SimpleTagSupport 下又 getJspContext(), setJspContext()

    public void doTag() throws JspException, IOException{
        // pageContext 变成私有，要读写函数才能访问
        // 下面一行错误，getPageContext 处理 http 协议
        // getPageContext().getOut().println(new java.util.Date());
        // getJspContext() 与协议无关
        ((PageContext) getJspContext()).getOut().println(new java.util.Date();
    }
}
```

#### 实例 <t:reset>
实现 <% numguess.reset(); %> 的标记

```java
import num.NumberGuessBean;

public class ResetTag extends SimpleTagSupport{
    public void doTag() throws JspException, IOException{
        PageContext pc = (PageContext) getJspContext();
        
        // pc.getSession();
        // pc.getOut();
        // pc.getAttribute();
        // ...
        
        NumberGuessBean numguess = (NumberGuessBean) pc.getSession().getAttribute("num");
        numguess.reset();
    
    }
}

```

部署 /WEB-INF/a.tld
```xml
<tag-lib>
    
    <!-- jar 包，不能在taglib 指令中访问，在这设计，taglib访问 -->
    <uri>http://www.abc.com/ns/javaee</uri>


    <tag>
        <name>reset</name>
        <tag-class>ResetTag</tag-class>
        
        <!-- <body-content>empty</body-content> 内容设为空，不能有值 -->
        
        <!-- <body-content>script</body-content> -->
        
        <!-- 默认jsp -->
        <body-content>JSP</body-content> 
    </tag>
    
</tag-lib>

```

```jsp
// 命名空间
<t:author xmlns="www.zhengxiang4056.club"   // 默认命名空间
          xmlns:t="http://www.abc.com/ns/book"
          xmlns:p="wwwwwww.xyz.com"
          xmlns:p2="www.qixqi.club/iqiq">
// 这个 http://www.abc.com/ns/book 是uri, 不是url, 不是超链接，只要全世界互联网唯一就行，只是命名，随便写，不一定是url 
// 这样 author实际上就是 http://www.abc.com/ns/book/author


<% numguess.reset(); %>
<%@ taglib uri="http://www.abc.com/ns/javaee" prefix="t" %>

```

#### 流程
1. tag handle
```java
public class ResetTag extends SimpleTagSupport{
    public void doTag() throws JspException, IOException{
        PageContext pc = (PageContext) getJspContext();
        
        // pc.getSession();
        // pc.getOut();
        // pc.getAttribute();
        // ...
        
        NumberGuessBean numguess = (NumberGuessBean) pc.getSession().getAttribute("num");
        numguess.reset();
    
    }
}

```

2 .tld
```xml
/WEB/page.tld
<tag-lib>
    <uri>mypage</uri>
    
    <tag>
        <name>page</name>
        <tag-class>PageTag</tag-class>
        <body-content>JSP</body-content>
        <attribute>
            <name>data</name>
            <type>java.util.list</type>
            <required>true</required> <!-- true必须提供 -->
            <!-- run time express value -->
            <!-- 默认true, true支持动态表达式 -->
            <rtexprvalue>true</rtexprvalue>
        </attribute>
    </tag>
</tag-lib>
```

3 .jsp
```jsp
<%@ taglib uri="/WEB-INF/page.tld" prefix="x" %>

<% 
<x:page data=<%= list %> >
```

## 第三次作业
增删改查放到Service类中
#### 连接池
包：javax.sql.DataSource

```java
import java.sql.*;

//  Connection
//  Statement   不建议使用，不安全，慢
// PreparedStatement    建议使用

// 这一行如果在 service 中，连接过多，大大降低资源
// 这一行需要建立连接，很慢
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/test",
    "java",
    "java1234"
);

Driver driverClassName = null;
Class.forName(driverClassName);

Statement stmt = conn.createStatement("select ...");
ResultSet rs = stmt.executeQuery("select ...");

PreparedStatement

```


读取配置文件
```properties
<!-- jdbc.properties -->
driverClassName = com.mysql.jdbc.Driver
username = java
password = java1234
```

```java
// 读取文件
main(){
    Properties p = new Properties();
    // 不推荐下一行的方法
    // InputStream in = new FileInputStream("jdbc.properties");
    
    // 推荐使用
    this.getClass().getResourceAsStream("jdbc.properties");
    
    // 加载
    p.load(in);
    
    // 读取属性
    String driverClassName = p.getProperty("driverClassName");
    
    Class.forName(driverClassName);
    
}

```

连接池
/META-INF/context.xml
```xml
<Context docBase = "" path="">
    <Resource name="jdbc/ExamDB" auth="Container" type="javax.sql.DataSource" maxTotal="100" maxIdle="30" maxWaitMillis="1" usernmae="javauser" password="javadude" driver="" url="jdbc:mysql://localhost:3306/javatest" />
</Context>
```

```
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;


// 创建命名服务
// javax.naming.Context cxt = new InitialContext();
Context cxt = new InitialContext();


// 创建连接池
// java:comp/env 前缀，进程内的，辟邪的
// java:comp/env 分布式，进程外的，不写前缀  ctx.lookup("jdbc/ExamDB");
DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ExamDB");


// apache common dbcp
DataSource ds = BasicDataSource();
// 法一：静态，最好不用
public DBUtils{
    public static
}
// 法二：存储在 servletContext











// 获取连接池中的连接
Connection conn = ds.getConnection();

Driver driverClassName = null;
Class.forName(driverClassName);

Statement stmt = conn.createStatement("select ...");
ResultSet rs = stmt.executeQuery("select ...");


con.close();        // 释放连接
```


#### 命名服务
包: javax.naming.Context
    
    --> lookfor 函数，返回object，需要强制转换
    --> bind 函数，绑定
    --> reBind 函数，重新绑定

商业包：javax.naming.NamingContext

```java
javax.naming.Context cxt = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ExamDB");

Cart cart = new Cart();
ctx.bind("/ABC/XYZ/mycart", cart);

// 法一
Context c1 = (Context) ctx.lookup("/ABC");
Context c2 = (Context) c1.lookup("/XYZ");
Cart cart = (Cart) c2.lookup("mycart");
// 法二
Cart cart = (Cart) ctx.lookup("/ABC/XYZ/mycart");

```

***
## struts 框架
*org.apache.struts.actions.DispatchAction(源代码重点)*

*DispatchAction处理函数映射的机制与流程（eg计算器），问答题，详细叙述*
    
    --> 类似与 Servlet 中的 service 根据 method 分发为 doGet, doPost

解决控制器类的复用性 Controller 类，降低类的粒度，提高类的可复用性

为什么有复用性问题：modules中

基于 mvc 结构的框架

a rapid framework for web development based on JavaEE

请求经过：前端控制器、action、后端控制器、module

org.apache.struts.action
    Action 类

前端控制器 org.apache.struts.action.ActionServlet

前端控制器的流程、职责（考试）

后端控制器是单实例的（考试）

前端控制器处理输入数据，封装成 Beans 对象

    --> ActionServlet 前端控制器
    --> struts框架主要实现 ActionForward函数
    --> public ActionForward execute Action
    
```xml
<web-app>
    <display-name>Struts Blank Application</display-name>
    
    <servlet>
        <servlet-name>action</servlet-name>
        <servlet-class>org.apache.struts.action.ActionServlet</servlet-class>
        <init-param>
            <param-name>config</param-name>
            
            <!-- 考试题：sturts 项目的配置文件不一定是 struts-config.xml , 可以修改-->
            <param-value>/WEB-INF/struts-config.xml</param-value>
            <!-- 获取config, getServletConfig().getInitParameter("config"); -->
        </init-param>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>action</servlet-name>
        <url-pattern>*.do</url-pattern>
        <!-- <url-pattern>/do/delete</url-pattern> -->
    </servlet-mapping>
</web-app>

```

action
```xml
<action path="/Input" type="org.apache."
```





***
jndi 
使用 struts 框架的action 做计算器

Java 反射机制：Class, Method

calc.add(5, 6)

String method = request.getParameter("method");

Method m = calc.getClass().getMethod(method);

m.invoke(m, 5, 6);

***
## struts 再讲
controller 控制软件运行的流程，映射成 mapping

考试：如何导入一个 struts 工程
#### 建立基于 struts 的web程序
1. 项目结构
```xml
c:\abc\s1
    WEB-INF
        classes
        lib
        web.xml
```

2. 导入 struts
```xml
import struts 1, www.apache.org 
    
    unzip, apps, docs, lib,
    
    copy jars to WEB-INF/lib
```

3. 编辑 web.xml, deploy ActionServlet(Front Controller)
```xml
<!-- web.xml -->
<web-app>
    <servlet>
        <servlet-name>s</servlet-name>
        <!-- 类名考过，选择题 -->
        <!-- 命名规范：包名小写，类名大写 -->
        <servlet-class>org.apache.struts.action.ActionServlet</servlet-class>
        <init-param>
            <!-- 配置信息 -->
            <!-- struts 配置主要是关于后端控制器的 -->
            <!-- 读取的信息放到 Map中，key 值是String, 可以根据url地址获取key值 -->
            <param-name>config</param-name>
            <!-- struts 配置文件名可以修改 -->
            <param-value>/WEB-INF/struts-config.xml</param-value>
        </init-param>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>s</servlet-name>
        <!-- 方式一：<url-pattern>/do/*</url-pattern> -->
        <!-- 方式二：使用后缀识别，.do文件类型访问前端控制器 -->
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
</web-app>

```

前端控制器类如何加载后端控制器类

```java
ActionServlet{
    public void service(request, response){
        // 方法一，使用 Action
        Action a = (Action) ...;
        a.execute(...);
        
        // 方法二：不使用 Action, 使用反射机制
        Object o = Beans.instantiate(){
            getClass().getClassLoader(),
            className       // className在配置文件中
        };
        Method method = o.getClass().getMethod(methodName);
        method.invoke(a, ...);
        
        // 区别
        // 方法一：快，眼中依赖 Action，耦合性强(struts1)
        // 方法二：耦合性弱
    }
}
```

3. 写 Action
```java
Action,
    
// Struts 后端控制器的根类：Action类
// request 提交处理不了的请求
public class AddAction extends Action{
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
        CalcForm form = (CalcForm) form;
        int i = Integer.parseInt(form.getV1());     // 输入数据
        int j = Integer.parseInt(form.getV2());
        
        Calc c = new Calc();
        int result = c.add(i, j);
        form.setResult("" + result);
        
        ActionForward r = mapping.findForward("result");
        return r;
    }
}
```

struts-config.xml
```xml
<struct-config>

    <!-- 不考 -->
    <formBean>
    </formBean>


<action-mapping type="">

    <action path="/welcome" forward="/welcome.jsp">
        <set-property property="example" value="" /> 
    </action>

    <!-- add.do 已经在前端控制器类中筛选 -->
    <action type="AddAction" path="/add" name="calcForm">
        <!-- 输出页面 -->
        <forward name="result" path="result.jsp" />
    </action>
</action-mapping>
</struct-config>
```

CalcForm 类
```java
public class CalcForm extends ActionForm{
    // 最好使用 String, 用 int 转换时可能出现前端异常
    // 数据处理最好不要让前端处理，逻辑放到后端
    private String v1, v2, result;  
    
    public String getV1(){
        return v1;
    }
    
    public void setV1(String v1){
        this.v1 = v1;
    }
    // 提高 public String getV1() 复用性
}
```

提高 public String getV1() 复用性
```java
public class DynaActioinForm extends ActionForm{
    private Map<String, Object> data; 
    
    public Object get(String name){
        return data.get(name);
    }
    
    public void set(String v1, Object value){
        map.put(v1, value);
    }
}
```


#### 降低后端控制器的action
*org.apache.struts.actions.DispatchAction(源代码重点)*

群里代码

DispatchAction



***
maven

类: Beans.instantiate();

java 的反射机制



*** 
## struts 后讲
java 资源文件是 properties
    
    --> java.util.ResourceBundle 管理资源包
    --> java.util.ResourceBundle("s4", new Locale("zh", "CN"));
    --> 国际化，多语言显示时，可以写入不同的资源文件，根据地址选择不同的资源文件

代码复用
```java
// 工具类，业务逻辑
// 继承
// AOP 编程：在两段代码中，只有一行代码，前后代码复用，可以使用AOP 编程

```

html 格式

    -->物理格式： 只有一个根元素
    -->逻辑格式： 应用逻辑决定存在的元素是否合法 (dtd, xsd<类型丰富>文件检查逻辑格式)
    
    
#### struts1 和 struts2 的区别(选择题)，网上找一下
```java
// struts1 Action 函数中有HttpServletRequest, HttpServletResponse
// struts2 中没有
// struts1 中 ActionForward r = mapping.findForward("result");   return r; 
// struts2 中 直接返回 return "result"; Action 函数 参数没有 ActionMapping map
// struts2 将ActionForm 与 Action 合并在一起
// struts2 缺点：多实例， struts1 是单实例的
```


#### struts2 的配置文件
```xml
<action type="s4.CalcAction" path="calc" method="add" />


```



*** 
mvc 设计模式

struts commmand命令

jiveforum 软件，汉化过程就是重写资源包

native2ascii 软件，转换文本文件的编码

***

## Spring 框架
核心概念：Ioc, AOP

容器，两个对象之间的依赖关系转到对象与容器的依赖关系
#### Ioc（必考）
控制反转  -->  松耦合

DI（必考）: 依赖注入，即组件之间的依赖关系由容器在运行期决定，形象地说，即由容器动态地将某种依赖关系注入到组件之中。

#### 初始化函数与构造函数的区别
构造函数创建类的过程中消耗比较多的内存

初始化可多次调用

#### 新建Spring 工程
org.springframework.context   
    --> ApplicationContext
    --> 封装了资源文件

```java
spring framework
JavaEE platform

A a = new A();

// 类加载器 getClass().getClassLoader()
// java 根类， java.lang.Object
// java 源类， java.lang.Class，虚拟机加载几个类，内存中就有几个Class类
Object java.beans.Beans.instantiate(ClassLoader c1, String className);

A a = new A();
Object o = Beans.instantiate(getClass().getClassLoader(), "A")  //运行时创建对象，返回Object

BeanFactory 接口

考试：ApplicationContext 接口
      FileSystemXmlApplicationContext  类，构造函数，读取xml文件参数，构造 Bean 组件，启动比较慢，可以延迟，没必要，支持多个配置文件，（文件系统）可以使用绝对路径和相对路径，new创建，不可以打包到jar
      ClassPathXmlApplicationContext 类，比FileSystem推荐使用, 可以归档到jar包，使用ClassLoader.instantiate 创建

```
创建spring 应该放到 listener 中
```xml
<listener
```

听众接口 servelt.context.listener {servlet init,servlet destory}

ContextLoaderListener implements ServletContextListener

读取配置文件中的 listenerlocation
```java
public class ContextLoaderListener implements ServletContextListener{
    public void contextInitialized(ServletContextEvent sce){
        ServletContext cxt = sce.getServletContext();
        
        // 获取 xml 配置文件中的内容
        String xmlFile = context.getInitParameter("contextConfigLocation");
        
        XmlWebApplicationContext ac = new XmlWebApplicationContext;
    }
}
```

*声明 listener 和 读取初始化参数*

#### 读取servlet 初始化参数
```xml
<web-app>
    <servlet>
        <servlet-name>a</servlet-name>
        <servlet-class>a.AServlet</servlet-class>
        <init-param>
            <param-name>servlet</param-name>
            <param-value>www.baidu.com</param-value>
        </init-param>   
    </servlet>
</web-app>

```

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <!-- *表示可以多配置文件 -->
    <param-value>classpath*:/resources/spring/*applicationContext.xml</param-value>
</context-param>
```

```java
doGet(){
    String value = getInitParameter("servlet");
    
    // 或者
    // HttpServlet 实现了
    // 读取Servlet的配置参数
    ServletContext/ServletConfig接口
    String value = getServletConfig().getInitParameter("servlet");
    
    // 读取整个应用参数
    getServletContext().getInitParameter("contextConfigLocation");
}
```

#### AOP
Spring 文档

    --> 切面（不是面向对象）
    --> 连接点 
    --> 切点（集）
    --> 增强（advance, 执行的关键函数）



***
简单工厂模式、抽象工厂模式、抽象工厂方法


***
## Spring 再讲
#### 理解FileSystemXmlApplicationContext & ClassPathXmlApplication
考试：FileSystemXmlApplicationContext 与 ClassPathXmlApplication 加载文件的区别：

new FileSystemXmlApplicationContext("d://beans.xml");

new FileSystemXmlApplicationContext("beans.xml");   先读配置文件，需要明确文件在哪

ClassPathXmlApplication 把文件放到Class包中就行，推荐使用，beans.xml 和 class文件放到一起


#### 理解 XmlWebApp
new(启动Web程序，new对象) --> ServletContext 对象 --> ServletContextEvent --> ServletListener(定义的事件处理函数：contextInitialized, contextDestroyed) 

servletconfig 是加载组件后才生成的

一个 Web 程序就有一个 ServletContext(不考虑分布式)

Listener 也是一个组件，客户端不需要访问，不需要Listener-Mapping，也需要部署到 web.xml
```xml
<web-app>
    <listener>
        <listener-class>SpringContextListener</listener-class>
    </listener>
    
    
    <servlet>
    
        <!-- 组件中的 init, 存在 ServletConfig -->
        <!-- 使用 获取初始化值函数获取 -->
        <init-param>
            <param-name>x</param-name>
            <param-value>1</param-value>
        </init-param>
    </servlet>
</web-app>
```

```java
    public     interface ServletContextListener{
        public void contextInitialized(ServletContextEvent sce){
            // 命名服务
            Context cxt = new InitialContext();
            // 本地数据源
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/ExamDB");
            // 将数据源ds存储在 ServletContext
            // ServletContext sc = (ServletContext) sce.getSource();  // java代码，太过复杂
            ServletContext sc = sce.getServletContext();    // j2ee代码，两个等价
            
        }
        
        public void contextDestroyed(ServletContextEvent sce){}
    }
    
```

#### 理解 JavaEE Web Application 集成 struts

#### 理解 JaveEE Web Application 如何集成 Spring框架（考试重点，分值大）
ContextLoaderListener 实现什么接口 ServletContextListener

如何读取配置文件 
```xml
<context-param>
    <param-name>ServletConfigLocation</param-name>
    <!-- classpath: 推荐使用 -->
    <!-- classpath*: 更慢，谨慎使用，从所有的类加载路径中找spring配置文件，比如jar包-->
    <param-value>classpath*:/resources/spring/*applicationContext.xml</param-value>
</context-param>
```


#### 理解Spring framework 配置
通常 ApplicationContext.xml
```xml
<beans>
    <!-- id标识符唯一 -->
    <bean id="a" class="a.b.c.Hello" /> 
    <!-- name标识符可以重名，不推荐重名 -->
    <!-- 也可以给同一个bean组件多个名字，不推荐 
        <bean name="a,b,c" class="" />
    -->
    <bean name="a" class="a.b.c.Hello" />
    
    <!-- bean组件不给命名的话，spring自动命名，小写
        <bean class="a.Student" />
    -->
    
    <!-- 工厂方法创建 bean
        <bean id="clientService" class="examples.ClientService"
        factory-bean="serviceLocator"
        factory-name="createInstance" />
        <!-- 使用 examples.ClientService.createInstance函数创建对象 -->(error?)
        <!-- 加factory-bean后使用 serviceLocator.createInstance创建 -->
    -->
</beans>
```

bean 的创建可以由构造函数或者延迟创建
```xml
<beans>
    <bean id="beanOne" class="x.y.ThingOne">
        <contructor-arg ref="beanTwo" type="" name=""/>
        <contructor-arg ref="beanThree" />
    </bean>
    
    <bean class="x.y.ThingTwo" />
    <bean class="x.y.ThingThree" />
</beans>
```


```xml
<beans>
    <bean id="exampleBean" class="examples.ExampleBean">
        <contructor-arg type="int" value="1" />
        <contructor-arg type="String" value="fe" />
    </bean>
</beans>
```

```xml
<beans>
    <bean id="exampleBean" class="examples.ExampleBean">
        <contructor-arg name="years" value="1" />
        <contructor-arg name="ultimateAnswer" value="fe" />
    </bean>
</beans>
```

```xml
<beans>
    <bean id="exampleBean" class="examples.ExampleBean">
        <contructor-arg name="years" value="1" />
        <contructor-arg name="ultimateAnswer" value="fe" />
    </bean>
</beans>
```


setter 方法注入
```xml
<beans>
    <bean id="exampleBean" class="examples.ExampleBean">
        <property name="beanOne">
            <ref bean="anotherExampleBean" />
        </property>
        
        
        <property name="beanOne" rel="anotherExampleBean" />
    </bean>
</beans>
```

java 注解实现bean 配置
```java
@Configuration
public class AppConfig{
    @Bean("myBean", scope="")
    ...
}
```

Spring配置连接池
```xml
<bean id="" destroy-method="close">
    <property name="" value=""/>
    <property name="" value=""/>
    <property name="" value=""/>
    <property name="" value=""/>
</bean>
```


#### xml 属性 or 子元素
推荐使用子元素，唯一性的子元素最好使用属性
```xml
<student id="201792179">
    <name>郑翔</name>
    <grade>3</grade>
</student>
```


#### ApplicationContext
获取 ApplicationContext
1. 全局静态变量
2. Aware 接口，只有一个函数
3. 也可以凭借 DI

```java
public void setApplicationContext(ApplicatoinContext act){
    this.ApplicationContext = act;
}
```

```java
public interface ApplicationContextAware{
    public void setApplicationContext(ApplicatoinContext act){
        this.ApplicatoinContext = act;
    }
    
    public static getApplicatoinContext
}
```

WebApplicatoinContextUtils      工具类中的函数都是静态的

#### bean对象的作用域
默认是单例模式，只有一个实例

singleton vs prototype 必考

```xml
<bean id="" class="", scope="" />
<!-- singleton 单例 -->
<!-- prototype 模型 -->
<!-- request  -->
<!-- session -->
<!-- application -->
<!--  -->
```

#### spring配置文件
```xml
<context:annotation-config /><!-- 可以不写 -->
<context:component-scan base-package="" /><!-- 扫描 -->

<!--spring对配置文件的加载-->
<context:property-placeholderlocation="classpath:jdbc.proerties" /><!-- 最多出现一次 -->
<!-- 加载多个 class="org.springframework.beans.factory.config.Properties" -->


<!--注解，在Bean中注入-->
<!--@Value("${abc}")，自动完成类型转换    -->
<!--private String abc;-->
```


***
第九周周五：考试        11.1    网安
第十周周四：考试        11.7    j2ee
第十周周一：大作业      11.4    ros
第十周周三：考试        11.6    网络综合实验
第九周周五：大作业      11.1    可视化
