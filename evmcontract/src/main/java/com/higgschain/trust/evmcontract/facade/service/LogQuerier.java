package com.higgschain.trust.evmcontract.facade.service;

import com.higgschain.trust.evmcontract.core.Bloom;
import com.higgschain.trust.evmcontract.vm.LogInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Log querier.
 *
 * @author Chen Jiawei
 * @date 2018 -11-26
 */
public class LogQuerier {
    private LogFilter logFilter;

    /**
     * Instantiates a new Log querier.
     */
    public LogQuerier() {
        logFilter = new LogFilter();
    }

    /**
     * With contract address.
     *
     * @param contractAddress the contract address
     */
    public void withContractAddress(byte[] contractAddress) {
        logFilter.withContractAddress(contractAddress);
    }

    /**
     * With topic.
     *
     * @param topic the topic
     */
    public void withTopic(byte[] topic) {
        logFilter.withTopic(topic);
    }

    /**
     * Match list.
     *
     * @param txBloom  the tx bloom
     * @param logInfos the log infos
     * @return the list
     */
    public List<LogInfo> match(Bloom txBloom, List<LogInfo> logInfos) {
        List<LogInfo> logInfoList = new ArrayList<>();

        if (logFilter.matchBloom(txBloom)) {
            for (LogInfo logInfo : logInfos) {
                if (logFilter.matchBloom(logInfo.getBloom()) && logFilter.matchesExactly(logInfo)) {
                    logInfoList.add(logInfo);
                }
            }
        }

        return logInfoList;
    }
}
