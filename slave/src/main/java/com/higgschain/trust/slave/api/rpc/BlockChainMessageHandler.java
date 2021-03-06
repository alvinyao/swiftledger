package com.higgschain.trust.slave.api.rpc;

import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.slave.api.BlockChainService;
import com.higgschain.trust.slave.api.rpc.request.BlockRequest;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Block chain message handler.
 *
 * @author duhongming
 * @date 2018 /9/18
 */
@Component
@Slf4j
public class BlockChainMessageHandler implements InitializingBean {

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    private NetworkManage networkManage;

    /**
     * get the block headers
     *
     * @param request the request
     * @return block headers
     */
    public List<BlockHeader> getBlockHeaders(BlockRequest request) {
        return blockChainService.listBlockHeaders(request.getStartHeight(), request.getSize());
    }

    /**
     * get the blocks
     *
     * @param request the request
     * @return blocks
     */
    public List<Block> getBlocks(BlockRequest request) {
        return blockChainService.listBlocks(request.getStartHeight(), request.getSize());
    }

    @Override
    public void afterPropertiesSet() {
        networkManage.registerHandler("block/header/get", this::getBlockHeaders);
        networkManage.registerHandler("block/get", this::getBlocks);
    }
}
