package com.nailong.websdk.service;

public interface IMetaService {

    /**
     * 获取区服列表
     * 但是客户端似乎是有问题 列表只能读取到第一个 后续无效
     *
     * @return HttpRsp
     */
    byte[] getServerList(String region);
}
