package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.CoreTransactionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Core transaction dao.
 */
@Mapper
public interface CoreTransactionDao extends BaseDao<CoreTransactionPO> {

    /**
     * query by tx id
     *
     * @param txId      the tx id
     * @param forUpdate the for update
     * @return core transaction po
     */
    CoreTransactionPO queryByTxId(@Param("txId")String txId,@Param("forUpdate")boolean forUpdate);

    /**
     * query by txs
     *
     * @param txIdList the tx id list
     * @return list
     */
    List<CoreTransactionPO> queryByTxIds(List<String> txIdList);

    /**
     * save tx execute reslult
     *
     * @param txId         the tx id
     * @param executResult the execut result
     * @param errorCode    the error code
     * @param errorMsg     the error msg
     * @param blockHeight  the block height
     * @return int
     */
    int saveExecuteResultAndHeight(@Param("txId")String txId,@Param("executResult")String executResult,@Param("errorCode")String errorCode, @Param("errorMsg")String errorMsg, @Param("blockHeight")Long blockHeight);

    /**
     * update sign datas
     *
     * @param txId      the tx id
     * @param signDatas the sign datas
     * @return int
     */
    int updateSignDatas(@Param("txId")String txId,@Param("signDatas")String signDatas);
}
