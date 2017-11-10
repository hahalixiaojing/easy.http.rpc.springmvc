package easy.http.rpc.springmvc;

import com.alibaba.fastjson.JSON;
import easy.http.rpc.JSONStringToObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Controller
@RequestMapping("/interfaceApi")
public class InterfaceApiController {

    @Autowired
    private ApplicationContext context;

    @PostMapping("/execute/{servcie}/{methodName}")
    @ResponseBody
    public Object execute(@PathVariable("servcie") String service, @PathVariable("methodName") String methodName, String args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        Class<?> aClass = Class.forName(service);

        Object bean = this.context.getBean(aClass);
        if (bean == null) {
            return "bean not find";
        }

        Method exeMethod = null;

        for (Method m : aClass.getMethods()) {
            if (m.getName().equals(methodName)) {
                exeMethod = m;
            }
        }
        if (exeMethod == null) {
            return "method not find";
        }

        List<String> strings = JSON.parseArray(args, String.class);

        Object[] params = new Object[exeMethod.getParameterTypes().length];

        for (int i = 0; i < exeMethod.getParameterTypes().length; i++) {
            Object o = JSONStringToObject.methodParameterDataToObject(exeMethod, i, strings.get(0));
            params[i] = o;
        }

        return exeMethod.invoke(bean, params);
    }
}
