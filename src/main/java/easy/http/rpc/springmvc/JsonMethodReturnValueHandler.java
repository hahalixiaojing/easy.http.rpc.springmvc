package easy.http.rpc.springmvc;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletResponse;

public class JsonMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {

        return methodParameter.hasMethodAnnotation(FastJsonResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {

        modelAndViewContainer.setRequestHandled(true);

        ServletResponse response = nativeWebRequest.getNativeResponse(ServletResponse.class);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String value = JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss");
        byte[] bytes = value.getBytes("utf-8");

        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }
}

