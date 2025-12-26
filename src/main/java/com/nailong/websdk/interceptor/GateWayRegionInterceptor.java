package com.nailong.websdk.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class GateWayRegionInterceptor implements HandlerInterceptor {

    /**
     * 当网关为请求头附加区域信息时，将信息放入request attribute；假如没有信息则改为默认区域
     * <p>
     * 无论发生什么都一律放行
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return true
     */
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        String region = request.getHeader("X-Region");

        if (region == null) {
            region = "cn";
        }

        request.setAttribute("X-Region", region);

        return true;
    }
}
