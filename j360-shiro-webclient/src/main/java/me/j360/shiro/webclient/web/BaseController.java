package me.j360.shiro.webclient.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.j360.shiro.appclient.core.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class BaseController {

    public static final int OK = 0;

    //注入request
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;

    /**
     * 获取请求的客户端及设备信息
     * Client-Agent: /3.1.0/1602/iOS/7.1/iPhone 5s (A1457/A1518/A1528/A1530)/7EAB70B1-624F-463A-943C-E7FF235A9A0C
     * 在传递参数的时候setOsNumber安卓=1，苹果=2
     */
    public ClientAgent getClientAgent() {
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

    /**
     * 获取用户的UID
     */
    public String getRequestUserId() {
        String uid = request.getHeader(Constants.REQUEST_HEADER_UID);
        return uid;
    }

    /**
     * 获取客户端IP
     */
    public String getRequestIP() {
        String remoteIp = request.getHeader("X-Real-IP");
        if (org.apache.commons.lang3.StringUtils.isBlank(remoteIp)) {
            String xff = request.getHeader("X-Forwarded-For");
            if (!org.apache.commons.lang3.StringUtils.isBlank(xff)) {
                remoteIp = xff.split(",")[0];
            }
        }
        return remoteIp;
    }

    //表达式：appName/version/buildVersion/osNumber/osVersion/deviceModel/deviceUUID
    public class ClientAgent {
        @Getter
        @Setter
        private String appName;
        @Getter
        @Setter
        private String version;
        @Getter
        @Setter
        private int buildVersion;
        @Getter
        @Setter
        private String osNumber;
        @Getter
        @Setter
        private String osVersion;
        @Getter
        @Setter
        private String deviceModel;
        @Getter
        @Setter
        private String deviceUUID;
    }


}
