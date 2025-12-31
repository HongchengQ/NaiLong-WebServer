package com.nailong.websdk.service;

import com.nailong.websdk.model.dto.AuthDto;
import com.nailong.websdk.model.vo.GameMotherAuthVo;

import java.security.NoSuchAlgorithmException;

/**
 * 从游戏母公司命名的路径获取用户
 */
public interface IGameMotherService {
    GameMotherAuthVo getAuth(AuthDto body) throws NoSuchAlgorithmException;
}
