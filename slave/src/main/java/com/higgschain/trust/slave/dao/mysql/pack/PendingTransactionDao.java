package com.higgschain.trust.slave.dao.mysql.pack;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.pack.PendingTransactionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Pending transaction dao.
 */
@Mapper public interface PendingTransactionDao extends BaseDao<PendingTransactionPO> {

    /**
     * query pending transaction by tx id
     *
     * @param txId the tx id
     * @return pending transaction po
     */
    PendingTransactionPO queryByTxId(@Param("txId") String txId);

    /**
     * query pending transaction list by height
     *
     * @param height the height
     * @return list
     */
    List<PendingTransactionPO> queryByHeight(@Param("height") Long height);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(List<PendingTransactionPO> list);

    /**
     * query pending transaction
     *
     * @param txIds the tx ids
     * @return list
     */
    List<PendingTransactionPO> queryByTxIds(@Param("txIds") List<String> txIds);

    /**
     * delete by less than height
     *
     * @param height the height
     * @return int
     */
    int deleteLessThanHeight(@Param("height")Long height);

    /**
     * delete by height
     *
     * @param height the height
     * @return int
     */
    int deleteByHeight(@Param("height")Long height);
}
