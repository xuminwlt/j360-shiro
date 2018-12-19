package me.j360.shiro.boot.web.shiro;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.subject.WebSubject;

import javax.servlet.ServletRequest;
import java.util.Set;

@Slf4j
public class WebUserRealm extends AuthorizingRealm {


    private ByteSource commonSalt;

    public void setCommonSalt(String commonSalt) {
        this.commonSalt = new SimpleByteSource(commonSalt);
    }


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

        authorizationInfo.setRoles(adminRoles);
        authorizationInfo.setStringPermissions(adminRoles);


        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token){

        //用户名和密码的登录过程
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        ServletRequest request = ((WebSubject) SecurityUtils.getSubject()).getServletRequest();

        //from db
        String hashedPass = PasswordUtil.encodedPassword("password", commonSalt);

        //增加邮件验证码验证登录，需要前端配合，暂时关闭
        String url = ((ShiroHttpServletRequest) request).getRequestURI();
//        if(StringUtils.isNotBlank(url)&&("/login/ajax".equals(url))) {
//            //登录接口校验密码：
//            if(!encodedPassword.equals(hashedPass)){
//                throw new IncorrectCredentialsException();
//            }
//        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username, //用户名
                hashedPass, //密码 处理后的 admin
                commonSalt,//salt=salt
                getName()  //realm name
        );

        //request.setAttribute("uid",userDto.getId());
        //request.setAttribute("userName",userDto.getUsername());
        //request.setAttribute("showName",userDto.getNickname());

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
