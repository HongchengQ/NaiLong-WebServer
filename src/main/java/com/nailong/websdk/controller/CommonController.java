package com.nailong.websdk.controller;

import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.HttpRsp;
import com.nailong.websdk.service.ICommonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/common", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class CommonController {

    private final ICommonService commonService;

    @RequestMapping(path = "/config")
    public HttpRsp config(HttpServletRequest handler) throws IOException {
        // authorization 来自于拦截器中添加的属性 - authInfo
        AuthorizationDto authorizationDto = (AuthorizationDto) handler.getAttribute("authInfo");
        return commonService.queryClientConfig(authorizationDto);
    }

    @RequestMapping(path = "/client-code")
    public HttpRsp clientCode() throws IOException {
        return commonService.queryClientCode();
    }

    @RequestMapping(path = "/version")
    public HttpRsp version() throws IOException {
        return commonService.queryVersion();
    }

    @RequestMapping(path = "/test")
    public String test(@RequestHeader("Authorization") AuthorizationDto ctxAuthorizationDto) {
        IO.println(ctxAuthorizationDto.getSign());
        return "test";
    }

}