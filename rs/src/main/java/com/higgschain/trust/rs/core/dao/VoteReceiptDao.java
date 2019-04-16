package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.VoteReceiptPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Vote receipt dao.
 */
@Mapper public interface VoteReceiptDao extends BaseDao<VoteReceiptPO> {

    /**
     * query by transaction id,return list
     *
     * @param txId the tx id
     * @return list
     */
    List<VoteReceiptPO> queryByTxId(@Param("txId") String txId);

    /**
     * query vote-receipt by transaction-id and voter rs-name
     *
     * @param txId  the tx id
     * @param voter the voter
     * @return vote receipt po
     */
    VoteReceiptPO queryForVoter(@Param("txId") String txId, @Param("voter") String voter);

    /**
     * batch insert datas
     *
     * @param list the list
     * @return int
     */
    int batchAdd(List<VoteReceiptPO> list);
}
