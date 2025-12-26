package com.nailong.websdk.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
@ToString
public class AppProperties {
    private Map<String, GateServer> gateServerMap;

    @Data
    @ToString
    public static class GateServer {
        String ip;
        Integer port;
        String uri; // 附加在 ip 和 port 后的路径
        Boolean useSSL;
        String ossHost; // 对象存储/cdn
        String bigVersion;
        Integer dataVersion;

        public String getComboAddress() {
            StringBuilder url = new StringBuilder();

            if (useSSL) {
                url.append("https://");
            } else {
                url.append("http://");
            }

            url.append(this.ip);

            if ((!useSSL && port != 80) || (useSSL && port != 443)) {
                if (port != 0) {
                    url.append(":");
                    url.append(port);
                }
            }

            if (uri != null) {
                url.append(uri);
            }

            return url.toString();
        }
    }
}
