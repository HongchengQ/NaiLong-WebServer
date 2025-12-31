package com.nailong.websdk.service.impl;

import com.nailong.websdk.dao.UserRepository;
import com.nailong.websdk.model.dto.AuthorizationDto;
import com.nailong.websdk.model.dto.LoginBodyDto;
import com.nailong.websdk.model.dto.UserSetDataDto;
import com.nailong.websdk.model.po.User;
import com.nailong.websdk.model.vo.UserVo;
import com.nailong.websdk.service.IUserService;
import com.nailong.websdk.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    protected final UserRepository userRepository;

    @Override
    public UserVo<Object> getOrCreateUserResult(LoginBodyDto body) throws NoSuchAlgorithmException {
        User user = getAccountFromHeader(body.getAuthorizationDto());

        if (user == null) {
            user = this.getOrCreateUserFromBody(body);
        }

        if (user == null) {
            return null;
        }

        // 返回 user 数据
        String pidName = body.getAuthorizationDto().getHead().getPid();
        return result(pidName, user);
    }


    /**
     * header 里的 token 是真的 token
     *
     * @param head
     * @return
     */
    @Override
    public User getAccountFromHeader(AuthorizationDto head) {
        if (head == null) {
            return null;
        }

        String token = head.getHead().getToken();
        if (token == null) {
            return null;
        }

        // Get account
        return userRepository.queryUserByLoginToken(token);
    }

    @Override
    public int getSetInfoRetCode(AuthorizationDto authorizationDto, UserSetDataDto body) {
        User user = getAccountFromHeader(authorizationDto);
        if (user == null) return 100403; // TOKEN_AUTH_FAILED

        String key = body.getKey();
        String value = body.getValue();

        if (key == null || body.getValue() == null) {
            return 100110; //VALID_FAIL
        }

        String token = authorizationDto.getHead().getToken();
        Long uid = authorizationDto.getHead().getUid();
        if (uid == null || uid == 0) {
            try {
                uid = userRepository.queryUserByLoginToken(token).id();
            } catch (Exception e) {
                log.warn("尝试使用 token 查找用户时发生异常 - {} ", token, e);
                return 100404;
            }
        }

        if (key.equals("Nickname") || key.equals("nickname")) {
            userRepository.updateUserName(uid, value);
        }

        return 0;
    }

    /**
     * body 里的 token 是验证码
     *
     * @param loginBodyDto
     * @return
     */
    private User getOrCreateUserFromBody(LoginBodyDto loginBodyDto) throws NoSuchAlgorithmException {
        User user = userRepository.queryUserByLoginToken(loginBodyDto.getToken());

        if (user != null) {
            return user;
        }

        String openId = loginBodyDto.getOpenId();
        user = userRepository.queryUserByOpenId(openId);
        if (user == null) {
            // 创建账号
            user = userRepository.createUser(openId, null, UserUtils.createSessionKey(loginBodyDto));
        }

        return user;
    }

    protected String generateLoginToken(Long userId) throws NoSuchAlgorithmException {
        String loginToken = UserUtils.createSessionKey(String.valueOf(userId));
        userRepository.updateToken(userId, loginToken);
        return loginToken;
    }

    protected String generateLoginToken(User user) throws NoSuchAlgorithmException {
        String loginToken = UserUtils.createSessionKey(String.valueOf(user));
        userRepository.updateToken(user, loginToken);
        return loginToken;
    }

    private UserVo<Object> result(String pidName, User user) throws NoSuchAlgorithmException {
        // 关于 id：cn用num os用str
        long userIdNum = user.id();
        String userIdStr = String.valueOf(userIdNum);
        String userOpenId = user.openId();
        String userName = user.nickName();
        String userToken = user.loginToken();
        // userCreatedAt
        Long userCreatedTime = user.createdTime();
        long userCreatedAt = userCreatedTime == null ? 0 : userCreatedTime;

        if (userToken == null) {
            userToken = generateLoginToken(user);
        }

        UserVo<Object> userVo = new UserVo<>();
        userVo.setNew(false);
        userVo.setTestAccount(false);
        userVo.setYostar( // 共有参数
                UserVo.LoginYostarJson.builder()
                        .id(userIdNum)
                        .nickName(userName)
                        .picture("")
                        .state(1)
                        .createdAt(userCreatedAt)
                        .build());


        if (pidName.equals("CN-NOVA")) {
            userVo.getYostar().setId(userIdNum);
            userVo.getYostar().setDefaultNickName("");

            userVo.setUser(
                    UserVo.UserJson.builder()
                            .id(userIdNum)
                            .pid(pidName)
                            .token(userToken)
                            .state(1)
                            .regChannel("pc_official")
                            .destroyState(0)
                            .build());
            userVo.setIdentity(
                    UserVo.IdentityJson.builder()
                            .type(0)
                            .realName("***")
                            .idCard("******************")
                            .underage(false)
                            .pi("")
                            .birthDate("")
                            .state(1)
                            .build());
            userVo.setKeys(List.of(
                    UserVo.UserKeyJson.builder()
                            .type("mobile")
                            .nickName(userName)
                            .nickNameEnc("")
                            .build()
            ));
            userVo.setDestroy(
                    UserVo.DestroyJson.builder()
                            .destroyAt(0)
                            .build());
            userVo.setYostarDestroy(
                    UserVo.YostarDestroyJson.builder()
                            .destroyAt(0)
                            .build());
        } else {
            userVo.getYostar().setId(userIdStr);
            userVo.getYostar().setAgreeAd(0);

            userVo.setUserInfo(UserVo.UserInfoJson.builder()
                    .id(userIdStr)
                    .uid2(0)
                    .pid(pidName)
                    .token(userToken)
                    .birthday("")
                    .regChannel("pc")
                    .transCode("")
                    .state(1)
                    .deviceID("")
                    .createdAt(userCreatedAt)
                    .build());
            userVo.setKeys(List.of(
                    UserVo.UserKeyJson.builder()
                            .id(userIdStr)
                            .type("yostar")
                            .key(userOpenId)
                            .nickName(userName)
                            .createdAt(userCreatedAt)
                            .build()
            ));
        }

        return userVo;
    }
}
