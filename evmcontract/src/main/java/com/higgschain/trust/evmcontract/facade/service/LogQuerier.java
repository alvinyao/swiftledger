package com.higgschain.trust.evmcontract.facade.service;

import com.higgschain.trust.evmcontract.core.Bloom;
import com.higgschain.trust.evmcontract.vm.LogInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Jiawei
 * @date 2018-11-26
 */
public class LogQuerier {
    private LogFilter logFilter;

    public LogQuerier() {
        logFilter = new LogFilter();
    }

    public void withContractAddress(byte[] contractAddress) {
        logFilter.withContractAddress(contractAddress);
    }

    public void withTopic(byte[] topic) {
        logFilter.withTopic(topic);
    }

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
