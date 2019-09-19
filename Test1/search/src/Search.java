import java.io.*;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.servlet.annotation.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * 访问 WebContent 下的资源使用 ./
 *  对于Web / 相当于整个服务器的根目录 即，http://localhost:8080
 *  但是WEB-INF不可以使用 ./来访问里面的资源, WEB-INF，如果没有配置路径，只能使用绝对路径（好像是虚拟路径那个玩意），或者知道当前路径，转换位相对路径
 *  /WEB-INF/contact/test.xls 访问失败
 * */

/**
 * Person 类
 */
class Person{
    String id;
    String name;
    String phone;
    String qq;
    String email;
    Person(){

    }
    Person(String id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    Person(String id, String name, String phone, String qq, String email){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.qq = qq;
        this.email = email;
    }

    String getId(){
        return this.id;
    }
    void setId(String id){
        this.id = id;
    }
    String getName(){
        return this.name;
    }
    void setName(String name){
        this.name = name;
    }
    String getPhone(){
        return this.phone;
    }
    void setPhone(String phone){
        this.phone = phone;
    }
    String getQQ(){
        return this.qq;
    }
    void setQQ(String qq){
        this.qq = qq;
    }
    String getEmail(){
        return this.email;
    }
    void setEmail(String email){
        this.email = email;
    }
    /* 字符串匹配 
       返回值： 
            2 精确匹配
            1 模糊匹配
            0 不匹配 
    */  
    int match(String search){
        int relation = 0;
        if(search == null) {
        		return relation;
        }
        if(search.equals(this.id)){			// 防止 this.id 为空时出现空指针异常
            relation = 2;
            return relation;
        }else if(this.id != null && (this.id).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.name)){
            relation = 2;
            return relation;
        }else if(this.name != null && (this.name).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.phone)){
            relation = 2;
            return relation;
        }else if(this.phone != null && (this.phone).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.qq)){
            relation = 2;
            return relation;
        }else if(this.qq != null && (this.qq).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.email)){
            relation = 2;
            return relation;
        }else if(this.email != null && (this.email).indexOf(search) != -1){
            relation = 1;
        }
        return relation;
    }
}


@WebServlet("/Search")
public class Search extends HttpServlet{
    private static final long serialVersionUID = 1L;

    Person[] persons = new Person[100];     // persons 对象数组

    private final int search_num = 10;      // 最大搜索条数
    private int[] precises = new int[search_num];       // 默认值是0，精确匹配 person下标  + 1
    private int[] fuzzys = new int[search_num];         // 模糊匹配 person下标 + 1
    int plen = 0;       // precises 当前长度
    int flen = 0;       // fluzzys 当前长度

    protected void searchResult(String search){
        for(int i=0; i<persons.length && persons[i]!=null; i++){
            int temp = persons[i].match(search);
            if(temp == 2){
                precises[plen ++] = i + 1;
                if(plen >= search_num){
                    break;
                }
            }else if(temp == 1 && flen < search_num){
                fuzzys[flen ++] = i + 1;
            }
        }
    }

