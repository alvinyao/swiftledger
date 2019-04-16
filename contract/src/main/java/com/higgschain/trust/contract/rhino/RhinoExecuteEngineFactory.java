package com.higgschain.trust.contract.rhino;

import com.higgschain.trust.contract.ExecuteConfig;
import com.higgschain.trust.contract.ExecuteEngine;
import com.higgschain.trust.contract.ExecuteEngineFactory;

import java.util.Map;

/**
 * The type Rhino execute engine factory.
 *
 * @author duhongming
 * @date 2018 /6/6
 */
public class RhinoExecuteEngineFactory implements ExecuteEngineFactory {

    private static final String engineName = "javascript";

    static {
        TrustContextFactory.install();
    }

    /**
     * Instantiates a new Rhino execute engine factory.
     */
    public RhinoExecuteEngineFactory() {

    }

    @Override
    public String getEngineName() {
        return engineName;
    }

    @Override
    public ExecuteEngine createExecuteEngine(String code, Map<String, Object> variables, ExecuteConfig executeConfig) {
        return new RhinoExecuteEngine(code, variables, executeConfig);
    }
}
