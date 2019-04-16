package com.higgschain.trust.contract;

import com.higgschain.trust.contract.rhino.types.NativeJavaMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * contract state manager
 *
 * @author duhongming
 */
public class StateManager {
    private ExecuteContext executeContext;
    private ContractStateStore contractStateStore;
    private Map<String, Object> state;

    /**
     * Instantiates a new State manager.
     *
     * @param executeContext     the execute context
     * @param contractStateStore the contract state store
     */
    public StateManager(ExecuteContext executeContext,ContractStateStore contractStateStore) {
        state = new HashMap<>(8);
        this.executeContext = executeContext;
        this.contractStateStore = contractStateStore;
    }

    /**
     * Put state manager.
     *
     * @param name  the name
     * @param value the value
     * @return the state manager
     */
    public StateManager put(String name, Object value) {
        this.state.put(name, JsonHelper.clone(value));
        return this;
    }

    /**
     * Get object.
     *
     * @param name the name
     * @return the object
     */
    public Object get(String name) {
        Object obj = this.state.get(name);
        //from external store
        if(obj == null && contractStateStore != null){
            obj = contractStateStore.get(makeStateKey(executeContext.getStateInstanceKey(),name));
        }
        if (obj == null) {
            return null;
        }

        obj = JsonHelper.clone(obj);
        if (obj instanceof Map) {
            obj = new NativeJavaMap((Map)obj);
        }
        return obj;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public Map<String, Object> getState() {
        return this.state;
    }

    /**
     * flush to store
     */
    public void flush(){
        if(this.contractStateStore == null || this.state == null || this.state.isEmpty()){
            return;
        }
        List<String> keys = new ArrayList<>();
        int i = 0;
        for(String key : this.state.keySet()){
            Object value = this.state.get(key);
            String newKey = makeStateKey(executeContext.getStateInstanceKey(),key);
            contractStateStore.put(newKey,value);
            //max size
            if(i < 10){
                keys.add(newKey);
            }
            i++;
        }
        //save contract state`s all keys
        contractStateStore.put(executeContext.getStateInstanceKey(),keys);
    }

    /**
     * make key by contract address
     *
     * @param address the address
     * @param keyName the key name
     * @return string
     */
    public static String makeStateKey(String address,String keyName){
        if(StringUtils.isEmpty(address)){
            return null;
        }
        return address + "-" + keyName;
    }
}
