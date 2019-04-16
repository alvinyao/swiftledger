package com.higgschain.trust.network;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

/**
 * The type Event demo.
 *
 * @author duhongming
 * @date 2018 /9/10
 */
public class EventDemo {

    private static Logger log = LoggerFactory.getLogger(EventDemo.class);

    /**
     * The type Event message.
     */
    public static class EventMessage {
        /**
         * The Event type.
         */
        final int eventType;
        /**
         * The Message.
         */
        final Object message;

        /**
         * Instantiates a new Event message.
         *
         * @param eventType the event type
         * @param message   the message
         */
        public EventMessage(int eventType, Object message) {
            this.eventType = eventType;
            this.message = message;
        }

        /**
         * Gets event type.
         *
         * @return the event type
         */
        public int getEventType() {
            return eventType;
        }

        /**
         * Gets message.
         *
         * @return the message
         */
        public Object getMessage() {
            return message;
        }
    }

    /**
     * DeadEvent：死信（没有订阅者关注的事件）对象
     * EventBus会把所有发布后没有监听者处理的事件包装为DeadEvent
     * @author Sunday
     *
     */
    private static class DeadEventsListener {
        /**
         * Handle dead event.
         *
         * @param deadEvent the dead event
         */
        @Subscribe
        public void handleDeadEvent(DeadEvent deadEvent) {
            log.error("DEAD EVENT: {}", deadEvent.getEvent());
        }

        /**
         * Handle event.
         *
         * @param message the message
         * @throws InterruptedException the interrupted exception
         */
        @Subscribe
        public void handleEvent(EventMessage message) throws InterruptedException {
//            TimeUnit.MILLISECONDS.sleep(2);
            log.info(message.toString());
        }
    }

    /**
     * The type Listener.
     */
    class Listener {

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        final EventBus eventBus = new AsyncEventBus("network", Executors.newFixedThreadPool(1));
        eventBus.register(new DeadEventsListener());

        int count = 0;
        while (true) {
            count++;
            if (count >= 2) {
                break;
            }
            eventBus.post("kdkdk");
            eventBus.post(new EventMessage(1, "hello"));
        }

        double d = 89999999.98999991d;
        System.out.println(d);

    }
}
