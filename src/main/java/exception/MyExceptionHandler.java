package exception;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyExceptionHandler implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Map<String,Object> model=new HashMap<String,Object>();
        model.put("ex",ex);
        ex.printStackTrace();
        if (ex instanceof SeckillException){
            return new ModelAndView("error",model);
        }
        return null;
    }
}
