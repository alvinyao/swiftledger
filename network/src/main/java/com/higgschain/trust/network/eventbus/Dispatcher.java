package com.higgschain.trust.network.eventbus;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * The type Dispatcher.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
public class Dispatcher {
    private final Executor executorService;
    private final EventExceptionHandler exceptionHandler;

    /**
     * The constant SEQ_EXECUTOR_SERVICE.
     */
    public static final Executor SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;
    /**
     * The constant PER_THREAD_EXECUTOR_SERVICE.
     */
    public static final Executor PER_THREAD_EXECUTOR_SERVICE = PerThreadExecutorService.INSTANCE;

    /**
     * Instantiates a new Dispatcher.
     *
     * @param executorService  the executor service
     * @param exceptionHandler the exception handler
     */
    public Dispatcher(Executor executorService, EventExceptionHandler exceptionHandler) {
        this.executorService = executorService;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Dispatch.
     *
     * @param bus      the bus
     * @param registry the registry
     * @param event    the event
     * @param topic    the topic
     */
    public void dispatch(Bus bus, Registry registry, Object event, String topic) {
        ConcurrentLinkedQueue<Subscriber> subscribers = registry.scanSubscriber(topic);
        if (subscribers == null) {
            if (exceptionHandler != null) {
                return;
            }
            return;
        }
        subscribers.stream()
            .filter(subscriber -> !subscriber.isDisable())
            .filter(subscriber -> {
                Method method = subscriber.getMethod();
                Class<?> clazz = method.getParameterTypes()[0];
                return clazz.isAssignableFrom(event.getClass());
            })
            .forEach(subscriber -> realInvokeSubscribe(subscriber, event, bus));
    }

    /**
     * Close.
     */
    public void close() {
        if (executorService instanceof ExecutorService) {
            ((ExecutorService) executorService).shutdown();
        }
    }

    /**
     * New dispatcher dispatcher.
     *
     * @param exceptionHandler the exception handler
     * @param executor         the executor
     * @return the dispatcher
     */
    static Dispatcher newDispatcher(EventExceptionHandler exceptionHandler, Executor executor) {
        return new Dispatcher(executor, exceptionHandler);
    }

    /**
     * Seq dispatcher dispatcher.
     *
     * @param exceptionHandler the exception handler
     * @return the dispatcher
     */
    static Dispatcher seqDispatcher(EventExceptionHandler exceptionHandler) {
        return new Dispatcher(SEQ_EXECUTOR_SERVICE, exceptionHandler);
    }

    /**
     * Per thread dispatcher dispatcher.
     *
     * @param exceptionHandler the exception handler
     * @return the dispatcher
     */
    static Dispatcher perThreadDispatcher(EventExceptionHandler exceptionHandler) {
        return new Dispatcher(PER_THREAD_EXECUTOR_SERVICE, exceptionHandler);
    }

    private void realInvokeSubscribe(Subscriber subscriber, Object event, Bus bus) {
        Method method = subscriber.getMethod();
        Object subscribeObject = subscriber.getSubscribeObject();
        executorService.execute(() -> {
            try {
                method.invoke(subscribeObject, event);
            } catch (Exception e) {
                if (exceptionHandler != null) {
                    return;
                }
                e.printStackTrace();
            }
        });
    }

    private static class SeqExecutorService implements Executor {

        private static final SeqExecutorService INSTANCE = new SeqExecutorService();

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    /**
     * 每个线程负责一次消息推送
     */
    private static class PerThreadExecutorService implements Executor{

        private final static PerThreadExecutorService INSTANCE = new PerThreadExecutorService();

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

//    private static class BaseEventContext implements EventContext {
//
//    }
}
