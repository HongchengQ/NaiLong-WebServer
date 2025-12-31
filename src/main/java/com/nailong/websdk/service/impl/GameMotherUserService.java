package com.nailong.websdk.service.impl;

import com.nailong.websdk.dao.UserRepository;
import com.nailong.websdk.model.dto.AuthDto;
import com.nailong.websdk.model.po.User;
import com.nailong.websdk.model.vo.GameMotherAuthVo;
import com.nailong.websdk.service.IGameMotherService;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class GameMotherUserService extends UserService implements IGameMotherService {

    public GameMotherUserService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public GameMotherAuthVo getAuth(AuthDto body) throws NoSuchAlgorithmException {
        User user = getOrCreateUserFromBody(body);

        if (user == null) return null;

        return GameMotherAuthVo.builder()
                .uid(user.openId())
                .token(generateLoginToken(user))
                .account(user.openId())
                .build();
    }

    private User getOrCreateUserFromBody(AuthDto body) {
        String openId = body.getAccount();

        User user = userRepository.queryUserByOpenId(openId);

        if (user == null) {
            // 创建账号
            user = userRepository.createUser(openId, null, null);
        }

        return user;
    }
}
