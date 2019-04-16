package com.higgschain.trust.consensus.p2pvalid.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Valid executor.
 *
 * @author cwy
 */
public class ValidExecutor {

    private Map<Class<?>, Function> registry = new HashMap<>();

    /**
     * Register.
     *
     * @param <T>      the type parameter
     * @param <U>      the type parameter
     * @param type     the type
     * @param function the function
     */
    public <T extends ValidCommand<U>, U extends Serializable> void register(Class<T> type, Function<T, U> function) {
        registry.put(type, function);
    }

    /**
     * Gets key set.
     *
     * @return the key set
     */
    public Set<Class<?>> getKeySet() {
        return registry.keySet();
    }

    /**
     * Register.
     *
     * @param <T>      the type parameter
     * @param <U>      the type parameter
     * @param type     the type
     * @param consumer the consumer
     */
    public <T extends ValidCommand<U>, U extends Serializable> void register(Class<T> type, Consumer<ValidBaseCommit> consumer) {
        registry.put(type, (Function<ValidBaseCommit, Void>)commit -> {
            consumer.accept(commit);
            return null;
        });
    }

    /**
     * Execute object.
     *
     * @param commit the commit
     * @return the object
     */
    public Object execute(ValidBaseCommit commit) {
        Function function = registry.get(commit.type());
        return function.apply(commit);
    }
}
