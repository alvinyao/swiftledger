package com.higgschain.trust.slave.metrics;

import com.codahale.metrics.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Block metric set.
 *
 * @author duhongming
 * @date 2019 /1/3
 */
public class BlockMetricSet {

    private final Timer blockTimer;
    private final Histogram histogram;

    private final ValuedGauge<Long> totalCountGauge;
    private final ValuedGauge<Long> perSecondCountGauge;

    private long lastRecordCount;
    private long currentTotalCount;

    /**
     * Instantiates a new Block metric set.
     */
    public BlockMetricSet() {
        blockTimer = new Timer();
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

        currentTotalCount = blockTimer.getCount();
        totalCountGauge.setValue(currentTotalCount);
        long perSecondCount = currentTotalCount - lastRecordCount;
        lastRecordCount = currentTotalCount;
        perSecondCountGauge.setValue(perSecondCount);
        histogram.update(perSecondCount);

        Snapshot snapshot = blockTimer.getSnapshot();
        metrics.put("block.timer.max", new ValuedGauge<>(snapshot.getMax()));
        metrics.put("timer.mean", new ValuedGauge<>(snapshot.getMean()));

        snapshot = histogram.getSnapshot();
        metrics.put("block.second.max", new ValuedGauge<>(snapshot.getMax()));
        metrics.put("block.second.mean", new ValuedGauge<>(snapshot.getMean()));

        metrics.put("block.second.current", perSecondCountGauge);
        metrics.put("block.total", totalCountGauge);
        return metrics;
    }

    /**
     * Time timer . context.
     *
     * @return the timer . context
     */
    public Timer.Context time() {
        return blockTimer.time();
    }

    /**
     * Time.
     *
     * @param run the run
     */
    public void time(Runnable run) {
        blockTimer.time(run);
    }
}
