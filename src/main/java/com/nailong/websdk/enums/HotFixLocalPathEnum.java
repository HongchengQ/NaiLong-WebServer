package com.nailong.websdk.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// 区域必须全大写 后面的平台用大驼峰
@Getter
public enum HotFixLocalPathEnum {
    CN_Android("hot_fix/CN_Android.json"),
    TW_Android("hot_fix/TW_Android.json"),
    KR_Android("hot_fix/KR_Android.json"),
    JP_Android("hot_fix/JP_Android.json"),
    GLOBAL_Android("hot_fix/GLOBAL_Android.json"),

    CN_Win("hot_fix/CN_Win.json"),
    TW_Win("hot_fix/TW_Win.json"),
    KR_Win("hot_fix/KR_Win.json"),
    JP_Win("hot_fix/JP_Win.json"),
    GLOBAL_Win("hot_fix/GLOBAL_Win.json"),

    CN_Ios("hot_fix/CN_Ios.json"),
    TW_Ios("hot_fix/TW_Ios.json"),
    KR_Ios("hot_fix/KR_Ios.json"),
    JP_Ios("hot_fix/JP_Ios.json"),
    GLOBAL_Ios("hot_fix/GLOBAL_Ios.json");

    final String localPath;
    final String region; // 所有 region 字段默认都应该是小写
    final String platform; // 这里是大驼峰 与字段名一致
    final String shortPlatform; // 截断前三个字符的平台代号 全小写
    final String ossHost; // 热更 url

    HotFixLocalPathEnum(String localPath) {
        // 根据下划线分割为 区域+设备标识
        List<String> hotFixNameParts = Pattern.compile("_")
                .splitAsStream(this.name())
                .toList();

        this.region = hotFixNameParts.getFirst().toLowerCase(Locale.ROOT);
        this.platform = hotFixNameParts.getLast();
        this.localPath = localPath;
        this.shortPlatform = platform.substring(0, Math.min(3, platform.length())).toLowerCase(Locale.ROOT);

        switch (this.region) {
            case "tw" -> this.ossHost = "https://nova-static.stargazer-games.com";
            case "jp" -> this.ossHost = "https://nova-static.stellasora.jp";
            case "global" -> this.ossHost = "https://nova-static.stellasora.global";
            case "kr" -> this.ossHost = "https://nova-static.stellasora.kr";
            default -> this.ossHost = "https://nova-static.yostar.cn";
        }
    }

    public static List<String> getLocalPaths() {
        return Arrays.stream(HotFixLocalPathEnum.values())
                .map(HotFixLocalPathEnum::getLocalPath)
                .toList();
    }
}
