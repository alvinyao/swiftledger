package com.higgschain.trust.network.message.handler;

import com.higgschain.trust.network.message.handler.annotation.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The type Message handler registry.
 *
 * @author duhongming
 * @date 2018 /9/18
 */
public class MessageHandlerRegistry {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ConcurrentHashMap<String, Function<Object, ?>> handlerMap = new ConcurrentHashMap<>();

    /**
     * Bind.
     *
     * @param handler the handler
     */
    public void bind(Object handler) {
        List<Method> actionMethods = getActionMethods(handler);
        for (Method method : actionMethods) {
            final MessageHandler action = method.getDeclaredAnnotation(MessageHandler.class);
            String actionName = action.name();
            add(actionName, request -> {
                try {
                    return method.invoke(handler, request);
                } catch (IllegalAccessException e) {
                    log.error("Invoke message handler {} error, {}", actionName, e);
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    log.error("Invoke message handler {} error, {}", actionName, e);
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Add.
     *
     * @param actionName the action name
     * @param func       the func
     */
    public void add(String actionName, Function func) {
        handlerMap.putIfAbsent(actionName, func);
    }

    /**
     * Get function.
     *
     * @param name the name
     * @return the function
     */
    public Function get(String name) {
        return handlerMap.get(name);
    }

    private List<Method> getActionMethods(Object handler) {
        final List<Method> methods = new ArrayList<>();
        Class<?> temp = handler.getClass();
        while (temp != null) {
            Method[] declaredMethods = temp.getDeclaredMethods();
            Arrays.stream(declaredMethods)
                    .filter(method -> method.isAnnotationPresent(MessageHandler.class)
                            && method.getParameterCount() == 1 && method.getModifiers() == Modifier.PUBLIC)
                    .forEach(methods::add);
            temp = temp.getSuperclass();
        }
        return methods;
    }
}
