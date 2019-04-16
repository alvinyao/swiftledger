package com.higgschain.trust.slave.dao.mysql.transaction;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.transaction.TransactionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TransactionPO data deal dao
 *
 * @author lingchao
 * @create 2018年03月27日20 :10
 */
@Mapper public interface TransactionDao extends BaseDao<TransactionPO> {

    /**
     * query transaction by transaction id
     *
     * @param txId the tx id
     * @return transaction po
     */
    TransactionPO queryByTxId(String txId);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(List<TransactionPO> list);

    /**
     * query by block height
     *
     * @param blockHeight the block height
     * @return list
     */
    List<TransactionPO> queryByBlockHeight(@Param("blockHeight") Long blockHeight);

    /**
     * query by tx ids
     *
     * @param txIds the tx ids
     * @return list
     */
    List<TransactionPO> queryByTxIds(@Param("txIds") List<String> txIds);

    /**
     * query transactions with condition
     *
     * @param blockHeight the block height
     * @param txId        the tx id
     * @param sender      the sender
     * @param start       the start
     * @param end         the end
     * @return list
     */
    List<TransactionPO> queryTxWithCondition(@Param("height") Long blockHeight,
        @Param("txId") String txId, @Param("sender") String sender,
        @Param("start") Integer start, @Param("end") Integer end);

    /**
     * count transaction with condition
     *
     * @param blockHeight the block height
     * @param txId        the tx id
     * @param sender      the sender
     * @return long
     */
    long countTxWithCondition(@Param("height") Long blockHeight,
        @Param("txId") String txId, @Param("sender") String sender);
}
