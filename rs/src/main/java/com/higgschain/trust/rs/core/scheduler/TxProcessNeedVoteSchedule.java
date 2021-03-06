package com.higgschain.trust.rs.core.scheduler;

import com.higgschain.trust.rs.core.api.CoreTransactionService;
import com.higgschain.trust.rs.core.api.enums.CoreTxStatusEnum;
import com.higgschain.trust.rs.core.dao.po.CoreTransactionProcessPO;
import com.higgschain.trust.rs.core.repository.CoreTxRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Tx process need vote schedule.
 */
@ConditionalOnProperty(name = "higgs.trust.isSlave", havingValue = "true", matchIfMissing = true)
@Service
@Slf4j
public class TxProcessNeedVoteSchedule {
    @Autowired private CoreTransactionService coreTransactionService;
    @Autowired private CoreTxRepository coreTxRepository;

    private int pageNo = 1;
    private int pageSize = 100;
    /**
     * rocks db seek key:01-tx_id
     */
    private String lastPreKey = null;

    /**
     * Exe.
     */
    @Scheduled(fixedDelayString = "${rs.core.schedule.processNeedVote:3000}") public void exe() {
        List<CoreTransactionProcessPO> list =
            coreTxRepository.queryByStatus(CoreTxStatusEnum.NEED_VOTE, (pageNo - 1) * pageSize, pageSize, lastPreKey);
        if (CollectionUtils.isEmpty(list)) {
            pageNo = 1;
            lastPreKey = null;
            return;
        }
        for (CoreTransactionProcessPO po : list) {
            try {
                coreTransactionService.processNeedVoteTx(po.getTxId());
            } catch (Throwable e) {
                log.error("has error", e);
            }
        }
        lastPreKey = list.get(list.size() - 1).getTxId();
        pageNo++;
    }
}
