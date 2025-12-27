package com.nailong.websdk.controller;

import com.nailong.websdk.pojo.HttpRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class UserController {
    /*
        getApp().post("/user/detail", new UserLoginHandler());
        getApp().post("/user/set", new UserSetDataHandler());
        getApp().post("/user/set-info", new UserSetDataHandler()); // CN
        getApp().post("/user/login", new UserLoginHandler());
        getApp().post("/user/quick-login", new UserLoginHandler());
        getApp().post("/user/send-sms", new HttpJsonResponse("{\"Code\":200,\"Data\":{},\"Msg\":\"OK\"}"));
     */

    @RequestMapping(path = "/send-sms")
    public HttpRsp sendSms() {
        return HttpRsp.ok();
    }
}
