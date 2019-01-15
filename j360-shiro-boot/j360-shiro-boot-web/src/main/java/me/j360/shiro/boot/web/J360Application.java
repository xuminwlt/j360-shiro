package me.j360.shiro.boot.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: min_xu
 * @date: 2018/12/18 4:23 PM
 * 说明：
 */

@Slf4j
@Controller
@EnableRedisHttpSession
@SpringBootApplication
public class J360Application {

    public static void main(String[] args) {
        SpringApplication.run(J360Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @ResponseBody
    @RequestMapping("/")
    public String home1(HttpServletRequest request, Model model) {
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        return "hello";
    }


    @ResponseBody
    @RequestMapping("/unauthenticated")
    public String unauthenticated(HttpServletRequest request, Model model) {
        //response DEFAULT_ERROR_KEY_ATTRIBUTE_NAME
        System.out.println(request.getAttribute("shiroAuthzFailure").toString());
        return "unauthenticated";
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        // 建议密码加密传输
        // 创建Token
        String username = "username";
        String password = "password";
        boolean rememberMe = true;
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            // 登录
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (UnknownAccountException e){
            log.error("账号不存在",e);
            return "账号不存在";
        }catch (IncorrectCredentialsException e1) {
            log.error("账号或密码错误",e1);
            return "账号或密码错误";
        }catch(AuthenticationException e2) {
            // 认证异常
            log.error("登录认证失败，[{}]",e2);
            return "认证失败";
        }
        return "hello";
    }
}


