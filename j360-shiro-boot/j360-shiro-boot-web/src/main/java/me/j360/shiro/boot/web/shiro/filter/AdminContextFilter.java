package me.j360.shiro.boot.web.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Objects;


/**
 * 用于首先绑定用户的Context,绑定失败抛出异常
 */

@Slf4j
public class AdminContextFilter extends PathMatchingFilter {


    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        System.out.println("----->" + subject.getPrincipal() + "<----");
        if (Objects.nonNull(subject.getPrincipal())) {
            //UserDto user = CurrentContext.getCurrentUser();
            //UserContext.setCurrentUid(user.getId());

//            if (user != null) {
//                MDC.put("userId", user.getId().toString());
//                MDC.put("userName", user.getUsername());
//            } else {
//                MDC.put("userId", "");
//                MDC.put("userName", "");
//            }
        }
        return true;
    }


    /**
     *
     */
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        //UserContext.clear();
        //UserContext.clearClientAgent();
    }
}
