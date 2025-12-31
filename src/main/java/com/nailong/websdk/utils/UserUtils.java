package com.nailong.websdk.utils;

import com.nailong.websdk.model.dto.LoginBodyDto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class UserUtils {
    /**
     * 创建 token
     *
     * @param accountUid 数据库中字段为 id
     * @return 40位token，它看起来像16进制
     */
    public static String createSessionKey(String accountUid) throws NoSuchAlgorithmException {
        byte[] random = new byte[64];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(random);

        String temp = accountUid + "." + System.currentTimeMillis() + "." + secureRandom;

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = md.digest(temp.getBytes());
        return Utils.bytesToHex(bytes);
    }

    public static String createSessionKey(LoginBodyDto loginBodyDto) throws NoSuchAlgorithmException {
        byte[] random = new byte[64];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(random);

        String temp = loginBodyDto.toString() + System.currentTimeMillis() + secureRandom;

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = md.digest(temp.getBytes());
        return Utils.bytesToHex(bytes);
    }
}
