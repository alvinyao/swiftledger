package com.higgschain.trust.slave.metrics;

import com.codahale.metrics.Gauge;

/**
 * The type Valued gauge.
 *
 * @param <T> the type parameter
 * @author duhongming
 * @date 2019 /1/3
 */
public class ValuedGauge<T> implements Gauge<T> {

    private T value;

    /**
     * Instantiates a new Valued gauge.
     */
    public ValuedGauge() {

    }

    /**
     * Instantiates a new Valued gauge.
     *
     * @param value the value
     */
    public ValuedGauge(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(T value) {
        this.value = value;
    }
}
