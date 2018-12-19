package me.j360.shiro.boot;

/**
 * @author: min_xu
 * @date: 2018/1/18 上午11:48
 * 说明：
 */
public class AppConfig {

    //Auth 定义jwt内容
    public static final String JWT_AUD_GUEST = "guest";
    public static final String JWT_AUD_USET = "user";
    public static final String JWT_ISSUER = "J360";

    //GUEST对应的访问URL
    public static final String[] JWT_VTM = {"/api/biz/**"};

}
