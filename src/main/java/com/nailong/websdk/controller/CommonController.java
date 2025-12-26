package com.nailong.websdk.controller;

import com.nailong.websdk.pojo.Authorization;
import com.nailong.websdk.pojo.HttpRsp;
import com.nailong.websdk.service.ICommonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/common", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
@Log4j2
public class CommonController {
    /*
    // https://en-sdk-api.yostarplat.com/
    getApp().post("/common/config", new CommonConfigHandler(this));
    getApp().post("/common/client-code", new CommonClientCodeHandler());
    getApp().post("/common/version", new HttpJsonResponse("{\"Code\":200,\"Data\":{\"Agreement\":[{\"Version\":\"0.1\",\"Type\":\"user_agreement\",\"Title\":\"用户协议\",\"Content\":\"\",\"Lang\":\"en\"},{\"Version\":\"0.1\",\"Type\":\"privacy_agreement\",\"Title\":\"隐私政策\",\"Content\":\"\",\"Lang\":\"en\"}],\"ErrorCode\":\"4.4\"},\"Msg\":\"OK\"}"));
    */

    private final ICommonService commonService;

    @RequestMapping(path = "/config")
    public HttpRsp config(HttpServletRequest handler) throws IOException {
        // authorization 来自于拦截器中添加的属性 - authInfo
        Authorization authorization = (Authorization) handler.getAttribute("authInfo");
        return commonService.queryClientConfig(authorization);
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
    public String test(@RequestHeader("Authorization") Authorization ctxAuthorization) {
        IO.println(ctxAuthorization.getSign());
        return "test";
    }

}