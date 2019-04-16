package com.higgschain.trust.slave.dao.mysql.utxo;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.utxo.TxOutPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TxOutBO data deal dao
 *
 * @author lingchao
 * @create 2018年03月27日20 :10
 */
@Mapper public interface TxOutDao extends BaseDao<TxOutPO> {
    /**
     * query txOut by txId, index and actionIndex
     *
     * @param txId        the tx id
     * @param index       the index
     * @param actionIndex the action index
     * @return tx out po
     */
    TxOutPO queryTxOut(@Param("txId") String txId, @Param("index") Integer index, @Param("actionIndex") Integer actionIndex);

    /**
     * batch insert
     *
     * @param txOutPOList the tx out po list
     * @return int
     */
    int batchInsert(List<TxOutPO> txOutPOList);

    /**
     * batch update
     *
     * @param txOutPOList the tx out po list
     * @return int
     */
    int batchUpdate(List<TxOutPO> txOutPOList);

    /**
     * query tx_out by tx_id
     *
     * @param txId the tx id
     * @return list
     */
    List<TxOutPO> queryByTxId(@Param("txId") String txId);

    /**
     * query tx_out by s_tx_id
     *
     * @param sTxId the s tx id
     * @return list
     */
    List<TxOutPO> queryBySTxId(@Param("sTxId") String sTxId);
}
