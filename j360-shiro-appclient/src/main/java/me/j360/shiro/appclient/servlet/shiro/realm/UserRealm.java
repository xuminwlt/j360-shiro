package me.j360.shiro.appclient.servlet.shiro.realm;

import me.j360.shiro.appclient.core.Constants;
import me.j360.shiro.appclient.service.redis.ShiroRedisManager;
import me.j360.shiro.appclient.servlet.shiro.codec.HmacSHA256Utils;
import me.j360.shiro.appclient.servlet.shiro.credentials.LoginHashedCredentialsMatcher;
import me.j360.shiro.appclient.servlet.shiro.service.ShiroAccountService;
import me.j360.shiro.appclient.servlet.shiro.util.ShiroRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 *
 * 需要配置realms的list，根据support token的类型来判断，一个通过即可通过
 * 或者，重写support允许多个类型在这里执行，推荐第一种方法，根据support对应多个realm
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private LoginHashedCredentialsMatcher loginHashedCredentialsMatcher;
    @Autowired
    private SimpleCredentialsMatcher whiteListcredentialsMatcher;
    @Autowired
    private ShiroRedisManager shiroRedisManager;
    @Autowired
    private ShiroAccountService shiroAccountService;


    private static final String appId1 = "b35d7751bc8ef46b87892a6abcb8f8";
    private static final String appSecret1 = "b35d7751bc8ef46b87892a6abcb8f86ede884481ef2e94aa49a7b48fffd4c13c";

    private static final String appId2 = "f86ede884481ef2e94aa49a7b48";
    private static final String appSecret2 = "b35d7751bc8ef46b87892a6abcb8f86ede884481ef2e94aa49a7b48fffd4c13c";


    /**
     * 对Token的支持类型
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessBaseToken;
    }

    /**
     * 认证回调函数,验证对应的Token是否匹配.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //无状态的审核验证过程
        if(token instanceof StatelessAuthcToken){
            return doGetStatelessAuthcTokenInfo(token);
        }
        if(token instanceof StatelessLoginToken){
            return doGetStatelessLoginTokenInfo(token);
        }
        if(token instanceof StatelessWhiteListToken){
            return doGetStatelessWhiteListTokenInfo(token);
        }
        return null;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用，这里不做Roles StringPermissions的权限允许，产生403没有权限提示
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /**
     * 用户会话权限的Token，产生401没有授权错误
     * */
    protected AuthenticationInfo doGetStatelessAuthcTokenInfo(AuthenticationToken token) throws AuthenticationException {
        String uid = (String) token.getPrincipal();
        if(StringUtils.isEmpty(uid)){
            throw new AuthenticationException();
        }
        Map<String,String> param = ((StatelessAuthcToken) token).getParams();
        if(null == param){
            throw new AuthenticationException();
        }
        String appId = null;
        String appUUID = null;
        String timestamp = null;

        if(param.containsKey(Constants.REQUEST_HEADER_APPID)){
            appId = param.get(Constants.REQUEST_HEADER_APPID);
        }else{
            throw new AuthenticationException();
        }
        if(param.containsKey(Constants.REQUEST_HEADER_APPUUID)){
            appUUID = param.get(Constants.REQUEST_HEADER_APPUUID);
        }else{
            throw new AuthenticationException();
        }
        if(param.containsKey(Constants.REQUEST_HEADER_TIMESTAMP)){
            timestamp = param.get(Constants.REQUEST_HEADER_TIMESTAMP);
        }else{
            throw new AuthenticationException();
        }
        String secret = getWhiteListKey(appId);
        if(null == secret){
            throw new AuthenticationException();
        }
        //param.put("appSecret",secret);

        //读取用户的uid对应的secret
        String userSecret = getUserSecret(Long.parseLong(uid));
        if(null == userSecret){
            throw new AuthenticationException();
        }
        //param.put("userSecret",userSecret);
        //对param进行字典排序
        param = ShiroRequestUtil.getSortedParam(param);

        //将uuid/timestamp加入到redis进行校验
        if(!shiroRedisManager.compareAndSetAppTimestamp(appId,appUUID,timestamp)){
            throw new AuthenticationException();
        }

        //在服务器端生成客户端参数消息摘要 appid uid timestamp
        String serverUserSign = HmacSHA256Utils.digest(userSecret, param);

        //然后进行客户端消息摘要和服务器端消息摘要的匹配
        return new SimpleAuthenticationInfo(
                uid,
                serverUserSign,
                getName());
    }

    /**
     * 用户登录验证的Token
     * */
    protected AuthenticationInfo doGetStatelessLoginTokenInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        //secret = salt
        String secret = "";//getSalt(username);
        if(secret == null){
            throw new UnknownAccountException();//没找到帐号
        }

        //在服务器端生成客户端参数消息摘要 appid uid timestamp
        String serverUserSign = HmacSHA256Utils.digest(secret, ((StatelessAuthcToken) token).getParams());

        //然后进行客户端消息摘要和服务器端消息摘要的匹配
        return new SimpleAuthenticationInfo(
                username,
                serverUserSign,
                getName());
    }
    /**
     * 白名单权限的Token
     * */
    protected AuthenticationInfo doGetStatelessWhiteListTokenInfo(AuthenticationToken token) throws AuthenticationException {
        //String appId = (String) token.getPrincipal();
        Map<String,String> param = ((StatelessWhiteListToken) token).getParams();
        if(null == param){
            throw new AuthenticationException();
        }
        String appId = null;
        String appUUID = null;
        String timestamp = null;

        if(param.containsKey(Constants.REQUEST_HEADER_APPID)){
            appId = param.get(Constants.REQUEST_HEADER_APPID);
        }else{
            throw new AuthenticationException();
        }
        if(param.containsKey(Constants.REQUEST_HEADER_APPUUID)){
            appUUID = param.get(Constants.REQUEST_HEADER_APPUUID);
        }else{
            throw new AuthenticationException();
        }
        if(param.containsKey(Constants.REQUEST_HEADER_TIMESTAMP)){
            timestamp = param.get(Constants.REQUEST_HEADER_TIMESTAMP);
        }else{
            throw new AuthenticationException();
        }

        String secret = getWhiteListKey(appId);
        if(null == secret){
            throw new AuthenticationException();
        }
        //param.put("appSecret",secret);
        //对param进行字典排序
        param = ShiroRequestUtil.getSortedParam(param);

        //将uuid/timestamp加入到redis进行校验
        if(!shiroRedisManager.compareAndSetAppTimestamp(appId,appUUID,timestamp)){
            throw new AuthenticationException();
        }
        //从服务器获取对应的secret、appkey、token进行加密
        String serverUserSign = HmacSHA256Utils.digest(secret, param);
        //然后进行客户端消息摘要和服务器端消息摘要的匹配
        return new SimpleAuthenticationInfo(
                appId,
                serverUserSign,
                getName());
    }



    private String getUserSecret(long uid) {
        return shiroAccountService.getShiroSecret(uid);
    }

    private String getWhiteListKey(String appId) {
        if(appId1.equals(appId)){
            return appSecret1;
        }else if(appId2.equals(appId)){
            return appSecret2;
        }
        return null;
    }

    /**
     * 验证账号状态，禁止登录之类的
     * */
    public void checkAccountStatus(String uid){
        /*
        throw new DisabledAccountException();   //账号禁用
        throw new LockedAccountException(); //帐号锁定
        throw new UnknownAccountException();//没找到帐号
        throw new ExpiredCredentialsException(); //凭证过期，需要重新登录
        */
    }

}
