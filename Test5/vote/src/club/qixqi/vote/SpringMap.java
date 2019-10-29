package club.qixqi.vote;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SpringMap{
    @RequestMapping("/init.do")
    public String init(HttpServletRequest request){
        // 获取 filePath
        String filePath = request.getServletContext().getRealPath("json/teachers.json");
        ReadJson.readJson(filePath);
        return "init_result";
    }

    // @RequestMapping("/vote.do")
    // public String vote(){

    //     return "vote_result";
    // }
}