package com.nailong.websdk.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo<T> {
    @JsonProperty("IsNew")
    private boolean isNew;

    @JsonProperty("IsTestAccount")
    private boolean isTestAccount;

    @JsonProperty("Keys")
    private List<UserKeyJson<T>> Keys;

    @JsonProperty("User")
    private UserJson<T> user; // cn

    @JsonProperty("UserInfo")
    private UserInfoJson<T> userInfo; // os

    @JsonProperty("Yostar")
    private LoginYostarJson<T> yostar;

    @JsonProperty("Identity")
    private IdentityJson identity;

    @JsonProperty("Destroy")
    private DestroyJson destroy;

    @JsonProperty("YostarDestroy")
    private YostarDestroyJson yostarDestroy;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserKeyJson<T> {
        @JsonProperty("ID")
        private T id;

        @JsonProperty("Type")
        private String type;

        @JsonProperty("Key")
        private String key;

        @JsonProperty("NickName")
        private String nickName;

        @JsonProperty("NickNameEnc")
        private String nickNameEnc;

        @JsonProperty("CreatedAt")
        private long createdAt;
    }

    // cn UserInfoJson
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserJson<T> {
        @JsonProperty("ID")
        private T id;

        @JsonProperty("PID")
        private String pid;

        @JsonProperty("Token")
        private String token;

        @JsonProperty("State")
        private int state;

        @JsonProperty("RegChannel")
        private String regChannel;

        @JsonProperty("DestroyState")
        private int destroyState;
    }

    // os UserJson
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserInfoJson<T> {
        @JsonProperty("ID")
        private T id;

        @JsonProperty("UID2")
        private int uid2;

        @JsonProperty("PID")
        private String pid;

        @JsonProperty("Token")
        private String token;

        @JsonProperty("Birthday")
        private String birthday;

        @JsonProperty("RegChannel")
        private String regChannel;

        @JsonProperty("TransCode")
        private String transCode;

        @JsonProperty("State")
        private int state;

        @JsonProperty("DeviceID")
        private String deviceID;

        @JsonProperty("CreatedAt")
        private long createdAt;
    }

    // cn os
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoginYostarJson<T> {
        @JsonProperty("ID")
        private T id;

        @JsonProperty("Country")
        public String country;

        @JsonProperty("NickName")
        private String nickName;

        @JsonProperty("Picture")
        private String picture;

        @JsonProperty("State")
        private int state;

        @JsonProperty("CreatedAt")
        private long createdAt;

        @JsonProperty("DefaultNickName")
        private String defaultNickName;

        @JsonProperty("AgreeAd")
        private int agreeAd;
    }

    // cn
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IdentityJson {
        @JsonProperty("Type")
        private int type;

        @JsonProperty("RealName")
        private String realName;

        @JsonProperty("IDCard")
        private String idCard;

        @JsonProperty("Underage")
        private boolean underage;

        @JsonProperty("PI")
        private String pi;

        @JsonProperty("BirthDate")
        private String birthDate;

        @JsonProperty("State")
        private int state;
    }

    // cn
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DestroyJson {
        @JsonProperty("DestroyAt")
        private int destroyAt;
    }

    // cn
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class YostarDestroyJson {
        @JsonProperty("DestroyAt")
        private int destroyAt;
    }
}
