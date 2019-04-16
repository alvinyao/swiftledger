package com.higgschain.trust.rs.core.scheduler;

import com.higgschain.trust.rs.core.api.enums.CoreTxStatusEnum;
import com.higgschain.trust.rs.core.dao.po.CoreTransactionProcessPO;
import com.higgschain.trust.rs.core.repository.CoreTxRepository;
import com.higgschain.trust.rs.core.task.TxIdBO;
import com.higgschain.trust.rs.core.task.TxIdProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Tx process init schedule.
 */
@ConditionalOnProperty(name = "higgs.trust.isSlave", havingValue = "true", matchIfMissing = true)
@Service
@Slf4j
public class TxProcessInitSchedule {
    @Autowired private CoreTxRepository coreTxRepository;
    @Autowired private TxIdProducer txIdProducer;

    private int pageNo = 1;
    @Value("${rs.core.schedule.initSize:200}") private int pageSize = 200;
    private int maxPageNo = 1000;
    /**
     * rocks db seek key:01-tx_id
     */
    private String lastPreKey = null;

    /**
     * Exe.
     */
    @Scheduled(fixedDelayString = "${rs.core.schedule.processInit:500}") public void exe() {
        List<CoreTransactionProcessPO> list =
            coreTxRepository.queryByStatus(CoreTxStatusEnum.INIT, (pageNo - 1) * pageSize, pageSize, lastPreKey);
        if (CollectionUtils.isEmpty(list) || pageNo == maxPageNo) {
            pageNo = 1;
            lastPreKey = null;
            return;
        }
        list.forEach(entry -> {
            txIdProducer.put(new TxIdBO(entry.getTxId(), CoreTxStatusEnum.INIT));
        });
        pageNo++;
    }
}
