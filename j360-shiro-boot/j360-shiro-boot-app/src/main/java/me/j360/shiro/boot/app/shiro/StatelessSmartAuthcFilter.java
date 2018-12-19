package me.j360.shiro.boot.app.shiro;

import lombok.extern.slf4j.Slf4j;
import me.j360.shiro.boot.JwtToken;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 智能识别的的Filter
 * 根据是否有相关标示兼容不同的role身份
 * app下兼容需要登录和非登录的页面验证,登录确认需要增加一个Filter作为配合
 *
 * /page1/toall = smart
 * /page1/tologinuser = smart, user
 * /page1/toguest = smart
 */
@Slf4j
public class StatelessSmartAuthcFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //强制为否，必须经过验证
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        JwtToken token = new JwtToken();
        return true;
    }

}
