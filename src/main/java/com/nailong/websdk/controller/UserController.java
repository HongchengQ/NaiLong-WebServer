package com.nailong.websdk.controller;

import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.HttpRsp;
import com.nailong.websdk.model.dto.LoginBodyDto;
import com.nailong.websdk.model.dto.UserSetDataBodyDto;
import com.nailong.websdk.model.vo.UserVo;
import com.nailong.websdk.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import static com.nailong.websdk.enums.ServletAttributeEnum.AUTH_INFO;

@RestController
@RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @RequestMapping(path = {"/login", "/quick-login", "/detail"})
    public HttpRsp login(HttpServletRequest handler, @Nullable @RequestBody LoginBodyDto body) throws NoSuchAlgorithmException {
        // authorization 来自于拦截器中添加的属性
        AuthorizationDto authorizationDto = (AuthorizationDto) handler.getAttribute(AUTH_INFO.getStr());

        // 将认证信息移交给 LoginBody
        if (body != null) {
            body.setAuthorizationDto(authorizationDto);
        }

        UserVo<Object> userVo = userService.getOrCreateUserResult(body);
        if (userVo == null) {
            return HttpRsp.error(100403, "Error");
        }

        return HttpRsp.ok(userVo);
    }

    @RequestMapping(path = "/send-sms")
    public HttpRsp sendSms() {
        return HttpRsp.ok();
    }

    @RequestMapping(path = {"/set", "/set-info"})
    public HttpRsp infoSet(HttpServletRequest handler, @RequestBody UserSetDataBodyDto body) {
        // authorization 来自于拦截器中添加的属性 - authInfo
        AuthorizationDto authorizationDto = (AuthorizationDto) handler.getAttribute(AUTH_INFO.getStr());

        int retCode = userService.getSetInfoRetCode(authorizationDto, body);

        if (retCode != 0) {
            return HttpRsp.error(retCode, "Error");
        }

        return HttpRsp.ok();
    }
}
