package com.higgschain.trust.slave.core.service.version;

import com.higgschain.trust.slave.api.enums.VersionEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Tx processor holder.
 *
 * @author WangQuanzhou
 * @desc this class maintain a map which store the relationship between version and exact processor
 * @date 2018 /3/28 17:59
 */
@Component public class TxProcessorHolder {
    private Map<VersionEnum, TransactionProcessor> versionProcessorMap = new HashMap<>();

    /**
     * Regist verison processor.
     *
     * @param version   the version
     * @param processor the processor
     */
    // register processor
    public void registVerisonProcessor(VersionEnum version, TransactionProcessor processor) {
        versionProcessorMap.put(version, processor);
    }

    /**
     * Gets processor.
     *
     * @param version the version
     * @return the processor
     */
    // get processor
    public TransactionProcessor getProcessor(VersionEnum version) {
        return versionProcessorMap.get(version);
    }
}
