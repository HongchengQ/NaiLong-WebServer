package com.nailong.websdk.controller;

import com.nailong.websdk.exception.BadRequestException;
import com.nailong.websdk.proto.Pb;
import com.nailong.websdk.utils.AeadHelper;
import com.nailong.websdk.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    @RequestMapping(path = "/decode_server_list")
    public Object decodeServerList(@RequestParam String region, @RequestBody String url) throws Exception {
        // 使用 RestTemplate 获取URL内容
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        if (response.getStatusCode().value() == 200) {
            byte[] body = response.getBody();

            if (ObjectUtils.isEmpty(body)) {
                throw new BadRequestException("response body is empty");
            }

            log.info("开始解析内容(hex): {}", Utils.bytesToHex(body));

            byte[] serverListBytes = AeadHelper.decryptCBC(body, region);

            Pb.ServerListMeta rsp = Pb.ServerListMeta.parseFrom(serverListBytes);

            log.info("已解析内容 {}", rsp);

            return rsp.toString();
        } else {
            return Map.of("error", "Failed to fetch data from URL: " + url, "status", response.getStatusCode());
        }
    }
}
