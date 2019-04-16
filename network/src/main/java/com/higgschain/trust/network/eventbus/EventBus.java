package com.higgschain.trust.network.eventbus;

import java.util.concurrent.Executor;

/**
 * The type Event bus.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
public class EventBus implements Bus {
    private static final String DEFAULT_BUS_NAME = "default";
    private static final String  DEFAULT_TOPIC = "default-topic";

    private final Registry registry = new Registry();
    private String busName;
    private final Dispatcher dispatcher;

    /**
     * Instantiates a new Event bus.
     */
    public EventBus() {
        this(DEFAULT_BUS_NAME, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    /**
     * Instantiates a new Event bus.
     *
     * @param busName the bus name
     */
    public EventBus(String busName) {
        this(busName, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    /**
     * Instantiates a new Event bus.
     *
     * @param exceptionHandler the exception handler
     */
    public EventBus(EventExceptionHandler exceptionHandler) {
        this(DEFAULT_BUS_NAME, exceptionHandler, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    /**
     * Instantiates a new Event bus.
     *
     * @param busName          the bus name
     * @param exceptionHandler the exception handler
     * @param executor         the executor
     */
    public EventBus(String busName, EventExceptionHandler exceptionHandler, Executor executor) {
        this.busName = busName;
        this.dispatcher = Dispatcher.newDispatcher(exceptionHandler, executor);
    }

    @Override
    public void register(Object subscriber) {
        this.registry.bind(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        this.registry.unbind(subscriber);
    }

    @Override
    public void post(Object event) {
        this.post(event, DEFAULT_TOPIC);
    }

    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispatch(this, registry, event, topic);
    }

    @Override
    public void close() {
        this.dispatcher.close();
    }

    @Override
    public String getBusName() {
        return this.busName;
    }
}
