package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.slave.api.enums.utxo.UTXOActionTypeEnum;
import com.higgschain.trust.slave.api.vo.*;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.utxo.TxIn;
import com.higgschain.trust.slave.model.bo.utxo.UTXO;

import java.util.List;

/**
 * The interface Rs block chain service.
 *
 * @author tangfashuang
 * @date 2018 /05/13 15:51
 * @desc
 */
public interface RsBlockChainService {

    /**
     * query block
     *
     * @param req the req
     * @return page vo
     */
    @Deprecated PageVO<BlockVO> queryBlock(QueryBlockVO req);

    /**
     * query transaction
     *
     * @param req the req
     * @return page vo
     */
    @Deprecated PageVO<CoreTransactionVO> queryTransaction(QueryTransactionVO req);

    /**
     * query account
     *
     * @param req the req
     * @return page vo
     */
    @Deprecated PageVO<AccountInfoVO> queryAccount(QueryAccountVO req);

    /**
     * query accounts
     *
     * @param req the req
     * @return list
     */
    List<AccountInfoVO> queryAccountsByPage(QueryAccountVO req);

    /**
     * query utxo
     *
     * @param txId the tx id
     * @return list
     */
    List<UTXOVO> queryUTXO(String txId);

    /**
     * check whether the identity is existed
     *
     * @param identity the identity
     * @return boolean
     */
    boolean isExistedIdentity(String identity);

    /**
     * check curency
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
     * query chain_owner
     *
     * @return string
     */
    String queryChainOwner();

    /**
     * query by height
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
     * process UTXO contract
     *
     * @param coreTransaction the core transaction
     * @return boolean
     */
    boolean processContract(CoreTransaction coreTransaction);

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
     * Query peers info list.
     *
     * @return list
     */
    List<NodeInfoVO> queryPeersInfo();
}
