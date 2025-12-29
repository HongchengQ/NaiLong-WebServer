package com.nailong.websdk.service;

import com.nailong.websdk.domain.Authorization;
import com.nailong.websdk.domain.LoginBody;
import com.nailong.websdk.domain.UserSetDataRequest;
import com.nailong.websdk.domain.po.User;
import com.nailong.websdk.domain.vo.UserVo;

import java.security.NoSuchAlgorithmException;

public interface IUserService {

    User getAccountFromHeader(Authorization authorization);

    UserVo<Object> getOrCreateUserResult(LoginBody body) throws NoSuchAlgorithmException;

    int getSetInfoRetCode(Authorization authorization, UserSetDataRequest body);
}
