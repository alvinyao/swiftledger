package com.higgschain.trust.slave.metrics;

import com.higgschain.trust.network.utils.Threads;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Metrics reporter.
 *
 * @author duhongming
 * @date 2018 /12/27
 */
public class MetricsReporter {

    private final ScheduledExecutorService executor;
    private final Runnable reporter;

    /**
     * Instantiates a new Metrics reporter.
     *
     * @param reporter the reporter
     */
    public MetricsReporter(Runnable reporter) {
        this.reporter = reporter;
        executor = new ScheduledThreadPoolExecutor(1, Threads.namedThreads("MetricsReporter"));
    }

    /**
     * Start.
     *
     * @param period the period
     * @param unit   the unit
     */
    public void start(long period, TimeUnit unit) {
        executor.scheduleAtFixedRate(this::report, period, period, unit);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        executor.shutdown();
    }

    private void report() {
        try {
            reporter.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
