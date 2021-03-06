package com.higgschain.trust.management.failover.service;

import com.higgschain.trust.common.dao.RocksUtils;
import com.higgschain.trust.common.enums.MonitorTargetEnum;
import com.higgschain.trust.common.utils.MonitorLogUtils;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.StateChangeListener;
import com.higgschain.trust.consensus.config.listener.StateListener;
import com.higgschain.trust.management.exception.FailoverExecption;
import com.higgschain.trust.management.exception.ManagementError;
import com.higgschain.trust.management.failover.config.FailoverProperties;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.core.service.action.GeniusBlockService;
import com.higgschain.trust.slave.core.service.block.BlockService;
import com.higgschain.trust.slave.core.service.consensus.log.PackageListener;
import com.higgschain.trust.slave.core.service.pack.PackageProcess;
import com.higgschain.trust.slave.core.service.pack.PackageService;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.enums.biz.PackageStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.rocksdb.Transaction;
import org.rocksdb.WriteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * The type Sync service.
 */
@StateListener @Order(2) @Service @Slf4j public class SyncService implements PackageListener {

    @Autowired private FailoverProperties properties;
    @Autowired private BlockRepository blockRepository;
    @Autowired private BlockService blockService;
    @Autowired private BlockSyncService blockSyncService;
    @Autowired private PackageService packageService;
    @Autowired private NodeState nodeState;
    @Autowired private TransactionTemplate txNested;
    @Autowired private GeniusBlockService geniusBlockService;
    @Autowired private InitConfig initConfig;
    @Autowired private PackageProcess packageProcess;
    private Long receivedFistHeight = null;
    private Long currentPackageHeight = null;

