/**
 * 1. 没有session.getAttribute("id") == null 的判断，会出现空指针异常，但不是判断过 session.isNew 了吗
 * 2. json 中不允许单引号
 */

package club.qixqi.vote;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;

@Controller
public class GetSession{
    @RequestMapping("/getSession")
    public void getSession(Locale locale, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(session.isNew()){
            // response.getWriter().write("new");
            initSession(session);       // session 初始化
        }
        if(session.getAttribute("id") == null){     // 不知道为什么
            initSession(session);
        }
        // 获取session 并返回
        String id = session.getAttribute("id").toString();
        String teachers = session.getAttribute("teachers").toString();
        String vote_numbers = session.getAttribute("vote_numbers").toString();
        String sex = session.getAttribute("sex").toString();
        String age = session.getAttribute("age").toString();
        String sex_numbers = session.getAttribute("sex_numbers").toString();
        String age_numbers = session.getAttribute("age_numbers").toString();
        JSONObject responseJson = new JSONObject();
        responseJson.put("id", id);
        responseJson.put("teachers", teachers);
        responseJson.put("vote_numbers", vote_numbers);
        responseJson.put("sex", sex);
        responseJson.put("age", age);
        responseJson.put("sex_numbers", sex_numbers);
        responseJson.put("age_numbers", age_numbers);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSON.toJSONString(responseJson));
        System.out.println(JSON.toJSONString(responseJson));
    }

    /**
     * 初始化 session
     */
    protected static void initSession(HttpSession session){
        try{
            // 要保存的数据列表
            List<String> id = new ArrayList<String>();
            List<String> teachers = new ArrayList<String>();
            List<Integer> vote_numbers = new ArrayList<Integer>();
            List<String> sex = new ArrayList<String>();
            List<Integer> age = new ArrayList<Integer>();
            List<JSONObject> sex_numbers = new ArrayList<JSONObject>();
            List<JSONObject> age_numbers = new ArrayList<JSONObject>();
            int male_vote_numbers = 0;
            int female_vote_numbers = 0;
            int age_20s_numbers = 0;
            int age_30s_numbers = 0;
            int age_40s_numbers = 0;
            int age_50s_numbers = 0;

            Context cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup("java:comp/env/jdbc/vote");
            Connection conn = ds.getConnection();

            String sql = "select id, name, sex, age, poll from vote";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                id.add(rs.getString("id"));
                teachers.add("\"" + rs.getString("name") + "\"");     // 必须加 "小明", 不然JSON.parse(teachers)报错,json 中不允许单引号
                vote_numbers.add(rs.getInt("poll"));
                sex.add("\"" + rs.getString("sex") + "\"");
                age.add(rs.getInt("age"));
                if("男".equals(rs.getString("sex"))){
                    male_vote_numbers += rs.getInt("poll");
                }else{
                    female_vote_numbers += rs.getInt("poll");
                }
                if(rs.getInt("age") >= 20 && rs.getInt("age") < 30){
                    age_20s_numbers += rs.getInt("poll");
                }else if(rs.getInt("age") >= 30 && rs.getInt("age") < 40){
                    age_30s_numbers += rs.getInt("poll");
                }else if(rs.getInt("age") >= 40 && rs.getInt("age") < 50){
                    age_40s_numbers += rs.getInt("poll");
                }else if(rs.getInt("age") >= 50 && rs.getInt("age") <= 60){
                    age_50s_numbers += rs.getInt("poll");
                }
            }
            JSONObject male_json = new JSONObject();
            male_json.put("name", "male");
            male_json.put("value", male_vote_numbers);
            sex_numbers.add(male_json);
            JSONObject female_json = new JSONObject();
            female_json.put("name", "female");
            female_json.put("value", female_vote_numbers);
            sex_numbers.add(female_json);

            JSONObject json_20s = new JSONObject();
            json_20s.put("name", "20s");
            json_20s.put("value", age_20s_numbers);
            age_numbers.add(json_20s);
            JSONObject json_30s = new JSONObject();
            json_30s.put("name", "30s");
            json_30s.put("value", age_30s_numbers);
            age_numbers.add(json_30s);
            JSONObject json_40s = new JSONObject();
            json_40s.put("name", "40s");
            json_40s.put("value", age_40s_numbers);
            age_numbers.add(json_40s);
            JSONObject json_50s = new JSONObject();
            json_50s.put("name", "50s");
            json_50s.put("value", age_50s_numbers);
            age_numbers.add(json_50s);

            // 存入session 中的JSON对象
            // JSONObject sessionJson = new JSONObject();
            // sessionJson.put("id", id);
            // sessionJson.put("teachers", teachers);
            // sessionJson.put("vote_numbers", vote_numbers);
            // sessionJson.put("sex", sex);
            // sessionJson.put("age", age);
            // sessionJson.put("sex_numbers", sex_numbers);
            // sessionJson.put("age_numbers", age_numbers);
            // System.out.println(sessionJson);
            session.setAttribute("id", id);
            session.setAttribute("teachers", teachers);
            session.setAttribute("vote_numbers", vote_numbers);
            session.setAttribute("sex", sex);
            session.setAttribute("age", age);
            session.setAttribute("sex_numbers", sex_numbers);
            session.setAttribute("age_numbers", age_numbers);
        } catch(NamingException ne){
            System.out.println("NamingException: " + ne.getMessage());
        } catch(SQLException se){
            System.out.println("SQLException: " + se.getMessage());
        }
    }


}