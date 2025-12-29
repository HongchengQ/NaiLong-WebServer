package com.nailong.websdk.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull(message = "请求体不能为空")
public class AuthRequest {
    @NotNull(message = "参数: Account 不能为空")
    public String account;
    @NotNull(message = "参数: Code 不能为空")
    public String code;
}
