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
package com.higgschain.trust.evmcontract.config;

import java.math.BigInteger;

/**
 * Describes different constants specific for a blockchain
 * <p>
 * Created by Anton Nashatyrev on 25.02.2016.
 */
public class Constants {
    private static final int MAXIMUM_EXTRA_DATA_SIZE = 32;
    private static final int MIN_GAS_LIMIT = 125000;
    private static final int GAS_LIMIT_BOUND_DIVISOR = 1024;
    private static final BigInteger MINIMUM_DIFFICULTY = BigInteger.valueOf(131072);
    private static final BigInteger DIFFICULTY_BOUND_DIVISOR = BigInteger.valueOf(2048);
    private static final int EXP_DIFFICULTY_PERIOD = 100000;

    private static final int UNCLE_GENERATION_LIMIT = 7;
    private static final int UNCLE_LIST_LIMIT = 2;

    private static final int BEST_NUMBER_DIFF_LIMIT = 100;

    private static final BigInteger BLOCK_REWARD = new BigInteger("1500000000000000000");

    private static final BigInteger SECP256K1N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);

    /**
     * Gets duration limit.
     *
     * @return the duration limit
     */
    public int getDURATION_LIMIT() {
        return 8;
    }

    /**
     * Gets initial nonce.
     *
     * @return the initial nonce
     */
    public BigInteger getInitialNonce() {
        return BigInteger.ZERO;
    }

    /**
     * Gets maximum extra data size.
     *
     * @return the maximum extra data size
     */
    public int getMAXIMUM_EXTRA_DATA_SIZE() {
        return MAXIMUM_EXTRA_DATA_SIZE;
    }

    /**
     * Gets min gas limit.
     *
     * @return the min gas limit
     */
    public int getMIN_GAS_LIMIT() {
        return MIN_GAS_LIMIT;
    }

    /**
     * Gets gas limit bound divisor.
     *
     * @return the gas limit bound divisor
     */
    public int getGAS_LIMIT_BOUND_DIVISOR() {
        return GAS_LIMIT_BOUND_DIVISOR;
    }

    /**
     * Gets minimum difficulty.
     *
     * @return the minimum difficulty
     */
    public BigInteger getMINIMUM_DIFFICULTY() {
        return MINIMUM_DIFFICULTY;
    }

    /**
     * Gets difficulty bound divisor.
     *
     * @return the difficulty bound divisor
     */
    public BigInteger getDIFFICULTY_BOUND_DIVISOR() {
        return DIFFICULTY_BOUND_DIVISOR;
    }

    /**
     * Gets exp difficulty period.
     *
     * @return the exp difficulty period
     */
    public int getEXP_DIFFICULTY_PERIOD() {
        return EXP_DIFFICULTY_PERIOD;
    }

    /**
     * Gets uncle generation limit.
     *
     * @return the uncle generation limit
     */
    public int getUNCLE_GENERATION_LIMIT() {
        return UNCLE_GENERATION_LIMIT;
    }

    /**
     * Gets uncle list limit.
     *
     * @return the uncle list limit
     */
    public int getUNCLE_LIST_LIMIT() {
        return UNCLE_LIST_LIMIT;
    }

    /**
     * Gets best number diff limit.
     *
     * @return the best number diff limit
     */
    public int getBEST_NUMBER_DIFF_LIMIT() {
        return BEST_NUMBER_DIFF_LIMIT;
    }

    /**
     * Gets block reward.
     *
     * @return the block reward
     */
    public BigInteger getBLOCK_REWARD() {
        return BLOCK_REWARD;
    }

    /**
     * Gets max contract szie.
     *
     * @return the max contract szie
     */
    public int getMAX_CONTRACT_SZIE() {
        return Integer.MAX_VALUE;
    }

    /**
     * Introduced in the Homestead release
     *
     * @return the boolean
     */
    public boolean createEmptyContractOnOOG() {
        return true;
    }

    /**
     * New DELEGATECALL opcode introduced in the Homestead release. Before Homestead this opcode should generate
     * exception
     *
     * @return the boolean
     */
    public boolean hasDelegateCallOpcode() {
        return false;
    }

    /**
     * Introduced in the Homestead release
     *
     * @return the secp 256 k 1 n
     */
    public static BigInteger getSECP256K1N() {
        return SECP256K1N;
    }
}
