package com.higgschain.trust.network.eventbus;

import com.higgschain.trust.network.eventbus.annotation.Subscribe;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Registry.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
class Registry {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>> subscribeContainer = new ConcurrentHashMap<>();

    /**
     * Bind.
     *
     * @param subscriber the subscriber
     */
    public void bind(Object subscriber) {
        List<Method> subscribeMethods = getSubscribeMethods(subscriber);
        for(Method m : subscribeMethods) {
            tierSubscriber(subscriber, m);
        }
    }

    /**
     * Unbind.
     *
     * @param subscriber the subscriber
     */
    public void unbind(Object subscriber) {
        subscribeContainer.forEach((key, queue) -> {
            queue.forEach(s -> {
                if (s.getSubscribeObject() == subscriber) {
                    s.setDisable(true);
                }
            });
        });
    }

    /**
     * Scan subscriber concurrent linked queue.
     *
     * @param topic the topic
     * @return the concurrent linked queue
     */
    public ConcurrentLinkedQueue<Subscriber> scanSubscriber(final String topic) {
        return subscribeContainer.get(topic);
    }

    private void tierSubscriber(Object subscriber, Method method) {
        final Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
        String topic = subscribe.topic();
        subscribeContainer.computeIfAbsent(topic, key -> new ConcurrentLinkedQueue<>());
        subscribeContainer.get(topic).add(new Subscriber(subscriber, method));
    }

    private List<Method> getSubscribeMethods(Object subscriber) {
        final List<Method> methods = new ArrayList<>();
        Class<?> temp = subscriber.getClass();
        while (temp != null) {
            Method[] declaredMethods = temp.getDeclaredMethods();
            Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(Subscribe.class)
                    && method.getParameterCount() == 1 && method.getModifiers() == Modifier.PUBLIC)
                .forEach(methods::add);
            temp = temp.getSuperclass();
        }
        return methods;
    }
}
