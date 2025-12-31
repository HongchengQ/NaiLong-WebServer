package com.nailong.websdk.controller;

import com.nailong.websdk.service.IMetaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.nailong.websdk.enums.ServletAttributeEnum.REGION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

/**
 * 此路由下发的内容都比较大，且不依赖逻辑判断，建议使用cdn或oss
 */
@RestController
@RequestMapping(value = "/meta", method = {RequestMethod.GET})
@RequiredArgsConstructor
public class MetaController {

    private final IMetaService metaService;

    /**
     * @return 经过区域 key 加密的 serverList .html文件 (用浏览器访问时会直接下载这个html文件)
     */
    @RequestMapping(path = "/serverlist.html")
    public ResponseEntity<byte[]> serverListHtml(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());

        return ResponseEntity
                .ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .body(metaService.getServerList(region));
    }

    /**
     * @return 经过区域 key 加密的 serverList 内容 (用浏览器访问时不会下载任何文件 它会以String类型展现在界面中)
     */
    @RequestMapping(path = "/serverlist")
    public byte[] serverList(HttpServletRequest req) {
        return metaService.getServerList(
                // 来自于拦截器中添加的属性 - X-Region
                (String) req.getAttribute(REGION.getStr())
        );
    }

    @RequestMapping(path = "/and.html")
    public byte[] androidPatches(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());
        return metaService.getPlatformPatch(region, "Android");
    }

    @RequestMapping(path = "/win.html")
    public byte[] winPatches(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());
        return metaService.getPlatformPatch(region, "Win");
    }

    @RequestMapping(path = "ios.html")
    public byte[] iosPatches(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());
        return metaService.getPlatformPatch(region, "Ios");
    }

    @RequestMapping(path = "/test")
    public String test() {
        return "test";
    }
}
