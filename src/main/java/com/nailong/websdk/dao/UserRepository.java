package com.nailong.websdk.dao;

import com.nailong.websdk.domain.dto.UserInput;
import com.nailong.websdk.domain.po.Tables;
import com.nailong.websdk.domain.po.User;
import com.nailong.websdk.domain.po.UserTable;
import jakarta.validation.constraints.NotEmpty;
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

    UserTable userTable = Tables.USER_TABLE;

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
        return sqlClient
                .getEntities()
                .save(user)
                .getModifiedEntity();
    }

    public void updateUserName(Long userId, String name) {
        sqlClient
                .createUpdate(userTable)
                .where(userTable.id().eq(userId))
                .set(userTable.nickName(), name)
                .execute();
    }

    public void updateToken(Long id, String loginToken) {
        sqlClient
                .createUpdate(userTable)
                .where(userTable.id().eq(id))
                .set(userTable.loginToken(), loginToken)
                .execute();
    }

    public void updateToken(User user, String loginToken) {
        sqlClient
                .createUpdate(userTable)
                .where(userTable.id().eq(user.id()))
                .set(userTable.loginToken(), loginToken)
                .execute();
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

    public User queryUserByOpenId(String openId) {
        return sqlClient.createQuery(userTable)
                .where(userTable.openId().eq(openId))
                .select(userTable)
                .execute()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public User queryUserByLoginToken(String token) {
        return sqlClient.createQuery(userTable)
                .where(userTable.loginToken().eq(token))
                .select(userTable)
                .execute()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
