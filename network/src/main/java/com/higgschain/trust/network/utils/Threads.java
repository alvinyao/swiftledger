package com.higgschain.trust.network.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;

import java.util.concurrent.ThreadFactory;

/**
 * The type Threads.
 *
 * @author duhongming
 * @date 2018 /9/5
 */
public class Threads {
    /**
     * Named threads thread factory.
     *
     * @param pattern the pattern
     * @param log     the log
     * @return the thread factory
     */
    public static ThreadFactory namedThreads(String pattern, Logger log) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .setThreadFactory(r -> new Thread(r))
                .setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception on " + t.getName(), e))
                .build();
    }

    /**
     * Named threads thread factory.
     *
     * @param pattern the pattern
     * @return the thread factory
     */
    public static ThreadFactory namedThreads(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .setThreadFactory(r -> new Thread(r))
                .build();
    }
}
