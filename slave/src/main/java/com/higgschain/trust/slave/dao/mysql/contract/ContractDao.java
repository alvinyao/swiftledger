package com.higgschain.trust.slave.dao.mysql.contract;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.contract.ContractPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Contract dao.
 */
@Mapper public interface ContractDao extends BaseDao<ContractPO> {

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(List<ContractPO> list);

    /**
     * query contract by address
     *
     * @param address the address
     * @return contract po
     */
    ContractPO queryByAddress(@Param("address") String address);

    /**
     * Query list.
     *
     * @param height     the height
     * @param txId       the tx id
     * @param startIndex the start index
     * @param endIndex   the end index
     * @return list
     */
    List<ContractPO> query(@Param("height") Long height, @Param("txId") String txId, @Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex);

    /**
     * get query count
     *
     * @param height the height
     * @param txId   the tx id
     * @return query count
     */
    Long getQueryCount(@Param("height") Long height, @Param("txId") String txId);

    /**
     * query by txId and action index
     *
     * @param txId        the tx id
     * @param actionIndex the action index
     * @return contract po
     */
    ContractPO queryByTxId(@Param("txId") String txId,@Param("actionIndex") int actionIndex);
}
