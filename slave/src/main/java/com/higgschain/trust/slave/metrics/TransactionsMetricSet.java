package com.higgschain.trust.slave.metrics;

import com.codahale.metrics.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Transactions metric set.
 *
 * @author duhongming
 * @date 2019 /1/3
 */
public class TransactionsMetricSet {
    private final Timer timer;
    private final Histogram histogram;

    private final ValuedGauge<Long> totalCountGauge;
    private final ValuedGauge<Long> perSecondCountGauge;

    private long lastRecordCount;
    private long currentTotalCount;

    /**
     * Instantiates a new Transactions metric set.
     */
    public TransactionsMetricSet() {
        timer = new Timer();
        histogram = new Histogram(new UniformReservoir());

        totalCountGauge = new ValuedGauge<>();
        perSecondCountGauge = new ValuedGauge<>();
    }

    /**
     * Gets metrics.
     *
     * @return the metrics
     */
    public Map<String, Metric> getMetrics() {
        final Map<String, Metric> metrics = new HashMap<>();

        currentTotalCount = timer.getCount();
        totalCountGauge.setValue(currentTotalCount);
        long perSecondCount = currentTotalCount - lastRecordCount;
        lastRecordCount = currentTotalCount;
        perSecondCountGauge.setValue(perSecondCount);
        histogram.update(perSecondCount);

        metrics.put("transactions.second.current", perSecondCountGauge);
        metrics.put("transactions.total", totalCountGauge);

        Snapshot snapshot = timer.getSnapshot();
        metrics.put("transactions.timer.max", new ValuedGauge<>(snapshot.getMax()));
        metrics.put("transactions.timer.mean", new ValuedGauge<>(snapshot.getMean()));

        snapshot = histogram.getSnapshot();
        metrics.put("transactions.second.max", new ValuedGauge<>(snapshot.getMax()));
        metrics.put("transactions.second.mean", new ValuedGauge<>(snapshot.getMean()));

        return metrics;
    }

    /**
     * Time timer . context.
     *
     * @return the timer . context
     */
    public Timer.Context time() {
        return timer.time();
    }

    /**
     * Time.
     *
     * @param run the run
     */
    public void time(Runnable run) {
        timer.time(run);
    }
}
