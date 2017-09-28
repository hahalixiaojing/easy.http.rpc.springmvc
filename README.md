服务端springmvc配置方法
====
1. 引入easy.http.rpc.springmvc.RpcExceptionReslover 异常解析器
```java
@SpringBootApplication
@Import(RpcExceptionReslover.class)
public class ApiserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiserverApplication.class, args);
    }
}

```
2. 定义springmvc Controller
> Controller类上的@RequestMapping("<接口类全名>")
>
> Controller类方法上加上@JSONArguments、@ResponseBody、@PostMapping,其中@PostMapping是接口方法名称
```java
package com.my.api.apiserver.controller;

import com.my.api.apiserver.api.IUserService;
import com.my.api.apiserver.api.model.MyEx;
import com.my.api.apiserver.api.model.Result;
import com.my.api.apiserver.api.model.UserInfo;
import easy.http.rpc.springmvc.JSONArguments;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("com.my.api.apiserver.api.IUserService")
public class UserController implements IUserService {

    @JSONArguments
    @Override
    @ResponseBody
    @PostMapping("/addUser")
    public void addUser(String username) {
        System.out.println(username);
    }

    @JSONArguments
    @Override
    @ResponseBody
    @PostMapping("/ex")
    public String ex() {
        Integer i = null;
        return i.toString();
    }

    @JSONArguments
    @Override
    @ResponseBody
    @PostMapping("/myEx")
    public String myEx() {
        throw new MyEx("测试自己定义的异常");
    }

    @JSONArguments
    @Override
    @ResponseBody
    @PostMapping("/resultData")
    public Result<UserInfo> resultData() {
        UserInfo u = new UserInfo();
        u.setName("李四");
        u.setId(1);

        Result<UserInfo> userInfoResult = new Result<>();
        userInfoResult.setData(u);
        return userInfoResult;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/getArrayCount")
    public int getArrayCount(int[] arrays) {
        return arrays.length;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/arrayUser")
    public UserInfo[] arrayUser() {
        List<UserInfo> userInfos = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(100);
        userInfo.setName("张三");
        userInfos.add(userInfo);

        return userInfos.toArray(new UserInfo[1]);
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/allUsers")
    public List<String> allUsers() {
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        return list;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/allTrues")
    public List<Boolean> allTrues(List<Boolean> booleans) {
        List<Boolean> arrayList = new ArrayList<>();
        arrayList.add(true);
        arrayList.add(true);
        arrayList.add(true);
        arrayList.addAll(booleans);
        return arrayList;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/userIds")
    public List<Integer> userIds() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        return list;

    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/bidec")
    public List<BigDecimal> bidec() {
        List<BigDecimal> list = new ArrayList<>();

        list.add(new BigDecimal("23.569"));
        return list;

    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/getAllUsers")
    public List<UserInfo> getAllUsers() {
        List<UserInfo> userInfos = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(100);
        userInfo.setName("张三");
        userInfos.add(userInfo);

        return userInfos;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/getObjMaps")
    public Map<Integer, UserInfo> getObjMaps() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(100);
        userInfo.setName("张三");

        Map<Integer, UserInfo> map = new HashMap<>();
        map.put(1, userInfo);

        return map;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/getIntegerMaps")
    public Map<String, Integer> getIntegerMaps() {
        Map<String, Integer> hash = new HashMap<>();
        hash.put("a", 1);
        hash.put("b", 2);
        return hash;
    }

    @JSONArguments
    @ResponseBody
    @Override
    @PostMapping("/checkUserNameIsUsed")
    public boolean checkUserNameIsUsed(String name) {
        return true;
    }


}

```

3.扩展SpringMvcConfig
```java
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        for (HttpMessageConverter converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter) converter;

                List<MediaType> mediaTypes = new ArrayList<>();
                mediaTypes.add(MediaType.TEXT_PLAIN);
                stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
            }
        }

        super.extendMessageConverters(converters);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        argumentResolvers.add(new JSONArrayHandlerMethodArgumentResolver());

        super.addArgumentResolvers(argumentResolvers);
    }
}
```