    /**
     * Async auto sync.
     */
    public void asyncAutoSync() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                autoSync();
            } catch (Throwable e) {
                log.error("auto sync block failed!", e);
            }
        });
    }

    /**
     * 自动同步区块
     */
    @StateChangeListener(NodeStateEnum.AutoSync) public void autoSync() {
        if (!nodeState.isState(NodeStateEnum.AutoSync, NodeStateEnum.ArtificialSync, NodeStateEnum.Standby)) {
            return;
        }
        log.info("auto sync starting ...");
        try {
            Long currentHeight = blockRepository.getMaxHeight();
            Long clusterHeight = blockSyncService.getSafeHeight(properties.getTryTimes());
            if (clusterHeight == null) {
                throw new SlaveException(SlaveErrorEnum.SLAVE_CONSENSUS_GET_RESULT_FAILED);
            }
            Long failoverHeight = clusterHeight;
            receivedFistHeight = null;
            while (currentHeight.compareTo(failoverHeight) < 0) {
                sync(currentHeight + 1, properties.getHeaderStep());
                currentHeight = blockRepository.getMaxHeight();
                if (receivedFistHeight != null) {
                    failoverHeight = receivedFistHeight - 1;
                }
            }
        } catch (Throwable e) {
            MonitorLogUtils.logIntMonitorInfo(MonitorTargetEnum.SYNC_BLOCKS_FAILED, 1);
            throw new FailoverExecption(ManagementError.MANAGEMENT_STARTUP_AUTO_SYNC_FAILED, e);
        }
    }

    /**
     * get the cluster height
     */
    private Long getClusterHeight() {
        Long clusterHeight;
        int tryTimes = 0;
        do {
            clusterHeight = blockSyncService.getClusterHeight(3);
            if (clusterHeight != null) {
                break;
            }
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                log.warn("self check error.", e);
            }
        } while (++tryTimes < properties.getTryTimes());
        return clusterHeight;
    }

    /**
     * 同步指定数量的区块
     *
     * @param startHeight 开始高度
     * @param size        同步数量
     */
    public synchronized void sync(long startHeight, int size) {
        if (!nodeState.isState(NodeStateEnum.AutoSync, NodeStateEnum.ArtificialSync, NodeStateEnum.Standby)) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_STATE_NOT_ALLOWED);
        }
        log.info("starting to sync the block, start height:{}, size:{}", startHeight, size);
        Assert.isTrue(size > 0, "the size of sync block must > 0");
        Long currentHeight = blockRepository.getMaxHeight();
        log.info("local current block height:{}", currentHeight);
        if (currentHeight != startHeight - 1) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_START_HEIGHT_ERROR);
        }
        int tryTimes = 0;
        List<BlockHeader> headers = null;
        Boolean headerValidated = false;
        BlockHeader currentHeader = blockRepository.getBlockHeader(currentHeight);
        //批量拉取header并验证
        do {
            try {
                headers = blockSyncService.getHeaders(startHeight, size);
            } catch (Exception e) {
                log.warn("get the headers error", e);
            }
            if (headers == null || headers.isEmpty()) {
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("get the block headers from other node:{}", ToStringBuilder.reflectionToString(headers));
            }
            headerValidated = blockSyncService.validating(currentHeader.getBlockHash(), headers);
            if (log.isDebugEnabled()) {
                log.debug("the block headers local valid result:{}", headerValidated);
            }
            if (!headerValidated) {
                continue;
            }
            headerValidated = blockSyncService.bftValidating(headers.get(headers.size() - 1));
            if (headerValidated == null || !headerValidated) {
                continue;
            }
        } while ((headerValidated == null || !headerValidated) && ++tryTimes < properties.getTryTimes());
        if (headerValidated == null || !headerValidated) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_GET_VALIDATING_HEADERS_FAILED);
        }
        int headerSize = headers.size();
        int startIndex = 0;
        long blockStartHeight = headers.get(startIndex).getHeight();
        long blockEndHeight = headers.get(headers.size() - 1).getHeight();
        int blockSize = properties.getBlockStep();
        BlockHeader preHeader = currentHeader;

        do {
            if (blockStartHeight + properties.getBlockStep() > blockEndHeight) {
                blockSize = new Long(blockEndHeight - blockStartHeight + 1).intValue();
            }
            if (blockSize <= 0) {
                break;
            }
            List<Block> blocks = getAndValidatingBlock(preHeader, blockStartHeight, blockSize);
            blockSize = blocks.size();
            Block lastBlock = blocks.get(blockSize - 1);
            //验证最后块的header是否与header列表中的一致
            boolean blocksValidated =
                blockService.compareBlockHeader(lastBlock.getBlockHeader(), headers.get(startIndex + blockSize - 1));
            if (!blocksValidated) {
                log.error("validating the last block of blocks failed");
                throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_SYNC_BLOCK_VALIDATING_FAILED);
            }
            blocks.forEach(block -> syncBlock(block));
            startIndex = startIndex + blockSize;
            blockStartHeight = blockStartHeight + blockSize;
            preHeader = headers.get(startIndex - 1);
        } while (startIndex < headerSize - 1);
    }

    /**
     * 从指定节点同步指定数量的区块
     *
     * @param startHeight  开始高度
     * @param size         同步数量
     * @param fromNodeName the from node name
     */
    public synchronized void sync(long startHeight, int size, String fromNodeName) {
        if (!nodeState.isState(NodeStateEnum.ArtificialSync)) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_STATE_NOT_ALLOWED);
        }
        log.info("starting to sync the block from node {}, start height:{}, size:{}", fromNodeName, startHeight, size);
        Assert.isTrue(size > 0, "the size of sync block must > 0");
        Long currentHeight = blockRepository.getMaxHeight();
        log.info("local current block height:{}", currentHeight);
        if (currentHeight != startHeight - 1) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_START_HEIGHT_ERROR);
        }
        int tryTimes = 0;
        List<BlockHeader> headers = null;
        Boolean headerValidated = false;
        BlockHeader currentHeader = blockRepository.getBlockHeader(currentHeight);
        //批量拉取header并验证
        do {
            headers = blockSyncService.getHeadersFromNode(startHeight, size, fromNodeName);
            if (headers == null || headers.isEmpty()) {
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("get the block headers from other node:{}", ToStringBuilder.reflectionToString(headers));
            }
            headerValidated = blockSyncService.validating(currentHeader.getBlockHash(), headers);
            if (log.isDebugEnabled()) {
                log.debug("the block headers local valid result:{}", headerValidated);
            }
            if (!headerValidated) {
                continue;
            }
        } while (!headerValidated && ++tryTimes < properties.getTryTimes());
        if (!headerValidated) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_GET_VALIDATING_HEADERS_FAILED);
        }
        int headerSize = headers.size();
        int startIndex = 0;
        long blockStartHeight = headers.get(startIndex).getHeight();
        long blockEndHeight = headers.get(headers.size() - 1).getHeight();
        int blockSize = properties.getBlockStep();
        BlockHeader preHeader = currentHeader;

        do {
            if (blockStartHeight + properties.getBlockStep() > blockEndHeight) {
                blockSize = new Long(blockEndHeight - blockStartHeight + 1).intValue();
            }
            List<Block> blocks = getAndValidatingBlock(preHeader, blockStartHeight, blockSize, fromNodeName);
            blockSize = blocks.size();
            Block lastBlock = blocks.get(blockSize - 1);
            //验证最后块的header是否与header列表中的一致
            boolean blocksValidated =
                blockService.compareBlockHeader(lastBlock.getBlockHeader(), headers.get(startIndex + blockSize - 1));
            if (!blocksValidated) {
                log.error("validating the last block of blocks failed");
                throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_SYNC_BLOCK_VALIDATING_FAILED);
            }
            blocks.forEach(block -> syncBlock(block));
            startIndex = startIndex + blockSize;
            blockStartHeight = blockStartHeight + blockSize;
            preHeader = headers.get(startIndex - 1);
        } while (startIndex < headerSize - 1);
    }

    /**
     * Sync genesis.
     */
    public void syncGenesis() {
        List<Block> blocks = getAndValidatingBlock(null, 1, 1);
        syncGenesis(blocks.get(0));
    }

    /**
     * Sync genesis.
     *
     * @param fromNode the from node
     */
    public void syncGenesis(String fromNode) {
        List<Block> blocks = getAndValidatingBlock(null, 1, 1, fromNode);
        syncGenesis(blocks.get(0));
    }

    private void syncGenesis(Block block) {
        int tryTimes = 0;
        Boolean headerValidated = false;
        do {
            headerValidated = blockSyncService.bftValidating(block.getBlockHeader());
            if (headerValidated == null || !headerValidated) {
                continue;
            }
        } while ((headerValidated == null || !headerValidated) && ++tryTimes < properties.getTryTimes());
        if (headerValidated == null || !headerValidated) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_GET_VALIDATING_HEADERS_FAILED);
        }
        geniusBlockService.generateGeniusBlock(block);
    }

    /**
     * receive package height
     */
    @Override public void received(Package pack) {
        Long height = pack.getHeight();
        if (receivedFistHeight == null) {
            receivedFistHeight = height;
            currentPackageHeight = height;
            return;
        }
        if (height == currentPackageHeight + 1) {
            currentPackageHeight = height;
        } else if (height > currentPackageHeight + 1) {
            log.warn("received discontinuous height, current height:{}, package height:{}", currentPackageHeight,
                height);
            currentPackageHeight = height;
            receivedFistHeight = currentPackageHeight;
        }
    }

    /**
     * get the blocks and validate it
     *
     * @param preHeader
     * @param startHeight
     * @param size
     * @return
     */
    private List<Block> getAndValidatingBlock(BlockHeader preHeader, long startHeight, int size) {
        int tryTimes = 0;
        List<Block> blocks = null;
        boolean blockValidated = false;
        do {
            try {
                blocks = blockSyncService.getBlocks(startHeight, size);
            } catch (Exception e) {
                log.warn("get the blocks error", e);
            }
            if (blocks == null || blocks.isEmpty()) {
                continue;
            }
            if (preHeader == null) {
                blockValidated = blockSyncService.validatingBlocks("IS_NULL", blocks);
            } else {
                blockValidated = blockSyncService.validatingBlocks(preHeader.getBlockHash(), blocks);
            }
            if (!blockValidated) {
                continue;
            }
        } while (!blockValidated && ++tryTimes < properties.getTryTimes());
        if (!blockValidated) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_GET_VALIDATING_BLOCKS_FAILED);
        }
        return blocks;
    }

    /**
     * get the blocks and validate it
     *
     * @param preHeader
     * @param startHeight
     * @param size
     * @return
     */
    private List<Block> getAndValidatingBlock(BlockHeader preHeader, long startHeight, int size, String fromNode) {
        int tryTimes = 0;
        List<Block> blocks = null;
        boolean blockValidated = false;
        do {
            blocks = blockSyncService.getBlocksFromNode(startHeight, size, fromNode);
            if (blocks == null || blocks.isEmpty()) {
                continue;
            }
            if (preHeader == null) {
                blockValidated = blockSyncService.validatingBlocks("IS_NULL", blocks);
            } else {
                blockValidated = blockSyncService.validatingBlocks(preHeader.getBlockHash(), blocks);
            }
            if (!blockValidated) {
                continue;
            }
        } while (!blockValidated && ++tryTimes < properties.getTryTimes());
        if (!blockValidated) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_GET_VALIDATING_BLOCKS_FAILED);
        }
        return blocks;
    }

    /**
     * 同步单一block
     *
     * @param block 区块
     * @return 同步结果
     */
    private void syncBlock(Block block) {
        BlockHeader blockHeader = block.getBlockHeader();
        log.info("Sync block:{}", blockHeader.getHeight());
        Package pack = new Package();
        pack.setPackageTime(blockHeader.getBlockTime());
        pack.setHeight(blockHeader.getHeight());
        pack.setStatus(PackageStatusEnum.FAILOVER);
        pack.setSignedTxList(block.getSignedTxList());
        PackContext packContext = packageService.createPackContext(pack);
        if (initConfig.isUseMySQL()) {
            txNested.execute(new TransactionCallbackWithoutResult() {
                @Override protected void doInTransactionWithoutResult(TransactionStatus status) {
                    packageService.process(packContext, true, true);
                }
            });
        } else {
            try {
                Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
                ThreadLocalUtils.putRocksTx(tx);
                packageService.process(packContext, true, true);
                RocksUtils.txCommit(tx);
            } finally {
                ThreadLocalUtils.clearRocksTx();
            }
        }
        //update block height in memory
        packageProcess.updateProcessedHeight(blockHeader.getHeight());

        boolean persistValid =
            blockService.compareBlockHeader(packContext.getCurrentBlock().getBlockHeader(), block.getBlockHeader());
        if (!persistValid) {
            throw new FailoverExecption(ManagementError.MANAGEMENT_FAILOVER_SYNC_BLOCK_PERSIST_RESULT_INVALID);
        }
    }
}
