package me.j360.shiro.appclient.servlet.shiro.service;

import me.j360.shiro.appclient.service.redis.ShiroRedisManager;
import me.j360.shiro.appclient.servlet.shiro.dao.ShiroAccountDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.service
 * User: min_xu
 * Date: 16/4/20 下午5:00
 * 说明：Shiro获取登录的信息，先写在这里模拟数据，后期需要调用User模块的信息
 */
public class ShiroAccountService {

    @Autowired
    private ShiroRedisManager shiroRedisManager;
    @Autowired
    private ShiroAccountDao shiroAccountDao;


    public String getShiroSecret(Long uid){
        String userSecret = shiroRedisManager.getUserSecret(uid);
        if(StringUtils.isNotEmpty(userSecret)){
            return userSecret;
        }else{
            String secret = shiroAccountDao.getUserSecret(uid);
            if(StringUtils.isNotEmpty(secret)){
                //写入到redis
                shiroRedisManager.setUserSecret(uid,secret);
                return secret;
            }
        }
        return null;
    }

    /**
     * 外部执行清空的命令
     * @param uid
     */
    public void clearShiroSecret(Long uid){
        if(null != uid){
            shiroRedisManager.clearUserSecret(uid);
        }
    }

}
