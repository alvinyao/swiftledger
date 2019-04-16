package com.higgschain.trust.network.eventbus;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type Async event bus.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
public class AsyncEventBus extends EventBus {

    /**
     * Instantiates a new Async event bus.
     *
     * @param executor the executor
     */
    public AsyncEventBus(ThreadPoolExecutor executor) {
        this(null, executor);
    }

    /**
     * Instantiates a new Async event bus.
     *
     * @param exceptionHandler the exception handler
     * @param executor         the executor
     */
    public AsyncEventBus(EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor) {
        this("default-async", exceptionHandler, executor);
    }

    /**
     * Instantiates a new Async event bus.
     *
     * @param busName          the bus name
     * @param exceptionHandler the exception handler
     * @param executor         the executor
     */
    public AsyncEventBus(String busName, EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor) {
        super(busName, exceptionHandler, executor);
    }
}
