package com.higgschain.trust.common.utils;

import java.security.MessageDigest;

/**
 * The type Md 5.
 */
public class MD5 {

    /**
     * Encode string.
     *
     * @param buf the buf
     * @return the string
     */
    public static String encode(String buf) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] rs = digest.digest(buf.getBytes("UTF-8"));
            StringBuffer digestHexStr = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                digestHexStr.append(byteHEX(rs[i]));
            }
            return digestHexStr.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Byte hex string.
     *
     * @param ib the ib
     * @return the string
     */
    public static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}
