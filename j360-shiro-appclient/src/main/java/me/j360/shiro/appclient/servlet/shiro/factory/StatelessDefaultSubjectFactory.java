package me.j360.shiro.appclient.servlet.shiro.factory;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.factory
 * User: min_xu
 * Date: 16/4/18 下午7:27
 * 说明：Subject创建工厂，此处创建无session的Subject
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
    public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
