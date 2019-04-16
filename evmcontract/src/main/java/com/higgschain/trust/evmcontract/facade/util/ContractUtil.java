package com.higgschain.trust.evmcontract.facade.util;

import java.math.BigInteger;

/**
 * Tool class for contract-related logic.
 *
 * @author Chen Jiawei
 * @date 2018 -11-15
 */
public class ContractUtil {
    /**
     * Defined to avoid an instance being created from outside.
     */
    private ContractUtil() {
    }

    /**
     * To big integer big integer.
     *
     * @param v the v
     * @return the big integer
     */
    public static BigInteger toBigInteger(byte[] v) {
        return new BigInteger(1, v);
    }

    /**
     * Not equal boolean.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the boolean
     */
    public static boolean notEqual(BigInteger v1, BigInteger v2) {
        return v1.compareTo(v2) != 0;
    }

    /**
     * Not equal boolean.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the boolean
     */
    public static boolean notEqual(byte[] v1, byte[] v2) {
        return notEqual(toBigInteger(v1), toBigInteger(v2));
    }

    /**
     * More than boolean.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the boolean
     */
    public static boolean moreThan(BigInteger v1, BigInteger v2) {
        return v1.compareTo(v2) > 0;
    }

    /**
     * More than boolean.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the boolean
     */
    public static boolean moreThan(byte[] v1, byte[] v2) {
        return moreThan(toBigInteger(v1), toBigInteger(v2));
    }
}
