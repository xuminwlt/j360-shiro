package me.j360.shiro.appclient.servlet.shiro.filter;

import me.j360.shiro.appclient.servlet.shiro.realm.StatelessWhiteListToken;
import me.j360.shiro.appclient.servlet.shiro.util.ShiroRequestUtil;
import me.j360.shiro.appclient.servlet.shiro.util.ShiroResponseUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 白名单的Filter
 */
public class StatelessWhiteListAuthcFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        StatelessWhiteListToken token = new StatelessWhiteListToken();
        //封装白名单的token
        token.setAppSign(ShiroRequestUtil.getRequestHeaderWhiteListSign((HttpServletRequest) request));
        token.setParams(ShiroRequestUtil.getRequestHeaderWhiteListMap((HttpServletRequest) request));
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
        } catch (Exception e) {
            String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
            onLoginFail(response,exceptionClassName);
            return false;
        }
        return true;
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response,String error) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String type = "application/json";
        String content = ShiroResponseUtil.getWhiteListFailResponse();
        try {
            httpResponse.setContentType(type + ";charset=UTF-8");
            httpResponse.setHeader("Pragma", "No-cache");
            httpResponse.setHeader("Cache-Control", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
            httpResponse.getWriter().write(content);
            httpResponse.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
