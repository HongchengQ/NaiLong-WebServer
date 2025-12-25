package com.nailong.websdk.service;

import com.nailong.websdk.pojo.Authorization;
import com.nailong.websdk.pojo.HttpRsp;

import java.io.IOException;

public interface ICommonService {
    HttpRsp queryClientCode() throws IOException;

    HttpRsp queryClientConfig(Authorization authorization) throws IOException;

    HttpRsp queryVersion() throws IOException;
}
