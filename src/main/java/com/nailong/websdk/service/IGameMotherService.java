package com.nailong.websdk.service;

import com.nailong.websdk.domain.AuthRequest;
import com.nailong.websdk.domain.vo.GameMotherAuthVo;

import java.security.NoSuchAlgorithmException;

/**
 * 从游戏母公司命名的路径获取用户
 */
public interface IGameMotherService {
    GameMotherAuthVo getAuth(AuthRequest body) throws NoSuchAlgorithmException;
}
