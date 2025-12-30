package com.nailong.websdk.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class HttpClientUtil {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendCommandToServer(String server, String token, String commandName, Object userObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 先将 user 对象转换为 JSON 字符串
        // 然后转 bytes
        // 最后转hex 用 string 类型存储
        String userJson = Utils.bytesToHex(JsonUtils.toJson(userObject).getBytes(StandardCharsets.UTF_8));

        String jsonBody = "{\"token\": \"" + token +
                "\", \"command\": \"" + commandName + " " + userJson + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(server, entity, String.class);

        log.info("Successfully sent user object to middleware. Response: {}", response.getBody());
    }
}