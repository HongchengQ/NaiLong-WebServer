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

import static com.nailong.websdk.enums.ServletAttributeEnum.REGION;

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
        String region = (String) req.getAttribute(REGION.getStr());
        // region 转大写
        String platform = region.toUpperCase(Locale.ROOT) + "_" + "Win";
        String url = HotFixLocalPathEnum.valueOf(platform).getOssHost();

        rsp.sendRedirect(url + req.getServletPath());
        return null;
    }

    @RequestMapping(path = "/and/**")
    public ModelAndView resAndroidRedirect(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        String region = (String) req.getAttribute(REGION.getStr());
        String platform = region.toUpperCase(Locale.ROOT) + "_" + "Android";
        String url = HotFixLocalPathEnum.valueOf(platform).getOssHost();

        rsp.sendRedirect(url + req.getServletPath());
        return null;
    }

    @RequestMapping(path = "/ios/**")
    public ModelAndView resIosRedirect(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        String region = (String) req.getAttribute(REGION.getStr());
        String platform = region.toUpperCase(Locale.ROOT) + "_" + "Ios";
        String url = HotFixLocalPathEnum.valueOf(platform).getOssHost();

        rsp.sendRedirect(url + req.getServletPath());
        return null;
    }

    @RequestMapping(path = "/test")
    public ModelAndView test(HttpServletResponse rsp) throws IOException {
        rsp.sendRedirect("http://127.0.0.1:1178" + "/res/test1");
        return null;
    }

    @RequestMapping(path = "/test1")
    public String testOne() {
        return "test";
    }
}
