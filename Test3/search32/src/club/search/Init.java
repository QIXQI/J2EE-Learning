package club.search;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@WebServlet("/Init")
public class Init extends HttpServlet {
    private static final long serialVersionUID = 1L;

    Person[] persons = new Person[100];

    @Override
    public void init() throws ServletException{
        File file = new File(getServletContext().getRealPath("contact/test.xls"));
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
            int t = 0;
            for(int i=0; i<sheetSize; i++){
                // 每个页创建一个Sheet对象
                Sheet sheet = wb.getSheet(i);
                // sheet.getRows() 返回该页的总行数
                for(int j=0; j<sheet.getRows(); j++){
                    if(i == 0 && j == 0){
                        continue;       // 不能读取第一页第一行
                    }
                    // sheet.getColumns() 返回该页的总列数
                    id = sheet.getCell(0, j).getContents();
                    name = sheet.getCell(1, j).getContents();
                    phone = sheet.getCell(2, j).getContents();
                    qq = sheet.getCell(3, j).getContents();
                    email = sheet.getCell(4, j).getContents();
                    persons[t] = new Person(id, name, phone, qq, email);
                    t ++;
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        try{
            Context cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup("java:comp/env/jdbc/ExamDB");
            Connection conn = ds.getConnection();

            // 清空表
            String sql = "delete from persons";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.executeUpdate();

            // 插入表
            for(int i=0; i<persons.length && persons[i] != null; i++){
                String id = persons[i].getId();
                String name = persons[i].getName();
                String phone = persons[i].getPhone();
                String qq = persons[i].getQQ();
                String email = persons[i].getEmail();
                sql = "insert into persons values(?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, phone);
                if(qq.equals("")){
                    pst.setString(4, null);
                }else{
                    pst.setString(4, qq);
                }
                if(email.equals("")){ 
                    pst.setString(5, null);
                }else{
                    pst.setString(5, email);
                }
                pst.executeUpdate();
            }
            out.println("<h2 align=\"center\">数据库初始化成功</h2>\n");
            pst.close();
            conn.close();
        }catch(NamingException ne){
            out.println("NamingException");
            ne.printStackTrace();
        }catch(SQLException se){
            out.println("SQLException: " + se.getMessage());
            se.printStackTrace();
        }
    }
}