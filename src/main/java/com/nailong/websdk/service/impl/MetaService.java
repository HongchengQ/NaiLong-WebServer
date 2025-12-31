package com.nailong.websdk.service.impl;

import com.nailong.websdk.config.AppProperties;
import com.nailong.websdk.config.AppProperties.GateServer;
import com.nailong.websdk.model.bo.HotfixPatchList;
import com.nailong.websdk.enums.HotFixLocalPathEnum;
import com.nailong.websdk.service.IMetaService;
import com.nailong.websdk.utils.AeadHelper;
import com.nailong.websdk.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Log4j2
public class MetaService implements IMetaService {
    // config
    private final AppProperties appProperties;

    // key: region; 例如: cn, tw
    static Map<String, byte[]> metaServerListMap = new HashMap<>(5);
    // key: 热更枚举字段名; 例如: CN_Android, GLOBAL_Ios
    static Map<String, byte[]> metaHotfixPatchListMap = new HashMap<>();

    // 用于跟踪文件最后修改时间的 Map
    static Map<String, Long> fileLastModifiedMap = new HashMap<>();
    // 定期检查文件变化的调度器
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

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

    @PostConstruct
    public void init() {
        initServerList();
        initHotfixPatchList();
        startPeriodicFileCheck();
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

    private void startPeriodicFileCheck() {
        // 初始化文件修改时间记录
        String basePath = System.getProperty("user.dir") + File.separator;
        for (HotFixLocalPathEnum hotFixLocalPathEnum : HotFixLocalPathEnum.values()) {
            String hotfixPath = hotFixLocalPathEnum.getLocalPath();
            String fullPath = basePath + hotfixPath;
            File file = new File(fullPath);
            if (file.exists()) {
                fileLastModifiedMap.put(hotfixPath, file.lastModified());
            }
        }

        // 定时检查文件是否被修改
        scheduledExecutorService.scheduleAtFixedRate(() -> checkForFileChanges(basePath), 30, 60, TimeUnit.SECONDS);
    }

    private void checkForFileChanges(String basePath) {
        for (HotFixLocalPathEnum hotFixLocalPathEnum : HotFixLocalPathEnum.values()) {
            String path = hotFixLocalPathEnum.getLocalPath();
            String name = hotFixLocalPathEnum.name();
            String region = hotFixLocalPathEnum.getRegion();

            File file = new File(basePath + path);
            if (!file.exists())
                continue;
            long currentModified = file.lastModified();

            Long lastModified = fileLastModifiedMap.get(path);

            if (lastModified == null || currentModified > lastModified) {
                // 文件被修改，更新缓存
                try {
                    HotfixPatchList hotfixPatchList = FileUtils.readHotFixPatch(path);
                    byte[] patchBytes = hotfixPatchList.toProto().toByteArray();
                    byte[] encryptPatchBytes = AeadHelper.encryptCBC(patchBytes, region);

                    metaHotfixPatchListMap.put(name, encryptPatchBytes);
                    fileLastModifiedMap.put(path, currentModified);
                    log.info("热更文件 {} 已通过定期检查更新", name);
                } catch (Exception e) {
                    log.warn("{} 更新热更清单时发生错误", name, e);
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }
}