package com.nailong.websdk.config;

import com.nailong.websdk.interceptor.AuthInterceptor;
import com.nailong.websdk.interceptor.GateWayRegionInterceptor;
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

        // 网关区域拦截器 - 获取网关添加的区域头，并将区域头放入request attribute
        registry
                .addInterceptor(new GateWayRegionInterceptor())
                .addPathPatterns("/meta/**", "/res/**")
                .excludePathPatterns("/user/**", "/common/**")
                .order(2);
    }
}