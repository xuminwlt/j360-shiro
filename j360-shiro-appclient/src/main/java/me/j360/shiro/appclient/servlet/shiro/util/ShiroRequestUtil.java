package me.j360.shiro.appclient.servlet.shiro.util;

import com.app.core.constants.Constants;
import com.app.utils.StringUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.j360.shiro.appclient.servlet.shiro.dto.ClientAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.util
 * User: min_xu
 * Date: 16/4/20 下午2:14
 * 说明：Shiro的请求头的信息
 */
public class ShiroRequestUtil {
    public static Set<String> userHeaderSet = Sets.newHashSet(Constants.REQUEST_HEADER_TIMESTAMP,Constants.REQUEST_HEADER_UID);

    public static Map<String,String> getRequestHeaderWhiteListMap(HttpServletRequest request){
        Map<String,String> map = Maps.newHashMapWithExpectedSize(4);
        //获取appid/uuid
        ClientAgent clientAgent = getClientAgent(request);
        if(null!=clientAgent){
            map.put(Constants.REQUEST_HEADER_APPID,clientAgent.getAppName());
            map.put(Constants.REQUEST_HEADER_APPUUID,clientAgent.getDeviceUUID());
        }
        map.put(Constants.REQUEST_HEADER_TIMESTAMP,request.getHeader(Constants.REQUEST_HEADER_TIMESTAMP));
        return map;
    }

    public static Map<String,String> getRequestHeaderUserMap(HttpServletRequest request){
        Map<String,String> map = Maps.newHashMapWithExpectedSize(6);
        for(String key:userHeaderSet){
            map.put(key,request.getHeader(key));
        }
        //获取appid/uuid
        ClientAgent clientAgent = getClientAgent(request);
        if(null!=clientAgent){
            map.put(Constants.REQUEST_HEADER_APPID,clientAgent.getAppName());
            map.put(Constants.REQUEST_HEADER_APPUUID,clientAgent.getDeviceUUID());
        }
        return map;
    }

    public static String getRequestHeaderWhiteListSign(HttpServletRequest request){
        return request.getHeader(Constants.REQUEST_HEADER_TOKEN);
    }

    public static String getRequestHeaderUserSign(HttpServletRequest request){
        return request.getHeader(Constants.REQUEST_HEADER_TOKEN);
    }

    public static String getRequestHeaderUid(HttpServletRequest request){
        return request.getHeader(Constants.REQUEST_HEADER_UID);
    }

    /**
     * 获取请求的客户端及设备信息
     * Client-Agent: Fotoplace/3.1.0/1602/iOS/7.1/iPhone 5s (A1457/A1518/A1528/A1530)/7EAB70B1-624F-463A-943C-E7FF235A9A0C
     * 在传递参数的时候setOsNumber安卓=1，苹果=iOS
     */
    public static ClientAgent getClientAgent(HttpServletRequest request) {
        String clientAgentString = request.getHeader(Constants.REQUEST_HEADER_CLIENT_AGENT);
        if (StringUtils.isNotEmpty(clientAgentString)) {
            //因为iPhone里面有括号斜线，导致获取失败，用正则去掉（）及里面的内容
            clientAgentString = clientAgentString.replaceAll(" \\(.*?\\)", "");
            String userAgent[] = clientAgentString.split("/");
            if (userAgent.length == 7) {
                ClientAgent clientAgent = new ClientAgent();
                clientAgent.setAppName(userAgent[0]);
                clientAgent.setVersion(userAgent[1]);
                clientAgent.setBuildVersion(Integer.parseInt(userAgent[2]));
                clientAgent.setOsNumber(userAgent[3]);
                clientAgent.setOsVersion(userAgent[4]);
                clientAgent.setDeviceModel(userAgent[5]);
                clientAgent.setDeviceUUID(userAgent[6]);

                return clientAgent;
            }
        }
        return null;
    }


    public static Map<String,String> getSortedParam(Map<String,String> map){
        Collection<String> keyset= map.keySet();
        List<String> list = new ArrayList<String>(keyset);
        //对key键值按字典升序排序
        Collections.sort(list);

        Map<String,String> sortMap = Maps.newHashMap();
        for (int i = 0; i < list.size(); i++) {
            sortMap.put(list.get(i),map.get(list.get(i)));
        }
        return sortMap;
    }
}
