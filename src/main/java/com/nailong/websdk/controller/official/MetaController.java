package com.nailong.websdk.controller.official;

import com.nailong.websdk.service.IMetaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.nailong.websdk.enums.ServletAttributeEnum.REGION;

/**
 * 此路由下发的内容都比较大，且不依赖逻辑判断，建议使用cdn或oss
 */
@RestController
@RequestMapping(method = {RequestMethod.GET})
@RequiredArgsConstructor
public class MetaController {

    private final IMetaService metaService;

    /**
     * @return 经过区域 key 加密的 serverList .html文件
     */
    @RequestMapping(path = "/meta/serverlist.html")
    public byte[] serverListHtml(HttpServletRequest req) {
        return metaService.getServerList(
                // 来自于拦截器中添加的属性 - X-Region
                (String) req.getAttribute(REGION.getStr())
        );
    }

    /**
     *
     * 工具箱
     * {
     * "AuthPID": "CN-NOVA",
     * "URL": "https://toolbox-stellasora.yostar.cn"
     * }
     * <p>
     * 官方社区
     * {
     * "AuthPID": "CN-BBS",
     * "URL": "https://bbs-stellasora.yostar.net/"
     * }
     *
     * @return 经过区域 key 加密的 serverList .html文件
     */
    @RequestMapping(path = "/notice/noticelist.html")
    public byte[] noticeListHtml(HttpServletRequest req) throws Exception {
        return metaService.getNoticeList(
                // 来自于拦截器中添加的属性 - X-Region
                (String) req.getAttribute(REGION.getStr())
        );
    }

    @RequestMapping(path = "/meta/and.html")
    public byte[] androidPatches(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());
        return metaService.getPlatformPatch(region, "Android");
    }

    @RequestMapping(path = "/meta/win.html")
    public byte[] winPatches(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());
        return metaService.getPlatformPatch(region, "Win");
    }

    @RequestMapping(path = "/meta/ios.html")
    public byte[] iosPatches(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute(REGION.getStr());
        return metaService.getPlatformPatch(region, "Ios");
    }

    @RequestMapping(path = "/meta/test")
    public String test() {
        return "test";
    }
}
