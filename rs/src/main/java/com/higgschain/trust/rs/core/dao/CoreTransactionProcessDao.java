package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.CoreTransactionProcessPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Core transaction process dao.
 */
@Mapper
public interface CoreTransactionProcessDao extends BaseDao<CoreTransactionProcessPO> {

    /**
     * query by tx id
     *
     * @param txId   the tx id
     * @param status the status
     * @return core transaction process po
     */
    CoreTransactionProcessPO queryByTxId(@Param("txId") String txId, @Param("status") String status);

    /**
     * query by status rowNum and count
     *
     * @param status the status
     * @param rowNum the row num
     * @param count  the count
     * @return list
     */
    List<CoreTransactionProcessPO> queryByStatus(@Param("status") String status, @Param("rowNum") int rowNum, @Param("count") int count);

    /**
     * update status by form->to
     *
     * @param txId the tx id
     * @param from the from
     * @param to   the to
     * @return int
     */
    int updateStatus(@Param("txId") String txId, @Param("from") String from, @Param("to") String to);

    /**
     * delete coreTxProcess for END status
     *
     * @return int
     */
    // TODO int not means the recordes be delete
    int deleteEnd();
}
