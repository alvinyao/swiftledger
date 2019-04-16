package com.higgschain.trust.contract;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * contract runtime Context
 *
 * @author duhongming
 */
@Slf4j public final class ExecuteContext {

    private static final ThreadLocal<ExecuteContext> currentExecuteContext = new ThreadLocal();

    private ContractEntity contract;
    private String stateInstanceKey;
    private ContractStateStore stateStore;
    private ExecuteContextData contextData;
    private boolean tryInitialization;
    private boolean onlyQuery;

    private ExecuteContext() {
        currentExecuteContext.set(this);
    }

    /**
     * Gets current.
     *
     * @return the current
     */
    public static ExecuteContext getCurrent() {
        ExecuteContext context = currentExecuteContext.get();
        return context;
    }

    /**
     * New context execute context.
     *
     * @return the execute context
     */
    public static ExecuteContext newContext() {
        ExecuteContext context = new ExecuteContext();
        return context;
    }

    /**
     * New context execute context.
     *
     * @param data the data
     * @return the execute context
     */
    public static ExecuteContext newContext(ExecuteContextData data) {
        ExecuteContext context = newContext();
        context.contextData = data;
        return context;
    }

    /**
     * Clear.
     */
    public static void Clear() {
        currentExecuteContext.remove();
    }

    /**
     * Gets data.
     *
     * @param name the name
     * @return the data
     */
    public Object getData(String name) {
        return this.contextData.get(name);
    }

    /**
     * Gets contract.
     *
     * @return the contract
     */
    public ContractEntity getContract() {
        return this.contract;
    }

    /**
     * Sets contract.
     *
     * @param contract the contract
     * @return the contract
     */
    public ExecuteContext setContract(ContractEntity contract) {
        this.contract = contract;
        return this;
    }

    /**
     * Gets state instance key.
     *
     * @return the state instance key
     */
    public String getStateInstanceKey() {
        return StringUtils.isEmpty(this.stateInstanceKey) ? this.contract.getAddress() : this.stateInstanceKey;
    }

    /**
     * Sets state instance key.
     *
     * @param instanceKey the instance key
     * @return the state instance key
     */
    public ExecuteContext setStateInstanceKey(String instanceKey) {
        this.stateInstanceKey = instanceKey;
        return this;
    }

    /**
     * Gets state store.
     *
     * @return the state store
     */
    public ContractStateStore getStateStore() {
        return stateStore;
    }

    /**
     * Sets db state store.
     *
     * @param stateStore the state store
     */
    public void setDbStateStore(ContractStateStore stateStore) {
        this.stateStore = stateStore;
    }

    /**
     * Gets context data.
     *
     * @return the context data
     */
    public ExecuteContextData getContextData() {
        return contextData;
    }

    /**
     * Gets context data.
     *
     * @param <T>    the type parameter
     * @param tClazz the t clazz
     * @return the context data
     */
    public <T extends ExecuteContextData> T getContextData(Class<T> tClazz) {
        return (T) contextData;
    }

    /**
     * Is try initialization boolean.
     *
     * @return the boolean
     */
    public boolean isTryInitialization() {
        return tryInitialization;
    }

    /**
     * Sets try initialization.
     *
     * @param tryInitialization the try initialization
     */
    public void setTryInitialization(boolean tryInitialization) {
        this.tryInitialization = tryInitialization;
    }

    /**
     * Is only query boolean.
     *
     * @return the boolean
     */
    public boolean isOnlyQuery() {
        return onlyQuery;
    }

    /**
     * Sets only query.
     *
     * @param onlyQuery the only query
     */
    public void setOnlyQuery(boolean onlyQuery) {
        this.onlyQuery = onlyQuery;
    }

    /**
     * Require.
     *
     * @param self       the self
     * @param isRequired the is required
     * @param message    the message
     */
    public static void require(Object self, Boolean isRequired, String message){
        if (!isRequired) {
            throw new SmartContractException(message);
        }
    }

    /**
     * Exception.
     *
     * @param self    the self
     * @param message the message
     */
    public static void exception(Object self, String message) {
        throw new SmartContractException(message);
    }

    /**
     * Gets logger.
     *
     * @return the logger
     */
    public static Logger getLogger() {
        return log;
    }
}
