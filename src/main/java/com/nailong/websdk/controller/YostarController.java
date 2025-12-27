package com.nailong.websdk.controller;

import com.nailong.websdk.pojo.HttpRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/yostar", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class YostarController {
        /*
        getApp().post("/yostar/get-auth", new GetAuthHandler());
        getApp().post("/yostar/send-code", new HttpJsonResponse("{\"Code\":200,\"Data\":{},\"Msg\":\"OK\"}")); // Dummy handler
     */

    @RequestMapping(path = "/send-code")
    public HttpRsp sendCode() {
        return HttpRsp.ok();
    }
}
