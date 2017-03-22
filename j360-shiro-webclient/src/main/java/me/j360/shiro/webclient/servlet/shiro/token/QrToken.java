package me.j360.shiro.webclient.servlet.shiro.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Package: com.fotoplace.servlet.shiro.token
 * User: min_xu
 * Date: 2016/12/26 下午2:56
 * 说明：
 */
@Data
@AllArgsConstructor
@ToString
public class QrToken implements AuthenticationToken {

    private String uid;
    private String qrCode;

    @Override
    public Object getPrincipal() {
        return uid;
    }

    @Override
    public Object getCredentials() {
        return qrCode;
    }

}