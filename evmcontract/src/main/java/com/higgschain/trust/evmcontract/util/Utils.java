/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.higgschain.trust.evmcontract.util;

import com.higgschain.trust.evmcontract.vm.DataWord;
import org.spongycastle.util.encoders.Hex;

import javax.swing.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * The type Utils.
 */
public class Utils {
    private static final DataWord DIVISOR = new DataWord(64);

    private static SecureRandom random = new SecureRandom();

    /**
     * on android this property equals to 0
     */
    public static final String JAVA_VERSION_OF_ANDROID= "0";

    /**
     * Unified numeric to big integer big integer.
     *
     * @param number should be in form '0x34fabd34....'
     * @return String big integer
     */
    public static BigInteger unifiedNumericToBigInteger(String number) {

        boolean match = Pattern.matches("0[xX][0-9a-fA-F]+", number);
        if (!match) {
            return (new BigInteger(number));
        } else {
            number = number.substring(2);
            number = number.length() % 2 != 0 ? "0".concat(number) : number;
            byte[] numberBytes = Hex.decode(number);
            return (new BigInteger(1, numberBytes));
        }
    }

    /**
     * Return formatted Date String: yyyy.MM.dd HH:mm:ss
     * Based on Unix's time() input in seconds
     *
     * @param timestamp seconds since start of Unix-time
     * @return String formatted as - yyyy.MM.dd HH:mm:ss
     */
    public static String longToDateTime(long timestamp) {
        Date date = new Date(timestamp * 1000);
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * Gets image icon.
     *
     * @param resource the resource
     * @return the image icon
     */
    public static ImageIcon getImageIcon(String resource) {
        URL imageURL = ClassLoader.getSystemResource(resource);
        ImageIcon image = new ImageIcon(imageURL);
        return image;
    }

    /**
     * 10^3 = 1000
     */
    static BigInteger CUBE_OF_10 = new BigInteger("1000");

    /**
     * Gets value short string.
     *
     * @param number the number
     * @return the value short string
     */
    public static String getValueShortString(BigInteger number) {
        BigInteger result = number;
        int pow = 0;
        while (result.compareTo(CUBE_OF_10) == 1 || result.compareTo(CUBE_OF_10) == 0) {
            result = result.divide(CUBE_OF_10);
            pow += 3;
        }
        return result.toString() + "\u00b7(" + "10^" + pow + ")";
    }

    /**
     * Is valid address boolean.
     *
     * @param addr the addr
     * @return the boolean
     */
    public static boolean isValidAddress(byte[] addr) {
        return addr != null && addr.length == 20;
    }

    /**
     * Gets random.
     *
     * @return the random
     */
    public static SecureRandom getRandom() {
        return random;
    }

    /**
     * The constant JAVA_VERSION.
     */
    public static double JAVA_VERSION = getJavaVersion();

    /**
     * Gets java version.
     *
     * @return the java version
     */
    static double getJavaVersion() {
        String version = System.getProperty("java.version");

        // on android this property equals to 0
        if (JAVA_VERSION_OF_ANDROID.equals(version)) {
            return 0;
        }

        //twice search point index
        int first = version.indexOf(".");
        int second = version.indexOf(".", first + 1);

        return Double.parseDouble(version.substring(0,second));
    }

    /**
     * Align string.
     *
     * @param s          the s
     * @param fillChar   the fill char
     * @param targetLen  the target len
     * @param alignRight the align right
     * @return the string
     */
    public static String align(String s, char fillChar, int targetLen, boolean alignRight) {
        if (targetLen <= s.length()) {
            return s;
        }
        String alignString = repeat("" + fillChar, targetLen - s.length());
        return alignRight ? alignString + s : s + alignString;

    }

    /**
     * Repeat string.
     *
     * @param s the s
     * @param n the n
     * @return the string
     */
    public static String repeat(String s, int n) {
        if (s.length() == 1) {
            byte[] bb = new byte[n];
            Arrays.fill(bb, s.getBytes()[0]);
            return new String(bb);
        } else {
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < n; i++) {
                ret.append(s);
            }
            return ret.toString();
        }
    }


}