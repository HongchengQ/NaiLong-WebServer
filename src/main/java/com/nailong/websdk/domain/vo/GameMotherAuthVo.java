package com.nailong.websdk.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameMotherAuthVo {
    @JsonProperty("UID")
    String uid;
    @JsonProperty("Token")
    String token;
    @JsonProperty("Account")
    String account;
}
