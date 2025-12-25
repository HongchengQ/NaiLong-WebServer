package com.nailong.websdk.interceptor;

import com.nailong.websdk.pojo.Authorization;
import com.nailong.websdk.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");

        try {
            // 解析并验证
            Authorization auth = JsonUtils.parseJsonToObject(authHeader, Authorization.class);
            if (!verifyAuth(auth)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"code\":401,\"message\":\"Invalid Authorization\"}");
                return false;
            }

            // 将解析后的对象放入request attribute
            request.setAttribute("authInfo", auth);
            return true;

        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("{\"code\":400,\"message\":\"Invalid Authorization format\"}");
            return false;
        }
    }

    private boolean verifyAuth(Authorization auth) {
        return auth != null;
    }
}