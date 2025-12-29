package com.nailong.websdk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBody {
    @JsonProperty("Type")
    String type;

    @JsonProperty("OpenID")
    String openId;

    @JsonProperty("Token")
    String token;

    @JsonProperty("UserName")
    String userName;

    @JsonProperty("Secret")
    String secret;

    Authorization authorization;
}
