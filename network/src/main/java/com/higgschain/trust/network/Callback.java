package com.higgschain.trust.network;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * The type Callback.
 *
 * @author duhongming
 * @date 2018 /9/3
 */
public class Callback {
    private final String action;
    private final long timeout;
    private final CompletableFuture<byte[]> future;
    private final long time = System.currentTimeMillis();

    /**
     * Instantiates a new Callback.
     *
     * @param action  the action
     * @param timeout the timeout
     * @param future  the future
     */
    Callback(String action, Duration timeout, CompletableFuture<byte[]> future) {
        this.action = action;
        this.timeout = timeout != null ? timeout.toMillis() : 0;
        this.future = future;
    }

    /**
     * Time long.
     *
     * @return the long
     */
    public long time() {
        return time;
    }

    /**
     * Timeout long.
     *
     * @return the long
     */
    public long timeout() {
        return timeout;
    }

    /**
     * Complete.
     *
     * @param value the value
     */
    public void complete(byte[] value) {
        future.complete(value);
    }

    /**
     * Complete exceptionally.
     *
     * @param error the error
     */
    public void completeExceptionally(Throwable error) {
        future.completeExceptionally(error);
    }
}
