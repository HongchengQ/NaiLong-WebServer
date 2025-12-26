package com.nailong.websdk.service.impl;

import com.nailong.websdk.pojo.Authorization;
import com.nailong.websdk.pojo.HttpRsp;
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
    public HttpRsp queryClientConfig(Authorization authorization) throws IOException {
        Authorization.Head head = authorization.getHead();
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
