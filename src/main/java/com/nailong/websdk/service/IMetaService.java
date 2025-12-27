package com.nailong.websdk.service;

public interface IMetaService {

    /**
     * 获取区服列表
     * 但是客户端似乎是有问题 列表只能读取到第一个 后续无效
     *
     * @return 经过区域 key 加密的 serverList
     */
    byte[] getServerList(String region);

    byte[] getPlatformPatch(String region, String platform);
}
