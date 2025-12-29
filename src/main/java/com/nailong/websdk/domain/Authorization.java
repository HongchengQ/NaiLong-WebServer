package com.nailong.websdk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.PrimitiveIterator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authorization {
    @NonNull
    @JsonProperty("Head")
    private Head head;

    @JsonProperty("Sign")
    private String Sign;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Head {
        @NonNull
        @JsonProperty("PID")
        private String pid;

        @NonNull
        @JsonProperty("Channel")
        private String channel;

        @NonNull
        @JsonProperty("Platform")
        private String platform;

        @JsonProperty("Lang")
        private String lang;

        @JsonProperty("DeviceID")
        private String deviceID;

        @JsonProperty("Version")
        private String version;

        @JsonProperty("GVersionNo")
        private String gVersionNo;

        @JsonProperty("GBuildNo")
        private String gBuildNo;

        @JsonProperty("RID")
        private String rid;

        @JsonProperty("DeviceModel")
        private String deviceModel;

        @NonNull
        @JsonProperty("Time")
        private Long time;

        @JsonProperty("UId")
        private Long uid;

        @JsonProperty("Token")
        private String token;
    }
}
