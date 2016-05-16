package me.j360.shiro.appclient.servlet.shiro.filter;

import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 验证码接口在单台设备上发送次数限制，需要根据Header中传入的客户端唯一标识
 */
public class StatelessCaptchaLimitFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

}
