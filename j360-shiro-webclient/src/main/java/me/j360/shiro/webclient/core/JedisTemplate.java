package me.j360.shiro.webclient.core;

import org.springframework.stereotype.Repository;

/**
 * Package: me.j360.shiro.appclient.core
 * User: min_xu
 * Date: 16/8/2 下午10:54
 * 说明：
 */
@Repository
public class JedisTemplate {


    public static void set(String key,String value){

    }

    public static void setExpire(String key,int secends){

    }

    public static String get(String key) {
        return "";
    }

    public static String hget(String key,String name) {
        return "";
    }

    public static void hset(String key, String name, String value) {

    }

    public static void del(String key) {

    }

}
