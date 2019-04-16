package com.higgschain.trust.network.utils;

import java.util.UUID;

/**
 * The type Random util.
 *
 * @author duhongming
 * @date 2018 /9/5
 */
public class RandomUtil {
    /**
     * Random int int.
     *
     * @param max the max
     * @return the int
     */
    public static int randomInt(int max) {
        return (int) Math.round(Math.random() * max);
    }

    /**
     * Random long long.
     *
     * @param max the max
     * @return the long
     */
    public static long randomLong(long max) {
        return Math.round(Math.random() * max);
    }

    /**
     * Random double double.
     *
     * @param max the max
     * @return the double
     */
    public static double randomDouble(long max) {
        return Math.random() * max;
    }

    /**
     * Uuid string.
     *
     * @return the string
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
