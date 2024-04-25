package com.hws.manager.config;

import com.hws.manager.interceptor.LoginAuthInterceptor;
import com.hws.manager.properties.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor;

    @Autowired
    private UserProperties userProperties;

    //实现跨域
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*") ;                // 允许所有的请求头
    }
    //TODO 拦截器的注册
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                .addPathPatterns("/**")      //要拦截的接口
//                .excludePathPatterns("/admin/system/index/login" ,    //不拦截的接口：登录、验证码生成
//                        "/admin/system/index/generateValidateCode");
                .excludePathPatterns(userProperties.getNoAuthUrls());
        //TODO 优化：这里路径是固定的，可以通过设置配置文件让他来读取--application.yml  --注入配置类
    }
}
