package com.nailong.websdk.service;

import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.HttpRsp;

import java.io.IOException;

public interface ICommonService {
    HttpRsp queryClientCode() throws IOException;

    HttpRsp queryClientConfig(AuthorizationDto authorizationDto) throws IOException;

    HttpRsp queryVersion() throws IOException;
}
