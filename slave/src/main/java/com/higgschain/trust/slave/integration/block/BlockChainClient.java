/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.integration.block;

import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.api.vo.TransactionVO;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The interface Block chain client.
 */
//@FeignClient("${higgs.trust.prefix}")
public interface BlockChainClient {
    /**
     * get the block headers
     *
     * @param nodeNameReg node name regex
     * @param startHeight the start height
     * @param size        the size
     * @return block headers
     */
    @RequestMapping(value = "/block/header/get", method = RequestMethod.GET)
    List<BlockHeader> getBlockHeaders(String nodeNameReg, @RequestParam(value = "startHeight") long startHeight, @RequestParam(value = "size") int size);

    /**
     * get the block headers
     *
     * @param nodeName    node name
     * @param startHeight the start height
     * @param size        the size
     * @return block headers from node
     */
    @RequestMapping(value = "/block/header/get", method = RequestMethod.GET)
    List<BlockHeader> getBlockHeadersFromNode(String nodeName,
        @RequestParam(value = "startHeight") long startHeight, @RequestParam(value = "size") int size);

    /**
     * get the blocks
     *
     * @param nodeNameReg node name regex
     * @param startHeight the start height
     * @param size        the size
     * @return blocks
     */
    @RequestMapping(value = "/block/get", method = RequestMethod.GET)
    List<Block> getBlocks(String nodeNameReg,
                          @RequestParam(value = "startHeight") long startHeight, @RequestParam(value = "size") int size);

    /**
     * get the blocks
     *
     * @param nodeName    node name
     * @param startHeight the start height
     * @param size        the size
     * @return blocks from node
     */
    @RequestMapping(value = "/block/get", method = RequestMethod.GET)
    List<Block> getBlocksFromNode(String nodeName,
        @RequestParam(value = "startHeight") long startHeight, @RequestParam(value = "size") int size);

    /**
     * submit transaction
     *
     * @param nodeName     node name
     * @param transactions the transactions
     * @return submit failed transaction list
     */
    @RequestMapping(value = "/transaction/master/submit", method = RequestMethod.POST)
    RespData<List<TransactionVO>> submitToMaster(String nodeName,
                                                 @RequestBody List<SignedTransaction> transactions);

}
