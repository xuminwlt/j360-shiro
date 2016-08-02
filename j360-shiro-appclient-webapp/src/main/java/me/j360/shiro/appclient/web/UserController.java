package me.j360.shiro.appclient.web;

import me.j360.shiro.appclient.web.request.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Package: me.j360.shiro.appclient.web
 * User: min_xu
 * Date: 16/8/2 下午2:33
 * 说明：shiro用例场景的DEMO
 */

@Controller
@RequestMapping("/api/user")
public class UserController extends BaseController{


    /**
     * 匿名接口，所有人可访问，
     *
     * @return
     */
    @RequestMapping(value = "/api-version", method = RequestMethod.GET)
    @ResponseBody
    public Response apiVersion() {

        return null;
    }

    /**
     * 用户的登录，白名单权限
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@Validated LoginRequest request) {


        return null;
    }

    /**
     * 获取用户名称，用户会话权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/name", method = RequestMethod.GET)
    @ResponseBody
    public Response getName(@PathVariable long id) {

        return null;
    }

    /**
     * 删除用户，用户会话权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Response deleteUser(@PathVariable long id) {

        return null;
    }
}
