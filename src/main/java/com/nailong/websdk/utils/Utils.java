package com.nailong.websdk.utils;

public class Utils {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, 0);
    }

    public static String bytesToHex(byte[] bytes, int offset) {
        if (bytes == null || (bytes.length - offset) <= 0) return "";
        char[] hexChars = new char[(bytes.length - offset) * 2];
        for (int j = offset; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            int h = j - offset;
            hexChars[h * 2] = HEX_ARRAY[v >>> 4];
            hexChars[h * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
