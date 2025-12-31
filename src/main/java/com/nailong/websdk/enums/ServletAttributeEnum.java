package com.nailong.websdk.enums;

import lombok.Getter;

@Getter
public enum ServletAttributeEnum {
    AUTH_INFO("Authorization"),
    REGION("X-Region");

    private final String str;

    ServletAttributeEnum(String s) {
        this.str = s;
    }

    @Override
    public String toString() {
        return "ServletAttributeEnum." + this.str;
    }
}
