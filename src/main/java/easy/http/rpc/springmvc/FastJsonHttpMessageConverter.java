package easy.http.rpc.springmvc;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FastJsonHttpMessageConverter implements HttpMessageConverter<Object> {

    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        list.add(MediaType.APPLICATION_JSON);
        return list;
    }

    @Override
    public Object read(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return new Object();
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {

        httpOutputMessage.getHeaders().add("content-type", "applicaton/json;charset=utf-8");
        byte[] bytes = JSON.toJSONString(o).getBytes(Charset.forName("utf-8"));
        httpOutputMessage.getBody().write(bytes);
    }
}

