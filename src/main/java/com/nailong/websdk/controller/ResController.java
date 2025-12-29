package com.nailong.websdk.controller;

import com.nailong.websdk.enums.HotFixLocalPathEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Locale;

/**
 * 将超大文件重定向到 cdn 或 oss
 * 这里逻辑比较简单 所以不用 service 处理了
 */
@RestController
@RequestMapping(value = "/res", method = RequestMethod.GET)
@RequiredArgsConstructor
public class ResController {
    @RequestMapping(path = "/win/**")
    public ModelAndView resWinRedirect(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        String region = req.getAttribute("X-Region").toString();
        // region 转大写
        String platform = region.toUpperCase(Locale.ROOT) + "_" + "Win";
        String url = HotFixLocalPathEnum.valueOf(platform).getOssHost();

        rsp.sendRedirect(url + req.getServletPath());
        return null;
    }

    @RequestMapping(path = "/and/**")
    public ModelAndView resAndroidRedirect(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        String region = req.getAttribute("X-Region").toString();
        String platform = region.toUpperCase(Locale.ROOT) + "_" + "Android";
        String url = HotFixLocalPathEnum.valueOf(platform).getOssHost();

        rsp.sendRedirect(url + req.getServletPath());
        return null;
    }

    @RequestMapping(path = "/ios/**")
    public ModelAndView resIosRedirect(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        String region = req.getAttribute("X-Region").toString();
        String platform = region.toUpperCase(Locale.ROOT) + "_" + "Ios";
        String url = HotFixLocalPathEnum.valueOf(platform).getOssHost();

        rsp.sendRedirect(url + req.getServletPath());
        return null;
    }

    @RequestMapping(path = "/test")
    public String test() {
        return "test";
    }
}
