package me.j360.shiro.appclient.servlet.shiro.dao;

import me.j360.shiro.appclient.core.BaseJdbcDao;
import me.j360.shiro.appclient.core.DataUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.dao
 * User: min_xu
 * Date: 16/5/13 下午5:12
 * 说明：
 */

@Repository
public class ShiroAccountDao  extends BaseJdbcDao {

    public Map<String,Object> getUserSecretMap(long uid){
        String sql = "select private_salt as privateSalt,secret as secret,password as password from user_secret where uid = ?";
        return super.selectOne(sql,String.valueOf(uid));
    }

    public String getUserSecret(long uid){
        String sql = "select secret as secret from user_secret where uid = ?";
        return DataUtils.getDataAsString(super.selectOne(sql, String.valueOf(uid)), "secret");
    }
}
