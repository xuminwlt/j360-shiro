package me.j360.shiro.boot.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: min_xu
 * @date: 2018/12/18 4:23 PM
 * 说明：
 */

@Slf4j
@Controller
@SpringBootApplication
public class J360Application {

    public static void main(String[] args) {
        SpringApplication.run(J360Application.class, args);
    }

    @ResponseBody
    @RequestMapping("/")
    public String home1(HttpServletRequest request, Model model) {
        System.out.println("---->"+request.getSession().getId() + "<----");
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        System.out.println("---->home "+request.getSession().getId() + "<----");
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/unauthenticated")
    public String unauthenticated(HttpServletRequest request, Model model) {
        System.out.println("---->unauthenticated "+request.getSession().getId() + "<----");
        //response DEFAULT_ERROR_KEY_ATTRIBUTE_NAME
        System.out.println(request.getAttribute("shiroAuthzFailure").toString());
        return "unauthenticated";
    }

}


