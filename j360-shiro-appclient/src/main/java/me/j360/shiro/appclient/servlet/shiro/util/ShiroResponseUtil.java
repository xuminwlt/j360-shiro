package me.j360.shiro.appclient.servlet.shiro.util;

import com.app.api.Response;
import com.fotoplace.base.util.JsonMapper;

/**
 * Package: com.fotoplace.servlet.shiro.util
 * User: min_xu
 * Date: 16/4/20 下午5:37
 * 说明：
 */
public class ShiroResponseUtil {

    public static String getAuthcFailResponse(){
        Response response = new Response();
        response.setError("会话失效，请重新登录。");
        return JsonMapper.nonEmptyMapper().toJson(response);
    }

    public static  String getLoginFailResponse(){
        Response response = new Response();
        response.setError("登录失败。");
        return JsonMapper.nonEmptyMapper().toJson(response);
    }

    public static  String getWhiteListFailResponse(){
        Response response = new Response();
        response.setError("请求无效");
        response.setStatus(403);
        return JsonMapper.nonEmptyMapper().toJson(response);
    }

}
