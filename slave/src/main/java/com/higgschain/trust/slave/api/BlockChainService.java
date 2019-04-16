package com.higgschain.trust.slave.api;

import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.api.enums.utxo.UTXOActionTypeEnum;
import com.higgschain.trust.slave.api.vo.*;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.utxo.TxIn;
import com.higgschain.trust.slave.model.bo.utxo.UTXO;

import java.util.List;

/**
 * The interface Block chain service.
 *
 * @author pengdi
 * @date
 */
public interface BlockChainService {

    /**
     * create transactions
     *
     * @param transactions the transactions
     * @return resp data
     */
    RespData<List<TransactionVO>> submitTransactions(List<SignedTransaction> transactions);

    /**
     * Submit transaction resp data.
     *
     * @param transaction the transaction
     * @return the resp data
     */
    RespData<List<TransactionVO>> submitTransaction(SignedTransaction transaction);

    /**
     * submit transactions to master node
     *
     * @param transactions the transactions
     * @return resp data
     */
    RespData<List<TransactionVO>> submitToMaster(List<SignedTransaction> transactions);

    /**
     * List block headers list.
     *
     * @param startHeight the start height
     * @param size        the size
     * @return the list
     */
    List<BlockHeader> listBlockHeaders(long startHeight, int size);

    /**
     * List blocks list.
     *
     * @param startHeight the start height
     * @param size        the size
     * @return the list
     */
    List<Block> listBlocks(long startHeight, int size);

    /**
     * query block
     *
     * @param req the req
     * @return page vo
     */
    @Deprecated
    PageVO<BlockVO> queryBlocks(QueryBlockVO req);

    /**
     * query transaction
     *
     * @param req the req
     * @return page vo
     */
    @Deprecated
    PageVO<CoreTransactionVO> queryTransactions(QueryTransactionVO req);

    /**
     * query utxo by transaction id
     *
     * @param txId the tx id
     * @return list
     */
    List<UTXOVO> queryUTXOByTxId(String txId);

    /**
     * check whether the identity is existed
     *
     * @param identity the identity
     * @return boolean
     */
    boolean isExistedIdentity(String identity);

    /**
     * check currency
     *
     * @param currency the currency
     * @return boolean
     */
    boolean isExistedCurrency(String currency);

    /**
     * query contract address by currency
     *
     * @param currency the currency
     * @return string
     */
    String queryContractAddressByCurrency(String currency);

    /**
     * check whether the contract address is existed
     *
     * @param address the address
     * @return boolean
     */
    boolean isExistedContractAddress(String address);

    /**
     * query System Property by key
     *
     * @param key the key
     * @return system property vo
     */
    SystemPropertyVO querySystemPropertyByKey(String key);

    /**
     * query UTXO list
     *
     * @param inputList the input list
     * @return list
     */
    List<UTXO> queryUTXOList(List<TxIn> inputList);

    /**
     * get utxo action type
     *
     * @param name the name
     * @return utxo action type
     */
    UTXOActionTypeEnum getUTXOActionType(String name);

    /**
     * query header by height
     *
     * @param blockHeight the block height
     * @return block header
     */
    BlockHeader getBlockHeader(Long blockHeight);

    /**
     * query max
     *
     * @return max block header
     */
    BlockHeader getMaxBlockHeader();

    /**
     * query max height for block
     *
     * @return max block height
     */
    Long getMaxBlockHeight();

    /**
     * query block by condition and page
     *
     * @param req the req
     * @return list
     */
    List<BlockVO> queryBlocksByPage(QueryBlockVO req);

    /**
     * query transaction by condition and page
     *
     * @param req the req
     * @return list
     */
    List<CoreTransactionVO> queryTxsByPage(QueryTransactionVO req);

    /**
     * query block info by height
     *
     * @param height the height
     * @return block vo
     */
    BlockVO queryBlockByHeight(Long height);

    /**
     * query tx info by tx_id
     *
     * @param txId the tx id
     * @return core transaction vo
     */
    CoreTransactionVO queryTxById(String txId);

    /**
     * query by ids
     *
     * @param txIds the tx ids
     * @return list
     */
    List<CoreTransactionVO> queryTxByIds(List<String> txIds);

    /**
     * get policy name by id
     *
     * @param policyId the policy id
     * @return policy name by id
     */
    String getPolicyNameById(String policyId);
}
