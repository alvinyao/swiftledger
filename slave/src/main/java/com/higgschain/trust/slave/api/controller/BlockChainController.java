/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.api.controller;

import com.higgschain.trust.slave.api.BlockChainService;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Block chain controller.
 *
 * @author suimi
 * @date 2018 /4/24
 */
@RestController @Slf4j @RequestMapping("/block") public class BlockChainController {

    @Autowired private BlockChainService blockChainService;

    /**
     * get the block headers
     *
     * @param startHeight the start height
     * @param size        the size
     * @return block headers
     */
    @RequestMapping(value = "/header/get", method = RequestMethod.GET) List<BlockHeader> getBlockHeaders(
        @RequestParam(value = "startHeight") long startHeight, @RequestParam(value = "size") int size) {
        return blockChainService.listBlockHeaders(startHeight, size);
    }

    /**
     * get the blocks
     *
     * @param startHeight the start height
     * @param size        the size
     * @return blocks
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET) List<Block> getBlocks(
        @RequestParam(value = "startHeight") long startHeight, @RequestParam(value = "size") int size) {
        return blockChainService.listBlocks(startHeight, size);
    }
}
