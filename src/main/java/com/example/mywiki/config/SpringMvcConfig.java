package com.example.mywiki.config;

import com.example.mywiki.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LogInterceptor logInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        //注册一个拦截器并设置拦截接口（范围
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**");
    }
}
