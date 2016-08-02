package me.j360.shiro.appclient.servlet.shiro.filter;

import me.j360.shiro.appclient.servlet.shiro.realm.StatelessAuthcToken;
import me.j360.shiro.appclient.servlet.shiro.util.ShiroRequestUtil;
import me.j360.shiro.appclient.servlet.shiro.util.ShiroResponseUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户会话的Filter
 */
public class StatelessAuthcFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //强制为否，必须经过验证
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        StatelessAuthcToken token = new StatelessAuthcToken();
        token.setUid(ShiroRequestUtil.getRequestHeaderUid((HttpServletRequest) request));
        token.setParams(ShiroRequestUtil.getRequestHeaderUserMap((HttpServletRequest) request));
        token.setToken(ShiroRequestUtil.getRequestHeaderUserSign((HttpServletRequest) request));

        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
        } catch (Exception e) {
            String error = null;
            if( e instanceof UnknownAccountException ){
                error = "用户名/密码错误";
            }else if(e instanceof IncorrectCredentialsException){
                error = "会话过期，请重新登录";
            }
            String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
            if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
                error = "用户名/密码错误";
            }
            onLoginFail(response,error);
            return false;
        }
        return true;
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response,String error) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String type = "application/json";
        String content = ShiroResponseUtil.getAuthcFailResponse();
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
