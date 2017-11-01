package easy.http.rpc.springmvc;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SpringMvcApiConfig extends WebMvcConfigurerAdapter {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        HttpMessageConverter jackson2Convert = null;
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter) converter;

                List<MediaType> mediaTypes = new ArrayList<>();
                mediaTypes.add(MediaType.TEXT_PLAIN);
                stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
            }
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                jackson2Convert = converter;
            }
        }

        if (jackson2Convert != null) {
            converters.remove(jackson2Convert);
            converters.add(0, new FastJsonHttpMessageConverter());
        }


        super.extendMessageConverters(converters);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        argumentResolvers.add(new JSONArrayHandlerMethodArgumentResolver());

        super.addArgumentResolvers(argumentResolvers);
    }
}
