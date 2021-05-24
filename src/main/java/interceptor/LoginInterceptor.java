package interceptor;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String mobile=(String) request.getSession().getAttribute("mobile");
        if (StrUtil.isEmpty(mobile)){
             response.sendRedirect("/login.jsp");
            return false;
        }
        return true;
    }
}
