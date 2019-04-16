package com.higgschain.trust.contract;

import java.util.Map;

/**
 * The interface Execute engine factory.
 *
 * @author duhongming
 * @date 2018 /04/25
 */
public interface ExecuteEngineFactory {
    /**
     * Returns the full  name of the <code>ExecuteEngine</code>.
     *
     * @return The name of the engine implementation.
     */
    public String getEngineName();

    /**
     * Returns the instance of ExecuteEngine implementation
     *
     * @param code          the code
     * @param variables     the variables
     * @param executeConfig the execute config
     * @return execute engine
     */
    public ExecuteEngine createExecuteEngine(String code, Map<String, Object> variables, ExecuteConfig executeConfig);
}
