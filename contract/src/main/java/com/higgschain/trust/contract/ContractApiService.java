package com.higgschain.trust.contract;

/**
 * The type Contract api service.
 */
public class ContractApiService {

    /**
     * Instantiates a new Contract api service.
     */
    public ContractApiService() {
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    protected ExecuteContext getContext() {
        return ExecuteContext.getCurrent();
    }

    /**
     * Gets context data.
     *
     * @return the context data
     */
    public ExecuteContextData getContextData() {
        return getContext().getContextData();
    }

    /**
     * Gets context data.
     *
     * @param <T>    the type parameter
     * @param tClazz the t clazz
     * @return the context data
     */
    public <T extends ExecuteContextData> T getContextData(Class<T> tClazz) {
        return getContext().getContextData(tClazz);
    }
}
