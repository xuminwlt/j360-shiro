package me.j360.shiro.appclient.servlet.shiro.realm;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


/**
 * 白名单Token
 */
public class StatelessWhiteListToken extends StatelessBaseToken {

    @Getter
    @Setter
    private String appId;
    @Getter
    @Setter
    private Map<String, String> params;
    @Getter
    @Setter
    private String appSign;

    @Override
    public Object getPrincipal() {
        return appId;
    }

    @Override
    public Object getCredentials() {
        return appSign;
    }
}
