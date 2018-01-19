package easy.http.rpc.springmvc;

import com.alibaba.fastjson.JSON;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RpcExceptionReslover implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView view = new ModelAndView(new View() {
            @Override
            public String getContentType() {
                return "application/json";
            }

            @Override
            public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

                Object ex = map.get("ex");
                String exName = "";
                if (ex instanceof InvocationTargetException) {
                    exName = ((InvocationTargetException) ex).getTargetException().getClass().getName();
                } else {
                    exName = ex.getClass().getName();
                }
                httpServletResponse.setContentType("application/json;charset=utf-8");
                httpServletResponse.addHeader("explicitEx", exName);
                httpServletResponse.getWriter().write(JSON.toJSONString(ex));
                httpServletResponse.flushBuffer();

            }
        });

        view.addObject("ex", e);

        return view;
    }
}
