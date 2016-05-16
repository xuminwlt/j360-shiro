package me.j360.shiro.appclient.servlet.shiro.realm;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 用户会话权限Token
 */
public class StatelessAuthcToken extends StatelessBaseToken {

    @Getter
    @Setter
    private String uid;
    @Getter
    @Setter
    private Map<String, String> params;
    @Getter
    @Setter
    private String token;

    @Override
    public Object getPrincipal() {
       return uid;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
