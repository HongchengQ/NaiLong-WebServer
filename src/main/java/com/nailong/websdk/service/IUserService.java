package com.nailong.websdk.service;

import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.dto.LoginBodyDto;
import com.nailong.websdk.model.dto.UserSetDataDto;
import com.nailong.websdk.model.po.User;
import com.nailong.websdk.model.vo.UserVo;

import java.security.NoSuchAlgorithmException;

public interface IUserService {

    User getAccountFromHeader(AuthorizationDto authorizationDto);

    UserVo<Object> getOrCreateUserResult(LoginBodyDto body) throws NoSuchAlgorithmException;

    int getSetInfoRetCode(AuthorizationDto authorizationDto, UserSetDataDto body);
}
