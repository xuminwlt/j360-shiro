package me.j360.shiro.appclient.servlet.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * 登录的用户名匹配的校验在此处进行
 * 限定用户输入密码错误次数
 */
public class LoginHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        /*String passwordRetryMemCache = CacheConstant.SHIRO_PASSWORD_RETRY + username;   //"passwordRetryCache_"+ username;
        int time = 1*10*60;
        //10分钟
        if(spyMemcachedClient.incr(passwordRetryMemCache,1,1,time) > 10){
            throw new ExcessiveAttemptsException();
        }
        if(spyMemcachedClient.get(passwordRetryMemCache) == null){
            spyMemcachedClient.set(passwordRetryMemCache, time, "1");
        }
        boolean
            matches = super.doCredentialsMatch(token, info);

        if(matches) {
            //clear retry count
            //passwordRetryCache.remove(username);
            spyMemcachedClient.set(passwordRetryMemCache, time, "1");
        }
        return matches;*/
        return false;
    }
}
