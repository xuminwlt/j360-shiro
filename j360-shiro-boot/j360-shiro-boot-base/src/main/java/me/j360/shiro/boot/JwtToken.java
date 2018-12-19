package me.j360.shiro.boot;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author: min_xu
 * @date: 2018/12/19 5:22 PM
 * 说明：
 */
public class JwtToken implements AuthenticationToken {

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
