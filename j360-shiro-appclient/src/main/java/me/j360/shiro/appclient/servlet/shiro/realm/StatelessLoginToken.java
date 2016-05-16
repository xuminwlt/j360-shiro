package me.j360.shiro.appclient.servlet.shiro.realm;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录的token
 */
public class StatelessLoginToken extends StatelessBaseToken {

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;


    public StatelessLoginToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
       return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
