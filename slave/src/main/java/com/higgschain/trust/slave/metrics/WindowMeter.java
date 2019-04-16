package com.higgschain.trust.slave.metrics;

import com.codahale.metrics.Meter;

/**
 * The type Window meter.
 *
 * @author duhongming
 * @date 2018 /12/28
 */
public class WindowMeter extends Meter {

    private ResetOnGetCounter winCounter;
    private double maxCountPerWindow;

    /**
     * Instantiates a new Window meter.
     */
    public WindowMeter() {
        winCounter = new ResetOnGetCounter();
    }

    @Override
    public void mark(long n) {
        winCounter.inc(n);
        super.mark(n);
    }

    /**
     * Gets count and reset.
     *
     * @return the count and reset
     */
    public double getCountAndReset() {
        double count = winCounter.getCount();
        maxCountPerWindow = Math.max(maxCountPerWindow, count);
        return count;
    }

    /**
     * Gets max count per window.
     *
     * @return the max count per window
     */
    public double getMaxCountPerWindow() {
        return maxCountPerWindow;
    }
}
