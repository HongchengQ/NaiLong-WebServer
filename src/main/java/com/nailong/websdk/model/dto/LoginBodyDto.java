package com.nailong.websdk.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBodyDto {
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

    AuthorizationDto authorizationDto;
}
