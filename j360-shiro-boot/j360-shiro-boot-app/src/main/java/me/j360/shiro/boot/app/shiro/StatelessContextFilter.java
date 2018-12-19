package me.j360.shiro.boot.app.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * 用于首先绑定用户的Context,绑定失败抛出异常
 */

@Slf4j
public class StatelessContextFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //设置该请求为无状态请求
        request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);

        return true;
    }


    /**
     *
     */
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {

    }
}
