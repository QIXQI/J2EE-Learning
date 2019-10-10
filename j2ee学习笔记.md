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
    
    --> 类似与 Servlet 中的 service 根据 method 分发为 doGet, doPost

解决控制器类的复用性 Controller 类，降低类的粒度，提高类的可复用性

为什么有复用性问题：modules中

基于 mvc 结构的框架

a rapid framework for web development based on JavaEE

请求经过：前端控制器、action、后端控制器、module

org.apache.struts.action
    Action 类

前端控制器 org.apache.struts.ActionServlet

前端控制器的流程、职责（考试）

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