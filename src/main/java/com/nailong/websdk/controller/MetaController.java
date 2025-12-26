package com.nailong.websdk.controller;

import com.nailong.websdk.service.IMetaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RestController
@RequestMapping(value = "/meta", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
@Log4j2
public class MetaController {
    /*
    // https://nova-static.stellasora.global/
    getApp().get("/meta/serverlist.html", new MetaServerlistHandler(this));
    getApp().get("/meta/and.html", new MetaPatchListHandler(this, "Android"));
    getApp().get("/meta/win.html", new MetaPatchListHandler(this, "Win"));
    getApp().get("/meta/ios.html", new MetaPatchListHandler(this, "Ios"));
    getApp().get("/res/**\/", new HotFixHandler());
    */

    private final IMetaService metaService;

    @RequestMapping(path = "/serverlist.html")
    public Object serverListHtml(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute("X-Region");
        return ResponseEntity.ok().contentType(APPLICATION_OCTET_STREAM).body(metaService.getServerList(region));
    }

    @RequestMapping(path = "/serverlist")
    public Object serverList(HttpServletRequest req) {
        // region 来自于拦截器中添加的属性 - X-Region
        String region = (String) req.getAttribute("X-Region");
        return metaService.getServerList(region);
    }

    @RequestMapping(path = "/test")
    public String test() {
        return "test";
    }
}
