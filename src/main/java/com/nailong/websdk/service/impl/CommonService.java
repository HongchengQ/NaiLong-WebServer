package com.nailong.websdk.service.impl;

import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.HttpRsp;
import com.nailong.websdk.service.ICommonService;
import com.nailong.websdk.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CommonService implements ICommonService {
    @Override
    public HttpRsp queryClientCode() throws IOException {
        return FileUtils.readClientCodeFile();
    }

    @Override
    public HttpRsp queryClientConfig(AuthorizationDto authorizationDto) throws IOException {
        AuthorizationDto.Head head = authorizationDto.getHead();
        String region;

        if (head.getPid().equals("CN-NOVA")) {
            if (head.getChannel().equals("bilibili")) {
                region = "bili";
                return FileUtils.readClientConfigFile(region);
            }
            region = "cn";
        } else {
            region = "os";
        }

        return FileUtils.readClientConfigFile(region);
    }

    @Override
    public HttpRsp queryVersion() throws IOException {
        return FileUtils.readClientCommonVersion();
    }
}
