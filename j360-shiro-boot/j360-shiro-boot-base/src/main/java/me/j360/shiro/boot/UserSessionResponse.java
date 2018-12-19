package me.j360.shiro.boot;

import lombok.Data;

/**
 * @author: min_xu
 * @date: 2018/1/17 下午6:58
 * 说明：
 */

@Data
public class UserSessionResponse {

    private String jwt;
    private boolean register;

}
