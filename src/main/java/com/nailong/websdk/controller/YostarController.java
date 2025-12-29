package com.nailong.websdk.controller;

import com.nailong.websdk.domain.AuthRequest;
import com.nailong.websdk.domain.HttpRsp;
import com.nailong.websdk.domain.vo.GameMotherAuthVo;
import com.nailong.websdk.service.IGameMotherService;
import com.nailong.websdk.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/yostar", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class YostarController {

    private final IGameMotherService gameMotherService;

    @RequestMapping(path = "/get-auth")
    public HttpRsp getAuth(@Validated @RequestBody AuthRequest body) throws NoSuchAlgorithmException {
        GameMotherAuthVo AuthVo = gameMotherService.getAuth(body);
        if (AuthVo == null) {
            return HttpRsp.error(100403, "Error");
        }

        return HttpRsp.ok(AuthVo);
    }

    @RequestMapping(path = "/send-code")
    public HttpRsp sendCode() {
        return HttpRsp.ok();
    }
}