    @Override
    public void init() throws ServletException{
        File file = new File("/Users/zhengxiang/Desktop/hello/eclipse-workspace/exam1/search/WebContent/WEB-INF/contact/test.xls");
        // File file = new File("/WEB-INF/contact/test.xls");
        String id;
        String name;
        String phone;
        String qq;
        String email;
        try{
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            Workbook wb = Workbook.getWorkbook(is);
            // Excel 的页签数量
            int sheetSize = wb.getNumberOfSheets();
            for(int i=0; i<sheetSize; i++){
                // 每个页创建一个Sheet对象
                Sheet sheet = wb.getSheet(i);
                // sheet.getRows() 返回该页的总行数
                for(int j=0; j<sheet.getRows(); j++){
                    // sheet.getColumns() 返回该页的总列数
                    id = sheet.getCell(0, j).getContents();
                    name = sheet.getCell(1, j).getContents();
                    phone = sheet.getCell(2, j).getContents();
                    qq = sheet.getCell(3, j).getContents();
                    email = sheet.getCell(4, j).getContents();
                    persons[j] = new Person(id, name, phone, qq, email);
                }
            } 
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(BiffException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");      // Post方式
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        // server.xml   中默认设置位 UTF-8
        // String search = new String(request.getParameter("search").getBytes("ISO8859-1"), "UTF-8");
        String search = request.getParameter("searchContent");
        plen = 0;		// 很奇怪，再次查找时值不是0，有上次查找的结果
        flen = 0;
        // out.println("<p>plen = " + plen + ",  flen = " + flen + "</p>\n");
        searchResult(search);
        int result_num =  (plen + flen >= search_num) ? search_num : (plen + flen);      // 实际查找条数
        out.println("<!DOCTYPE html>\n" +
            "<html lang=\"zh\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\" />\n" +
            "<title>search</title>\n" +
            "<link rel=\"stylesheet\" href=\"./css/search.css\" type=\"text/css\" />\n" +
            "</head>\n" +
            "<body>\n" + 
            "<form action=\"http://localhost:8080/search/Search\" method=\"POST\">\n" +
            "<div class=\"search_name\">\n" +
            "<label>\n" +
            "<span style=\"color: rgb(60, 122, 252);\">软</span>\n" +
            "<span style=\"color: rgb(251, 38, 10);\">院</span>\n" +
            "<span style=\"color: rgb(255, 190, 0);\">找</span>\n" +
            "<span style=\"color: rgb(60, 122, 252);\">人</span>\n" +
            "</label>\n" +
            "</div>\n" +
            "<div id=\"search_father\">\n" +
            "<div id=\"search\">\n" +
            "<div class=\"search_empty\"></div>\n" +
            "<div class=\"search_content\">\n" +
            "<input type=\"text\" name=\"searchContent\" />\n" +
            "</div>\n" +
            "</div>\n" +
            "<button type=\"submit\" aria-label=\"软院查人\" class=\"search_go\">\n" +
            "<div class=\"search_icon\">" +
            "<span>\n" +
            "<svg focusable=\"false\" xmls=\"http://www.w3.org/2000/svg\" viewbox=\"0 0 24 24\">\n" +
            "<path d=\"M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 " + 
            "3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 " + 
            "5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z\"></path>\n" +
            "</svg>\n" +
            "</span>\n" +
            "</div>\n" +
            "</button>\n" +
            "</div>\n" +
            "</form>\n" +
            "<div class=\"search_result\">\n"
        );
        if(result_num > 0){
            out.println("<label class=\"result_title\"><span>查找结果如下所示(" + result_num + "条)</span></label><br />\n");
        }else{
            out.println("<label class=\"result_title\"><span>Sorry! 没有找到合适的结果</span></label><br />\n");
        }
        out.println("<table>\n");
        if(result_num != 0) {
            out.println("<tr><td>学号（教工号）</td><td>姓名</td><td>手机号</td><td>QQ</td><td>邮箱</td></tr>\n");
        }
        for(int i=0; i<search_num; i++){
            if(i < plen){
                // out.println("<label><span>");
            		out.println("<tr>");
                out.println("<td>" + persons[precises[i] - 1].getId() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getName() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getPhone() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getQQ() + "</td>");
                out.println("<td>" + persons[precises[i] - 1].getEmail() + "</td>");
                // out.println("</span></label><br />\n");
                out.println("</tr>\n");
            }else if(i - plen < flen){
                // out.println("<label><span>");
            		out.println("<tr>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getId() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getName() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getPhone() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getQQ() + "</td>");
                out.println("<td>" + persons[fuzzys[i-plen] - 1].getEmail() + "</td>");
                // out.println("</span></label><br />\n");
                out.println("</tr>\n");
            }else{
                break;
            }
        }
        out.println("</table>\n");



        
        
        out.println(
            "</div>\n" +
            "<script type=\"text/javascript\" src=\"./javascript/search.js\"></script>\n" +
            "</body>\n" +
            "</html>"
        );

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

}