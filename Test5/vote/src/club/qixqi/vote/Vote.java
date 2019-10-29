/**
 * 1. spring 如何实现像 Servlet 中的init那样的方法，来实现项目初始化时初始化数据库
 * 2. 参数 HttpSession session 和 request.getSession()，两个session有什么区别呢
 * 3. getUserIp 中获得的ip为什么还要根据',' 截取
 * 4. vote_ip 表应该投票成功在更新
 * 5. 投票后更新 sessio，效率如何更高呢？
 */
package club.qixqi.vote;

import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class Vote{
    @RequestMapping("/vote.do")
    public String vote(Locale locale, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        String [] votes = request.getParameterValues("teacher");
        if(votes == null || votes.length == 0){
            return "null_error";
        }
        int len = votes.length;
        System.out.println("len = " + len);         // console

        // 完成投票，票数增加
        try{
            Context cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup("java:comp/env/jdbc/vote");
            Connection conn = ds.getConnection();
            String [] ids = new String[len];
            int [] polls = new int[len];
            int poll_index = 0;

            // 获取ip，判断是否存在刷票行为
            String ip = getUserIp(request);
            System.out.println("user_ip: " + ip);
            if(isBrush(ip, conn)){      // 存在刷票行为
                // session.setAttribute("message", "brush");
                conn.close();
                return "brush_error";
            }else{                      // 不存在刷票行为
                // 获取数据库信息
                String sql = "select id, poll from vote where id = " + votes[0];
                String temp_sql = "where id in (" + votes[0];   // where id in (11, 22, 33), 更新数据库时用到
                for(int i=1; i<len; i++){
                    sql += " or id = " + votes[i];
                    temp_sql += ", " + votes[i];
                }
                temp_sql += ");";
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    ids[poll_index] = rs.getString("id");
                    polls[poll_index] = rs.getInt("poll") + 1;
                    poll_index ++;
                }
                for(int i=0; i<len; i++){
                    System.out.println(polls[i] + "  " + ids[i]);
                }

                // 更新数据库
                sql = "update vote set poll = case id ";
                for(int i=0; i<len; i++){
                    sql += " when " + ids[i] + " then " + polls[i];       // votes[i] 和 poll[i] 不匹配，votes[i] 是复选框点击顺序，而 poll[i] 是数据库中的id顺序
                }
                sql += " end " + temp_sql;
                System.out.println("update_sql: " + sql);       // console
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                update_vote_ip(ip, conn);           // 更新 vote_ip 表
                System.out.println("投票成功");         // console
                // session.setAttribute("message", "success");
                update_session(session);
                rs.close();
                pst.close();
                conn.close();
            }
        } catch(NamingException ne){
            System.out.println("NamingException: " + ne.getMessage());
        } catch(SQLException se){
            System.out.println("SQLException: " + se.getMessage());
        }
        // session.setAttribute("votes", votes);
        return "vote_result";
    }

    // 获取用户ip
    protected String getUserIp(HttpServletRequest request){
        // 优先获取 X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        String ERROR_IP = "127.0.0.1";
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("x-forwarded-for");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
            if("0:0:0:0:0:0:0:1".equals(ip)){
                ip = ERROR_IP;
            }
        }
        if("unknown".equalsIgnoreCase(ip)){
            ip = ERROR_IP;
            return ip;
        }
        int index = ip.indexOf(',');
        if(index >= 0){
            ip = ip.substring(0, index);
        }
        return ip;
    }

    /**
     * 
     * @param ip
     * @param conn
     * @return 是否刷票
     * @throws NamingException
     * @throws SQLException
     */
    protected boolean isBrush(String ip, Connection conn) throws NamingException, SQLException {       
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        boolean flag = false;
        
        String sql = "select last_vote_time from vote_ip where ip = '" + ip + "';";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            Date last_vote_time = rs.getTimestamp("last_vote_time");
            System.out.println("last_vote_time: " + df.format(last_vote_time));
            System.out.println("now: " + df.format(now));
            if((df.format(last_vote_time)).equals(df.format(now))){     // 刷票
                flag = true;
            }
        }
        rs.close();
        pst.close();
        return flag;
    }


    /**
     * 更新 vote_ip 表
     * @param ip
     * @param conn
     * @throws NamingException
     * @throws SQLException
     */
    protected void update_vote_ip(String ip, Connection conn) throws NamingException, SQLException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();

        String sql = "select last_vote_time from vote_ip where ip = '" + ip + "';";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){      // 更新条例
            sql = "update vote_ip set last_vote_time = ? where ip = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, df.format(now));
            pst.setString(2, ip);
            pst.executeUpdate();
        }else{              // 插入条例
            sql = "insert into vote_ip(ip, last_vote_time) values(?, ?)";
            pst = conn.prepareStatement(sql);           // 有毒吧，这有什么问题？
            pst.setString(1, ip);
            pst.setString(2, df.format(now));
            pst.executeUpdate();
        }
        rs.close();
        pst.close();
    }

    /**
     * 更新 session 中的值
     * @param session
     */
    protected void update_session(HttpSession session){
        GetSession.initSession(session);
    }

}