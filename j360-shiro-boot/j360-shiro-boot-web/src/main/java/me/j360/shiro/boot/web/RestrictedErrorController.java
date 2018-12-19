package me.j360.shiro.boot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 */

@Slf4j
@ControllerAdvice
@Controller
public class RestrictedErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ResponseBody
    @RequestMapping(ERROR_PATH)
    Map<String, Object> error(HttpServletRequest request, Model model) {
        log.warn(ERROR_PATH + "->");
        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(new ServletWebRequest(request), false);
        model.addAttribute("errors", errorMap);
        return errorMap;
    }
}
