package com.nailong.websdk.controller.official;

import com.nailong.websdk.model.HttpRsp;
import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.service.ICommonService;
import com.nailong.websdk.utils.AeadHelper;
import com.nailong.websdk.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping(value = "/common", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class CommonController {

    private final ICommonService commonService;

    @RequestMapping(path = "/config")
    public HttpRsp config(@RequestHeader("Authorization") String authHeader) throws IOException {
        // authorization 来自于拦截器中添加的属性 - authInfo
        return commonService.queryClientConfig(AuthorizationDto.parseStrToObject(authHeader));
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
    public byte[] test(@RequestHeader("Authorization") String authHeader) throws Exception {
        String sign = AuthorizationDto.parseStrToObject(authHeader).getSign();
        byte[] signBytes = Base64.getDecoder().decode(sign);

        IO.println(Utils.bytesToHex(signBytes));

        return AeadHelper.decryptCBC(signBytes, "cn");
    }

}