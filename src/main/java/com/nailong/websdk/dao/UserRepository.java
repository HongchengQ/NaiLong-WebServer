package com.nailong.websdk.dao;

import com.nailong.websdk.config.AppProperties;
import com.nailong.websdk.domain.dto.UserInput;
import com.nailong.websdk.domain.po.AccountMongoPo;
import com.nailong.websdk.domain.po.Tables;
import com.nailong.websdk.domain.po.User;
import com.nailong.websdk.domain.po.UserTable;
import com.nailong.websdk.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Repository
@Log4j2
public class UserRepository {

    private final JSqlClient sqlClient;
    private final AppProperties appProperties;
    private final HttpClientUtil httpClientUtil;

    UserTable userTable = Tables.USER_TABLE;

    private void sendUserToMiddleware(User user) {
        String commandServer = appProperties.getNebulaCommandServer();
        String authToken = appProperties.getNebulaCommandServerAuthToken();

        if (ObjectUtils.isEmpty(commandServer))
            return;
        if (ObjectUtils.isEmpty(authToken))
            return;

        Thread.startVirtualThread(() -> {
            try {
                AccountMongoPo accountMongoPo = new AccountMongoPo(user);
                // 发送命令 - 需要对方服务器有插件
                httpClientUtil.sendCommandToServer(commandServer, authToken, "dbaccount", accountMongoPo);
            } catch (Exception e) {
                log.error("Failed to send user object to middleware", e);
            }
        });
    }

    public User createUser(String openId, String password, String token) {
        User user = queryUserByOpenId(openId);
        if (user != null) {
            log.warn("无法创建 User 对象，因为它已经存在过了  id: {}", openId);
            return null;
        }

        user = new UserInput.Builder()
                .openId(openId)
                .password(password)
                .loginToken(token)
                .build()
                .toEntity();

        return saveUserObj(user);
    }

    public User saveUserObj(User user) {
        User savedUser = sqlClient
                .getEntities()
                .save(user)
                .getModifiedEntity();
        sendUserToMiddleware(savedUser);
        return savedUser;
    }

    public void updateUserName(Long userId, String name) {
        sqlClient
                .createUpdate(userTable)
                .where(userTable.id().eq(userId))
                .set(userTable.nickName(), name)
                .execute();
        queryUserByOpenId(queryUserById(userId).openId());
    }

    public void updateToken(Long id, String loginToken) {
        sqlClient
                .createUpdate(userTable)
                .where(userTable.id().eq(id))
                .set(userTable.loginToken(), loginToken)
                .execute();
        queryUserById(id);
    }

    public void updateToken(User user, String loginToken) {
        sqlClient
                .createUpdate(userTable)
                .where(userTable.id().eq(user.id()))
                .set(userTable.loginToken(), loginToken)
                .execute();
        sendUserToMiddleware(user);
    }

    /**
     * 根据 id 删除 user 对象
     *
     * @param id 自增 id
     */
    public void delUserById(long id) {
        sqlClient.deleteById(User.class, id);
    }

    /**
     * 根据账号删除 user 对象
     *
     * @param openId 账号
     * @return 删除的数量
     */
    public void delUserByOpenId(String openId) {
        sqlClient.createDelete(userTable).where(userTable.openId().eq(openId)).execute();
    }

    public User queryUserById(Long id) {
        User user;
        user = sqlClient.createQuery(userTable)
                .where(userTable.id().eq(id))
                .select(userTable)
                .execute()
                .stream()
                .findFirst()
                .orElse(null);

        if (user != null) {
            // 查询时向远程同步
            // 这是为了防止之前增删改时由于可能无法意料的异常导致不能同步
            sendUserToMiddleware(user);
        }

        return user;
    }

    public User queryUserByOpenId(String openId) {
        User user;
        user = sqlClient.createQuery(userTable)
                .where(userTable.openId().eq(openId))
                .select(userTable)
                .execute()
                .stream()
                .findFirst()
                .orElse(null);
        if (user != null) {
            // 查询时向远程同步
            // 这是为了防止之前增删改时由于可能无法意料的异常导致不能同步
            sendUserToMiddleware(user);
        }

        return user;
    }

    public User queryUserByLoginToken(String token) {
        User user;
        user = sqlClient.createQuery(userTable)
                .where(userTable.loginToken().eq(token))
                .select(userTable)
                .execute()
                .stream()
                .findFirst()
                .orElse(null);
        if (user != null) {
            // 查询时向远程同步
            // 这是为了防止之前增删改时由于可能无法意料的异常导致不能同步
            sendUserToMiddleware(user);
        }

        return user;
    }
}
