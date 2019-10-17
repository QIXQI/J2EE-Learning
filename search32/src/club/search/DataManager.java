package club.search;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DataManager{
    private static final long serialVersionUID = 1L;

    static List<Person> persons = new ArrayList<Person>();

    public static List<Person> getPersons() {
        return persons;
    }

    // 从数据库中获取 persons 列表
    public static void setPersons(){
        persons.clear();        // 清空
        String id;
        String name;
        String phone;
        String qq;
        String email;
        try{
            Context ctx  = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ExamDB");
            Connection conn = ds.getConnection();

            String sql = "select * from persons";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                id = rs.getString("id");
                name = rs.getString("name");
                phone = rs.getString("phone");
                qq = rs.getString("qq") == null ? "" : rs.getString("qq");
                email = rs.getString("email") == null ? "" : rs.getString("email");
                persons.add(new Person(id, name, phone, qq, email));
            }
        } catch(NamingException ne){
            ne.printStackTrace();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

}