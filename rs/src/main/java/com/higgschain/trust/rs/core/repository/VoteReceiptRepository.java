package com.higgschain.trust.rs.core.repository;

import com.higgschain.trust.common.utils.BeanConvertor;
import com.higgschain.trust.rs.common.config.RsConfig;
import com.higgschain.trust.rs.core.api.enums.VoteResultEnum;
import com.higgschain.trust.rs.core.bo.VoteReceipt;
import com.higgschain.trust.rs.core.dao.VoteReceiptDao;
import com.higgschain.trust.rs.core.dao.po.VoteReceiptPO;
import com.higgschain.trust.rs.core.dao.rocks.VoteReceiptRocksDao;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Vote receipt repository.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
@Slf4j @Repository public class VoteReceiptRepository {
    @Autowired private RsConfig rsConfig;
    @Autowired private VoteReceiptDao voteReceiptDao;
    @Autowired private VoteReceiptRocksDao voteReceiptRocksDao;

    /**
     * create new vote-receipt
     *
     * @param voteReceipt the vote receipt
     */
    public void add(VoteReceipt voteReceipt) {
        VoteReceiptPO voteReceiptPO = BeanConvertor.convertBean(voteReceipt, VoteReceiptPO.class);
        voteReceiptPO.setVoteResult(voteReceipt.getVoteResult().getCode());
        if (rsConfig.isUseMySQL()) {
            try {
                voteReceiptDao.add(voteReceiptPO);
            } catch (DuplicateKeyException e) {
                log.error("[add.vote-receipt] is idempotent by txId:{}", voteReceiptPO.getTxId());
                throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT);
            }
        } else {
            voteReceiptRocksDao.save(voteReceiptPO);
        }
    }

    /**
     * batch create new vote-receipt
     *
     * @param voteReceipts the vote receipts
     */
    public void batchAdd(List<VoteReceipt> voteReceipts) {
        if(CollectionUtils.isEmpty(voteReceipts)){
            return;
        }

        List<VoteReceiptPO> list = new ArrayList<>(voteReceipts.size());
        for(VoteReceipt voteReceipt : voteReceipts){
            VoteReceiptPO po = new VoteReceiptPO();
            po.setTxId(voteReceipt.getTxId());
            po.setVoter(voteReceipt.getVoter());
            po.setSign(voteReceipt.getSign());
            po.setVoteResult(voteReceipt.getVoteResult().getCode());
            list.add(po);
        }
        if (rsConfig.isUseMySQL()) {
            try {
                voteReceiptDao.batchAdd(list);
            } catch (DuplicateKeyException e) {
                log.error("[add.vote-receipt] is idempotent ");
                throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT);
            }
        } else {
            voteReceiptRocksDao.batchInsert(list);
        }
    }

    /**
     * query vote-receipts by transaction id
     *
     * @param txId the tx id
     * @return list
     */
    public List<VoteReceipt> queryByTxId(String txId) {
        List<VoteReceiptPO> list;
        if (rsConfig.isUseMySQL()) {
            list = voteReceiptDao.queryByTxId(txId);
        } else {
            list = voteReceiptRocksDao.queryByTxId(txId);
        }

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<VoteReceipt> voteReceipts = new ArrayList<>(list.size());
        for (VoteReceiptPO po : list) {
            VoteReceipt voteReceipt = BeanConvertor.convertBean(po, VoteReceipt.class);
            voteReceipt.setVoteResult(VoteResultEnum.fromCode(po.getVoteResult()));
            voteReceipts.add(voteReceipt);
        }
        return voteReceipts;
    }

    /**
     * query vote-receipt by transaction-id and voter rs-name
     *
     * @param txId  the tx id
     * @param voter the voter
     * @return vote receipt
     */
    public VoteReceipt queryForVoter(String txId, String voter) {
        VoteReceiptPO po;
        if (rsConfig.isUseMySQL()) {
            po = voteReceiptDao.queryForVoter(txId, voter);
        } else {
            po = voteReceiptRocksDao.get(txId + "_" + voter);
        }

        if (null == po) {
            return null;
        }

        VoteReceipt voteReceipt = BeanConvertor.convertBean(po, VoteReceipt.class);
        voteReceipt.setVoteResult(VoteResultEnum.fromCode(po.getVoteResult()));
        return voteReceipt;
    }
}
