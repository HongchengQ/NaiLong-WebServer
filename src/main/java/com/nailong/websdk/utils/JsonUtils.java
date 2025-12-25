package com.nailong.websdk.utils;

import org.springframework.util.ObjectUtils;
import tools.jackson.databind.ObjectMapper;

public class JsonUtils {
    /**
     * 将 json 字符串解析为对象
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToObject(String jsonStr, Class<T> clazz) {
        if (ObjectUtils.isEmpty(jsonStr)) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr, clazz);
    }
}
