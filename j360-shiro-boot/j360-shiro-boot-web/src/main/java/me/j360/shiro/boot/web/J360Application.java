package me.j360.shiro.boot.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
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

@Controller
@EnableRedisHttpSession
@SpringBootApplication
public class J360Application {

    public static void main(String[] args) {
        SpringApplication.run(J360Application.class, args);
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @ResponseBody
    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {
        System.out.println("---->"+request.getSession().getId() + "<----");
        return "hello";
    }

}


