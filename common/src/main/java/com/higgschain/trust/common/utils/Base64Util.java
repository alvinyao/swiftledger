package com.higgschain.trust.common.utils;

import org.springframework.util.Base64Utils;

/**
 * The type Base 64 util.
 */
public class Base64Util {

    /**
     * BASE64解码
     *
     * @param key the key
     * @return byte [ ]
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) {
        return Base64Utils.decodeFromString(key);
    }

    /**
     * BASE64编码
     *
     * @param key the key
     * @return string
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) {
        return Base64Utils.encodeToString(key);
    }

    /**
     * Byte array to hex str string.
     *
     * @param byteArray the byte array
     * @return String string
     * @desc byte[] to  String(HEX formart)
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Hex str to byte array byte [ ].
     *
     * @param str the str
     * @return byte[] byte [ ]
     * @desc String(HEX formart) to byte[]
     */
    public static byte[] hexStrToByteArray(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    /**
     * Str to byte array byte [ ].
     *
     * @param str the str
     * @return byte [ ]
     * @desc String to byte[]
     */
    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }

    /**
     * Byte array to str string.
     *
     * @param byteArray the byte array
     * @return string
     * @desc byte[] to String
     */
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }
}
