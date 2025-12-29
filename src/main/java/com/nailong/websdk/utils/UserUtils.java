package com.nailong.websdk.utils;

import com.nailong.websdk.domain.LoginBody;

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
        byte[] random = new byte[8];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(random);

        String temp = accountUid + "." + System.currentTimeMillis() + "." + secureRandom;

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = md.digest(temp.getBytes());
        return Utils.bytesToHex(bytes);
    }

    public static String createSessionKey(LoginBody loginBody) throws NoSuchAlgorithmException {
        byte[] random = new byte[8];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(random);

        String temp = loginBody.toString() + System.currentTimeMillis() + secureRandom;

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = md.digest(temp.getBytes());
        return Utils.bytesToHex(bytes);
    }
}
