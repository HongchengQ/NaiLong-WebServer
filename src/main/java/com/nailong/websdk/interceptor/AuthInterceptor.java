package com.nailong.websdk.interceptor;

import com.nailong.websdk.exception.AuthorizationHeadException;
import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.nailong.websdk.enums.ServletAttributeEnum.AUTH_INFO;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        String authHeader = request.getHeader(AUTH_INFO.getStr());

        // 解析并验证
        AuthorizationDto auth = JsonUtils.parseJsonStrToObject(authHeader, AuthorizationDto.class);
        if (auth == null) {
            throw new AuthorizationHeadException("参数有误:Authorization不能为空");
        }

        // 将解析后的对象放入request attribute
        request.setAttribute(AUTH_INFO.getStr(), auth);
        return true;
    }
}