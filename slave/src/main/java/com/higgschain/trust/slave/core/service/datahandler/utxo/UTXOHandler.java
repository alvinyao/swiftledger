package com.higgschain.trust.slave.core.service.datahandler.utxo;

import com.higgschain.trust.slave.dao.po.utxo.TxOutPO;
import com.higgschain.trust.slave.model.bo.utxo.TxIn;
import com.higgschain.trust.slave.model.bo.utxo.UTXO;

import java.util.List;

/**
 * UTXO data Handler
 *
 * @author lingchao
 * @create 2018年03月27日16 :53
 */
public interface UTXOHandler {

    /**
     * query UTXO by txId, index and actionIndex
     *
     * @param txId        the tx id
     * @param index       the index
     * @param actionIndex the action index
     * @return utxo
     */
    UTXO queryUTXO(String txId, Integer index, Integer actionIndex);

    /**
     * query UTXO list
     *
     * @param inputList the input list
     * @return list
     */
    List<UTXO> queryUTXOList(List<TxIn> inputList);

    /**
     * batch insert
     *
     * @param txOutPOList the tx out po list
     * @return boolean
     */
    boolean batchInsert(List<TxOutPO> txOutPOList);

    /**
     * batch update
     *
     * @param txOutPOList the tx out po list
     * @return boolean
     */
    boolean batchUpdate(List<TxOutPO> txOutPOList);

}
