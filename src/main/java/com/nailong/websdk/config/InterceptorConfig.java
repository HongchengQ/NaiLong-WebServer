package com.nailong.websdk.config;

import com.nailong.websdk.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 认证拦截器 - 参数完整性校验，并将解析后的对象放入request attribute
        registry
                .addInterceptor(new AuthInterceptor())
                .addPathPatterns("/user/**", "/common/**")
                .excludePathPatterns("/meta/**", "/res/**")
                .order(1);
    }
}