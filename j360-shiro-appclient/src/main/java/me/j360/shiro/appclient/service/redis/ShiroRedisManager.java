package me.j360.shiro.appclient.service.redis;

import lombok.extern.slf4j.Slf4j;
import me.j360.shiro.appclient.core.JedisTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Package: me.j360.shiro.appclient.service.redis
 * User: min_xu
 * Date: 16/5/13 下午4:29
 * 说明：
 */

@Component
@Lazy(false)
@Slf4j
public class ShiroRedisManager {

    private static final String APP_ID_UUID_REQUEST_TIMESTAMP = "app:$1:$2:request_timestamp";
    private static final String USER_UID_SECRET_INFO = "user:%s:secret";


    private static final int TTLDAY = 24*60*60;
    private static final int TTLQUART = 15*60;

    //替换成你正在使用的redis客户端
    @Autowired
    private JedisTemplate jedisTemplate;

    public boolean compareAndSetAppTimestamp(String appId,String uuid,String timestamp){
        String key = StringUtils.replace(APP_ID_UUID_REQUEST_TIMESTAMP,"$1",appId);
        key = StringUtils.replace(key,"$2",uuid);
        /*if(jedisTemplate.existsKey(key)){
            String lastTimestamp = jedisTemplate.get(key);
            if(StringUtils.isNotEmpty(lastTimestamp)){
                long l = Long.parseLong(lastTimestamp);
                long t = Long.parseLong(timestamp);
                if(t <= l){
                    return false;
                }
            }
        }
        jedisTemplate.set(key, timestamp);
        jedisTemplate.setExpire(key,TTLQUART);*/
        return true;
    }

    private String getUserSecretKey(long uid){
        return StringUtils.replaceOnce(USER_UID_SECRET_INFO,"%s",String.valueOf(uid));
    }
    /**
     * 从redis读取用户的密码信息，TTL 1天
     * @param uid
     * @return
     */
    public String getUserSecret(long uid){
        /*if(jedisTemplate.existsKey(getUserSecretKey(uid))){
            return jedisTemplate.get(getUserSecretKey(uid));
        }*/
        return null;
    }

    public void setUserSecret(long uid,String secret){
        //jedisTemplate.set(getUserSecretKey(uid),secret);
        //jedisTemplate.setExpire(getUserSecretKey(uid),TTLDAY);
    }

    public void clearUserSecret(long uid){
        //jedisTemplate.del(getUserSecretKey(uid));
    }
}
