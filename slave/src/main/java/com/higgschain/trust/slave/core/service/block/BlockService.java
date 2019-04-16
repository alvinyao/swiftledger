package com.higgschain.trust.slave.core.service.block;

import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.TransactionReceipt;
import com.higgschain.trust.slave.model.bo.context.PackageData;

import java.util.Map;

/**
 * The interface Block service.
 *
 * @Description:
 * @author: pengdi
 */
public interface BlockService {
    /**
     * get the height from most recently persisted block in the final block chain
     *
     * @return max height
     */
    Long getMaxHeight();

    /**
     * build block p2p
     *
     * @param packageData  the package data
     * @param txReceiptMap the tx receipt map
     * @return block header
     */
    BlockHeader buildHeader(PackageData packageData,
        Map<String, TransactionReceipt> txReceiptMap);

    /**
     * get final persisted block header
     *
     * @param height the height
     * @return header
     */
    BlockHeader getHeader(Long height);

    /**
     * build block
     *
     * @param packageData the package data
     * @param blockHeader the block header
     * @return block
     */
    Block buildBlock(PackageData packageData, BlockHeader blockHeader);

    /**
     * build dummy block
     *
     * @param height    the height
     * @param blockTime the block time
     * @return block
     */
    Block buildDummyBlock(Long height, Long blockTime);

    /**
     * persist block for final result
     *
     * @param block      the block
     * @param txReceipts the tx receipts
     */
    void persistBlock(Block block, Map<String, TransactionReceipt> txReceipts);

    /**
     * compare the two header datas
     *
     * @param header1 the header 1
     * @param header2 the header 2
     * @return boolean
     */
    boolean compareBlockHeader(BlockHeader header1, BlockHeader header2);

    /**
     * build hash for block header
     *
     * @param blockHeader the block header
     * @return string
     */
    String buildBlockHash(BlockHeader blockHeader);

    /**
     * query block from db
     *
     * @param blockHeight the block height
     * @return block
     */
    Block queryBlock(Long blockHeight);
}
