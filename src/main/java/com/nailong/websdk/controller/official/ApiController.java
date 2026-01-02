package com.nailong.websdk.controller.official;

import com.nailong.websdk.config.AppProperties;
import com.nailong.websdk.model.HttpRsp;
import com.nailong.websdk.model.vo.OpenIpVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class ApiController {
    private final AppProperties appProperties;

    /**
     * 反馈客服
     * @return
     */
    @RequestMapping(path = "/open/ip")
    public HttpRsp openIp() {
        OpenIpVo openIpVo = new OpenIpVo(appProperties.getOpenIpUrl());

        return HttpRsp.ok(openIpVo);
    }
}
