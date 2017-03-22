package me.j360.shiro.webclient.servlet.shiro.realm;

import com.google.common.collect.Sets;
import me.j360.shiro.webclient.servlet.shiro.token.QrToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Set;

public class QrUserRealm extends AuthorizingRealm {



    private static final Set<String> userRoles = Sets.newHashSet("ROLE_USER");
    private static final Set<String> adminRoles = Sets.newHashSet("ROLE_ADMIN");

    /**
     * 对Token的支持类型
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        return  (token instanceof AuthenticationToken);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //username = uid
        if(StringUtils.isNumeric(username)){
            authorizationInfo.setRoles(userRoles);
            authorizationInfo.setStringPermissions(userRoles);
        }else{
            authorizationInfo.setRoles(adminRoles);
            authorizationInfo.setStringPermissions(adminRoles);
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        if(token instanceof QrToken){
            return doGetQrcodeAuthcTokenInfo(token);
        }
        //用户名和密码的登录过程
        String username = (String)token.getPrincipal();
        String password = new String((char[])token.getCredentials());



        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username,
                "encodedPassword",
                ByteSource.Util.bytes("username+salt"),
                getName()
        );


        return authenticationInfo;
    }


    protected AuthenticationInfo doGetQrcodeAuthcTokenInfo(AuthenticationToken token) throws AuthenticationException {
        String uid = (String) token.getPrincipal();
        QrToken qrToken = (QrToken) token;
        String secret = qrToken.getQrCode();

        String encodedPassword = "";//PasswordUtil.encodedPassword(uid,secret,uid);

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                uid, //用户名
                encodedPassword,
                ByteSource.Util.bytes(uid),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


}
