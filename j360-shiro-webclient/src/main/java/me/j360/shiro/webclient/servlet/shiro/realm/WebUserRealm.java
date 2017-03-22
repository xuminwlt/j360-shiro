package me.j360.shiro.webclient.servlet.shiro.realm;

import com.google.common.collect.Sets;
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

public class WebUserRealm extends AuthorizingRealm {



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

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                "username", //用户名
                "encodedPassword", //密码 处理后的 admin
                ByteSource.Util.bytes("username+salt"),//salt=username+salt
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
