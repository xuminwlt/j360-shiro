package me.j360.shiro.boot.app.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.j360.shiro.boot.AppConfig;
import me.j360.shiro.boot.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 智能识别的的Filter
 * 根据是否有相关标示兼容不同的role身份
 * app下兼容需要登录和非登录的页面验证,登录确认需要增加一个Filter作为配合
 *
 * /page1/toall = smart
 * /page1/tologinuser = smart, user
 * /page1/toguest = smart
 */
@Slf4j
public class StatelessSmartAuthcFilter extends AccessControlFilter {


    @Getter
    @Setter
    private Algorithm algorithm;

    private static final String Authorization = "Authorization";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //强制为否，必须经过验证

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        JwtToken jwtToken = new JwtToken();

        String authorization = ((HttpServletRequest) request).getHeader(Authorization);
        if (StringUtils.isEmpty(authorization)) {
            //TODO
        }
        if(StringUtils.isEmpty(authorization) || ! authorization.startsWith("Bearer ")){
            //TODO
        }
        String token = authorization.substring(7);
        //sign check
        String sub;
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(AppConfig.JWT_ISSUER)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            sub = jwt.getSubject();
            List<String> aud = jwt.getAudience();
            boolean access = false;
            if (aud.contains(AppConfig.JWT_AUD_GUEST)) {


            } else if (aud.contains(AppConfig.JWT_AUD_USET)) {
                access = true;

                //user check

            }
            if (!access) {
                //TODO
            }
        } catch (JWTVerificationException exception){
            //Invalid signature/claims //TODO

        }

        return true;
    }

}
