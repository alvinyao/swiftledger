package com.higgschain.trust.slave.metrics;


import com.codahale.metrics.*;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.slave.metrics.jvm.CpuUsageGaugeSet;
import com.higgschain.trust.slave.metrics.jvm.MemoryUsageGaugeSet;
import com.higgschain.trust.slave.metrics.jvm.ThreadStatesGaugeSet;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * The type Trust metrics.
 *
 * @author duhongming
 * @date 2018 /12/27
 */
public class TrustMetrics {

    private Map<String, Object> reportedMetrics;

    private MetricRegistry register = new MetricRegistry();
    private MetricsReporter reporter;

    private final TransactionsMetricSet transactionsMetricSet;
    private final BlockMetricSet blockMetricSet;

    private static long startTime;

    static {
        startTime = System.currentTimeMillis();
    }

    private static TrustMetrics defaultTrustMetrics = new TrustMetrics();

    /**
     * Gets default.
     *
     * @return the default
     */
    public static TrustMetrics getDefault() {
        return defaultTrustMetrics;
    }

    private TrustMetrics() {
        transactionsMetricSet = new TransactionsMetricSet();
        blockMetricSet = new BlockMetricSet();
        registerMetrics();
        NetworkManage.installTrafficReporter(new NetworkTrafficReporter(this));
        reporter = new MetricsReporter(this::report);
    }

    private void registerMetrics() {
        register.register("memory", new MemoryUsageGaugeSet(new ArrayList<>(0)));
        register.register("cpu", new CpuUsageGaugeSet());
        register.register("thread", new ThreadStatesGaugeSet());
    }

    /**
     * Block block metric set.
     *
     * @return the block metric set
     */
    public BlockMetricSet block() {
        return blockMetricSet;
    }

    /**
     * Transactions transactions metric set.
     *
     * @return the transactions metric set
     */
    public TransactionsMetricSet transactions() {
        return transactionsMetricSet;
    }

    /**
     * Gets reporter.
     *
     * @return the reporter
     */
    public MetricsReporter getReporter() {
        return reporter;
    }

    /**
     * Gets reported metrics.
     *
     * @return the reported metrics
     */
    public Map<String, Object> getReportedMetrics() {
        return reportedMetrics;
    }

    /**
     * Start report.
     */
    public void startReport() {
        reporter.start(1, TimeUnit.SECONDS);
    }

    /**
     * Increment.
     *
     * @param metricName the metric name
     */
    public void increment(String metricName) {
        increment(metricName, 1);
    }

    /**
     * Increment.
     *
     * @param metricName the metric name
     * @param n          the n
     */
    public void increment(String metricName, long n) {
        register.counter(metricName).inc(n);
    }

    /**
     * Mark.
     *
     * @param metricName the metric name
     */
    public void mark(String metricName) {
        mark(metricName, 1);
    }

    /**
     * Mark.
     *
     * @param metricName the metric name
     * @param n          the n
     */
    public void mark(String metricName, long n) {
        register.meter(metricName).mark(n);
    }

    /**
     * Update.
     *
     * @param metricName the metric name
     * @param value      the value
     */
    public void update(String metricName, long value) {
        register.histogram(metricName).update(value);
    }

    /**
     * Gets register.
     *
     * @return the register
     */
    public MetricRegistry getRegister() {
        return register;
    }

    private void report() {
        Map map = new TreeMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("instance.uptime", System.currentTimeMillis() - startTime);
        report(map, register.getMetrics());
        report(map, blockMetricSet.getMetrics());
        report(map, transactionsMetricSet.getMetrics());

        reportedMetrics = map;
    }

    private void report(Map map, Map<String, Metric> metrics) {
        metrics.forEach((name, metric) -> {
            if (metric instanceof WindowMeter) {
                WindowMeter windowMeter = (WindowMeter) metric;
                map.put(name +".count.total", windowMeter.getCount());
                map.put(name +".count.second", windowMeter.getCountAndReset());
                map.put(name +".count.second.max", windowMeter.getMaxCountPerWindow());
                map.put(name +".count.mean-rate", windowMeter.getMeanRate());
            } else if (metric instanceof Meter) {
                Meter meter = (Meter) metric;
                map.put(name + ".count", meter.getCount());
                map.put(name + ".mean-rate", meter.getMeanRate());
                map.put(name + ".one-minute-rate", meter.getOneMinuteRate());
            } else if (metric instanceof Counter) {
                Counter counter = (Counter) metric;
                map.put(name + ".count", counter.getCount());
            } else if (metric instanceof Gauge) {
                Gauge gauge = (Gauge) metric;
                map.put(name, gauge.getValue());
            } else if (metric instanceof Timer) {
                Timer timer = (Timer) metric;
                map.put(name + "." + "timer.count", timer.getCount());
                Snapshot snapshot = timer.getSnapshot();
                map.put(name + "." + "timer.max", snapshot.getMax());
                map.put(name + "." + "timer.min", snapshot.getMin());
                map.put(name + "." + "timer.mean", snapshot.getMean());
                map.put(name + "." + "timer.median", snapshot.getMedian());
            }
        });
    }
}
