package com.nailong.websdk.utils;

import com.nailong.websdk.pojo.HotfixPatchList;
import com.nailong.websdk.pojo.HttpRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.nailong.websdk.utils.JsonUtils.parseJsonStrToObject;

@Slf4j
public class FileUtils {
    /**
     * 使用 Spring 提供的 ClassPathResource 获取文件内容
     * <p>
     * 备注：jar包可用
     *
     * @param fileName 路径 + 文件名
     * @return BufferedReader 字符流
     * @throws IOException IO 异常
     */
    public static BufferedReader getFileBuffer(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * 获取文件字符内容
     *
     * @param fileName 路径 + 文件名
     * @return 文件内容
     * @throws IOException IO 异常
     */
    public static String getFileStr(String fileName) throws IOException {
        BufferedReader file = getFileBuffer(fileName);
        return file.readAllAsString();
    }

    /**
     * 将 JSON 文件转换为对象
     *
     * @param fileName 路径 + 文件名
     * @param clazz    目标类类型
     * @return 对象实例
     * @throws IOException IO 异常
     */
    public static <T> T fileToObject(String fileName, Class<T> clazz) throws IOException {
        String jsonStr = getFileStr(fileName);

        return parseJsonStrToObject(jsonStr, clazz);
    }

    /**
     * 读取客户端代码 JSON 文件并转换为 HttpRsp 对象
     *
     * @return HttpRsp 对象，包含 CodeList 数据
     * @throws IOException IO 异常
     */
    public static HttpRsp readClientCodeFile() throws IOException {
        return fileToObject("common/codes/client_code.json", HttpRsp.class);
    }

    public static HttpRsp readClientConfigFile(String region) throws IOException {
        String path = String.format("common/client_configs/client_config_%s.json", region);

        return fileToObject(path, HttpRsp.class);
    }

    public static HttpRsp readClientCommonVersion() throws IOException {
        return fileToObject("common/version.json", HttpRsp.class);
    }

    /**
     * 我们需要验证文件是合规的对象
     *
     * @param path ClassPathResource
     * @return HotfixPatchList
     * @throws IOException IO 异常
     */
    public static HotfixPatchList readHotFixPatch(String path) throws IOException {
        return fileToObject(path, HotfixPatchList.class);
    }
}
