package com.nailong.websdk.controller.official;

import com.nailong.websdk.model.HttpRsp;
import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.dto.LoginBodyDto;
import com.nailong.websdk.model.dto.UserSetDataBodyDto;
import com.nailong.websdk.model.vo.UserVo;
import com.nailong.websdk.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @RequestMapping(path = {"/login", "/quick-login", "/detail"})
    public HttpRsp login(
            @RequestHeader("Authorization") String authHeader,
            @Nullable @RequestBody LoginBodyDto body
    ) throws NoSuchAlgorithmException {
        AuthorizationDto authorizationDto = AuthorizationDto.parseStrToObject(authHeader);

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
    public HttpRsp infoSet(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserSetDataBodyDto body
    ) {
        AuthorizationDto authorizationDto = AuthorizationDto.parseStrToObject(authHeader);

        int retCode = userService.getSetInfoRetCode(authorizationDto, body);

        if (retCode != 0) {
            return HttpRsp.error(retCode, "Error");
        }

        return HttpRsp.ok();
    }

    @RequestMapping(path = "/test")
    public Object test(@RequestHeader("Authorization") String authHeader) {
        return AuthorizationDto.parseStrToObject(authHeader);
    }
}
