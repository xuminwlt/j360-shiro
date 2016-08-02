package me.j360.shiro.appclient.servlet.shiro.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.dto
 * User: min_xu
 * Date: 16/4/20 下午5:04
 * 说明：
 */
public class ShiroUserDto implements Serializable{

    @Getter
    @Setter
    private String secret;
    @Getter
    @Setter
    private String privateSalt;
    @Getter
    @Setter
    private String password;
}
