package com.higgschain.trust.common.utils;

import org.rocksdb.Transaction;

/**
 * ThreadLocal utils
 *
 * @author tangfashuang
 */
public class ThreadLocalUtils {
    private static final ThreadLocal<Transaction> rocksTx = new ThreadLocal<>();

    /**
     * Put rocks tx.
     *
     * @param tx the tx
     */
    public static void putRocksTx(Transaction tx) {
        if (null != getRocksTx()) {
            clearRocksTx();
        }
        rocksTx.set(tx);
    }

    /**
     * Clear rocks tx.
     */
    public static void clearRocksTx() {
        rocksTx.remove();
    }

    /**
     * Gets rocks tx.
     *
     * @return the rocks tx
     */
    public static Transaction getRocksTx() {
        return rocksTx.get();
    }
}
