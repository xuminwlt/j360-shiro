package me.j360.shiro.appclient.servlet.shiro.realm;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;


/**
 * BaseToken
 */
public class StatelessBaseToken implements AuthenticationToken {

    @Getter
    @Setter
    private String appId;

    @Override
    public Object getPrincipal() {
        return appId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
