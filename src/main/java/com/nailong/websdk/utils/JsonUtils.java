package com.nailong.websdk.utils;

import org.springframework.util.ObjectUtils;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.json.JsonMapper;

public class JsonUtils {
    /**
     * 将 json 字符串解析为对象
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseJsonStrToObject(String jsonStr, Class<T> clazz) {
        if (ObjectUtils.isEmpty(jsonStr)) {
            return null;
        }

        JsonFactory jsonFactory = JsonFactory.builder()
                .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, true)// 允许注释
                .configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true)// 允许尾随符号
                .build();
        JsonMapper jsonMapper = new JsonMapper(jsonFactory);

        return jsonMapper.readValue(jsonStr, clazz);
    }

    public static String toJson(Object obj) {
        try {
            JsonMapper jsonMapper = new JsonMapper();
            return jsonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
}
