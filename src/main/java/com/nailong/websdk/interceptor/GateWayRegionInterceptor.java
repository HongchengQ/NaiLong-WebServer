package com.nailong.websdk.interceptor;

import com.nailong.websdk.config.AppProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.nailong.websdk.enums.ServletAttributeEnum.REGION;

@Log4j2
@RequiredArgsConstructor
public class GateWayRegionInterceptor implements HandlerInterceptor {

    AppProperties appProperties;

    /**
     * 当网关为请求头附加区域信息时，将信息放入request attribute；假如没有信息则改为默认区域
     * <p>
     * 无论发生什么都一律放行
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return true
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        String headerRegion = request.getHeader(REGION.getStr());

        // 请求头区域为空时
        if (ObjectUtils.isEmpty(headerRegion)) {
            // 先从配置文件找默认区域
            if (!ObjectUtils.isEmpty(appProperties.getDefaultRegion())) {
                headerRegion = appProperties.getDefaultRegion();
            } else {
                headerRegion = "cn";
            }

            log.warn("未找到网关附加的区域头，针对这个请求已使用默认区域：{} -> {}", headerRegion, request.getServletPath());
        }

        // 在请求中存储属性
        // K: X-Region V: cn/tw/...
        request.setAttribute(REGION.getStr(), headerRegion);

        return true;
    }
}
