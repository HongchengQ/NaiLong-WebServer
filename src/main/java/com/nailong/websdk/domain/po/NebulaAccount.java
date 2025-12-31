package com.nailong.websdk.domain.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NebulaAccount {
    private String uid;

    private String email;
    private String code;

    private String nickname;
    private String picture;

    private String loginToken;
    private String gameToken;

    private Set<String> permissions;

    private int reservedPlayerUid;
    private Long createdAt;

    public NebulaAccount(User user) {
        this.uid = String.valueOf(user.id());
        this.email = user.openId();
        this.code = user.password();
        this.nickname = user.nickName();
        this.picture = "";
        this.loginToken = user.loginToken();
        this.gameToken = user.loginToken();
        this.permissions = null;
        this.reservedPlayerUid = 0;
        this.createdAt = user.createdTime();
    }
}
