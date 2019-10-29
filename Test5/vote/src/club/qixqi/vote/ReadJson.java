/**
 * 1. 每次读入内存一个对象，然后插入数据库，可不可以最后一次插入再执行 pst.executeUpdate() 呢？
 * 2. 读取 teachers.json 时文件路径问题，需要导入 servlet，不太好
 */

package club.qixqi.vote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * 相当于一个工具类，静态函数，读取Json中的信息
 * 在 j2ee 数据库中新建 vote表
 * 
 * 在流和对象模型之间使用混合模式
 * 将 teachers.json 中的对象一个一个的读入内存
 */
public class ReadJson{
    public static void readJson(String filePath){
        try{
            // 在连接池中获取连接
            Context cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup("java:comp/env/jdbc/vote");
            Connection conn = ds.getConnection();

            // 创建表 vote
            String sql = "create table if not exists `vote`(" + 
                "`id` int(11) not null, " + 
                "`name` varchar(255) not null, " +
                "`subject` varchar(255) not null, " +
                "`sex` varchar(255) not null, " +
                "`age` int(5) not null, " +
                "`poll` int(11) not null default 0, " +
                "primary key(`id`)" +
                ") ENGINE=InnoDB default charset=utf8;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.executeUpdate();

            // 清空 vote 表
            sql = "delete from vote";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

            // 创建 vote_ip 表
            sql = "create table if not exists `vote_ip` (" +
                "`ip` varchar(255) not null, " +
                "`last_vote_time` datetime not null, " +
                "primary key(`ip`)" +
                ") ENGINE=InnoDB default charset=utf8;";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

            // 清空 vote_ip 表
            sql = "delete from vote_ip";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

            // String filePath = JsonReader.class.getClassLoader().getResource("json/teachers.json").getPath();
            // String filePath = "/vote/json/teachers.json";
            // String filePath = getServletContext().getRealPath("json/teachers.json");
            System.out.println(filePath);
            InputStream stream = new FileInputStream(new File(filePath));

            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // 流模式读取 json 文件
            reader.beginArray();
            while(reader.hasNext()){
                // 对象模式读取文件
                Teacher teacher = gson.fromJson(reader, Teacher.class);

                // 插入表
                sql = "insert into vote(id, name, subject, sex, age) values (?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, teacher.getId());
                pst.setString(2, teacher.getName());
                pst.setString(3, teacher.getSubject());
                pst.setString(4, teacher.getSex());
                pst.setInt(5, teacher.getAge());
                pst.executeUpdate();        // 可不可以最后更新
            }
            System.out.println("success!");
            reader.close();
            pst.close();
            conn.close();
        } catch(NamingException ne){
            System.out.println("NamingException: " + ne.getMessage());
        } catch(SQLException se){
            System.out.println("SQLException: " + se.getMessage());
        } catch(UnsupportedEncodingException uex){
            System.out.println("UnsupportedEncodingException: " + uex.getMessage());
        } catch(IOException ex){
            System.out.println("IOException: " + ex.getMessage());
        }
    }
}