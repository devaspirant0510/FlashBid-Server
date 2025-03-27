package seoil.capstone.flashbid.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @GetMapping("/login")
    public String loginPage(){
        return "index";
    }
    @GetMapping("/")
    @ResponseBody
    public String homepage(){
        return "home";
    }
}
