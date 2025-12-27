package com.nailong.websdk.service.impl;

import com.nailong.websdk.config.AppProperties;
import com.nailong.websdk.config.AppProperties.GateServer;
import com.nailong.websdk.enums.HotFixLocalPathEnum;
import com.nailong.websdk.pojo.HotfixPatchList;
import com.nailong.websdk.service.IMetaService;
import com.nailong.websdk.utils.AeadHelper;
import com.nailong.websdk.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Log4j2
public class MetaService implements IMetaService {
    // key: region; 例如: cn, tw
    static Map<String, byte[]> metaServerListMap = new HashMap<>(5);
    // key: 热更枚举字段名; 例如: CN_Android, GLOBAL_Ios
    static Map<String, byte[]> metaHotfixPatchListMap = new HashMap<>();

    // config
    private final AppProperties appProperties;

    @PostConstruct
    public void init() {
        initServerList();
        initHotfixPatchList();
    }

    public void initServerList() {
        Map<String, GateServer> gateServerList = appProperties.getGateServerMap();

        for (String name : gateServerList.keySet()) {
            GateServer gateServer = gateServerList.get(name);

            if (metaServerListMap.get(name) != null) {
                log.warn("重复加载区域 {} - gateServer，只能记录一次", name);
                continue;
            }

            byte[] encodeRegionMetaBytes;
            try {
                encodeRegionMetaBytes = AeadHelper.encodeRegionMeta(name, gateServer);
            } catch (Exception e) {
                log.warn("{} 加载区服时发生错误", name, e);
                continue;
            }

            metaServerListMap.put(name, encodeRegionMetaBytes);
            log.info("已预编译区服列表：{} - {}", name, gateServer);
        }
    }

    public void initHotfixPatchList() {
        for (HotFixLocalPathEnum hotFixLocalPathEnum : HotFixLocalPathEnum.values()) {
            String name = hotFixLocalPathEnum.name();
            String path = hotFixLocalPathEnum.getLocalPath();
            String region = hotFixLocalPathEnum.getRegion();

            byte[] encryptPatchBytes;
            try {
                HotfixPatchList hotfixPatchList = FileUtils.readHotFixPatch(path);
                byte[] patchBytes = hotfixPatchList.toProto().toByteArray();
                encryptPatchBytes = AeadHelper.encryptCBC(patchBytes, region);
            } catch (Exception e) {
                log.warn("{} 加载热更清单时发生错误", name, e);
                continue;
            }

            metaHotfixPatchListMap.put(name, encryptPatchBytes);
            log.info("已预编译热更清单：{}", name);
        }
    }

    @Override
    public byte[] getServerList(String region) {
        return metaServerListMap.get(region);
    }

    @Override
    public byte[] getPlatformPatch(String region, String platform) {
        // 组装平台和区域为 metaHotfixPatchListMap 的 key
        String mapKey = region.toUpperCase(Locale.ROOT) + "_" + platform;
        return metaHotfixPatchListMap.get(mapKey);
    }
}
