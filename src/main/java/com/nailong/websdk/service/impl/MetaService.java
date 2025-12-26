package com.nailong.websdk.service.impl;

import com.nailong.websdk.Pb;
import com.nailong.websdk.config.AppProperties;
import com.nailong.websdk.config.AppProperties.GateServer;
import com.nailong.websdk.service.IMetaService;
import com.nailong.websdk.utils.AeadHelper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Log4j2
public class MetaService implements IMetaService {
    static Map<String, byte[]> metaServerMap = new HashMap<>(5);

    private final AppProperties appProperties;

    /**
     * 获取区服列表
     * 但是客户端似乎是有问题 列表只能读取到第一个 后续无效
     *
     * @return String
     */
    public byte[] getServerList(String region) {
        return metaServerMap.get(region);
    }

    @PostConstruct
    public void initServerList() throws Exception {
        Map<String, GateServer> gateServerList = appProperties.getGateServerMap();

        for (String name : gateServerList.keySet()) {
            GateServer gateServer = gateServerList.get(name);

            if (metaServerMap.get(name) != null) {
                log.warn("重复加载区域 {} - gateServer，只能记录一次", name);
                continue;
            } else {
                log.info("已预编译 ServerList：{} - {}", name, gateServer);
            }

            metaServerMap.put(name, encodeRegionMeta(name, gateServer));
        }
    }

    private byte[] encodeRegionMeta(String gateServerName, GateServer gateServer) throws Exception {
        String address = gateServer.getComboAddress();

        // 这里需要注意 虽然名字叫list 但是客户端只认第一个
        Pb.ServerListMeta meta = Pb.ServerListMeta.newInstance()
                .setVersion(gateServer.getDataVersion())
                .setReportEndpoint(address + "/report");

        var agent = Pb.ServerAgent.newInstance()
                .setName(gateServerName)
                .setAddr(address + "/agent-zone-" + gateServer.getUri())
                .setStatus(1)
                .setZone(1);

        meta.addAgent(agent);

        return AeadHelper.encryptCBC(meta.toByteArray(), gateServerName) ;
    }
}
