package com.higgschain.trust.slave.core.repository;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.common.utils.BeanConvertor;
import com.higgschain.trust.common.utils.Profiler;
import com.higgschain.trust.slave.api.vo.BlockVO;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.repository.config.SystemPropertyRepository;
import com.higgschain.trust.slave.dao.mysql.block.BlockDao;
import com.higgschain.trust.slave.dao.po.block.BlockPO;
import com.higgschain.trust.slave.dao.rocks.block.BlockRocksDao;
import com.higgschain.trust.slave.model.bo.*;
import com.higgschain.trust.slave.model.bo.config.SystemProperty;
import com.higgschain.trust.slave.model.convert.BlockConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * The type Block repository.
 *
 * @author tangfashuang
 * @desc block repository
 * @date 2018 /04/10 16:26
 */
@Repository @Slf4j public class BlockRepository {
    /**
     * The Block dao.
     */
    @Autowired
    BlockDao blockDao;
    /**
     * The Block rocks dao.
     */
    @Autowired
    BlockRocksDao blockRocksDao;
    /**
     * The Transaction repository.
     */
    @Autowired TransactionRepository transactionRepository;
    /**
     * The System property repository.
     */
    @Autowired
    SystemPropertyRepository systemPropertyRepository;
    /**
     * The Init config.
     */
    @Autowired
    InitConfig initConfig;

    /**
     * get max height of block
     *
     * @return max height
     */
    public Long getMaxHeight() {
        if (initConfig.isUseMySQL()) {
            return blockDao.getMaxHeight();
        } else {
            SystemProperty bo = systemPropertyRepository.queryByKey(Constant.MAX_BLOCK_HEIGHT);
            return (bo != null && !StringUtils.isEmpty(bo.getValue())) ? Long.parseLong(bo.getValue()) : null;
        }
    }

    /**
     * get max height of block
     *
     * @param size the size
     * @return limit height
     */
    public List<Long> getLimitHeight(int size) {
        if (initConfig.isUseMySQL()) {
            return blockDao.getLimitHeight(size);
        } else {
            Long maxBlockHeight = getMaxHeight();
            if (null == maxBlockHeight) {
                return null;
            }

            List<String> blockHeights = new ArrayList<>();
            while (size-- > 0 && maxBlockHeight > 0) {
                blockHeights.add(String.valueOf(maxBlockHeight--));
            }
            return blockRocksDao.getLimitHeight(blockHeights);
        }
    }

    /**
     * get block info by block height
     *
     * @param height the height
     * @return block
     */
    public Block getBlock(Long height) {
        BlockPO blockPO;
        if (initConfig.isUseMySQL()) {
            blockPO = blockDao.queryByHeight(height);
        } else {
            blockPO = blockRocksDao.get(String.valueOf(height));
        }
        return convertPOToBO(blockPO);
    }

    private Block convertPOToBO(BlockPO blockPO) {
        if (null == blockPO) {
            return null;
        }
        Block block = new Block();
        BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHeight(blockPO.getHeight());
        blockHeader.setBlockHash(blockPO.getBlockHash());
        blockHeader.setBlockTime(blockPO.getBlockTime() != null ? blockPO.getBlockTime().getTime() : null);
        blockHeader.setPreviousHash(blockPO.getPreviousHash());
        blockHeader.setVersion(blockPO.getVersion());
        blockHeader.setTotalTxNum(blockPO.getTotalTxNum());
        StateRootHash rootHash = new StateRootHash();
        rootHash.setRsRootHash(blockPO.getRsRootHash());
        rootHash.setTxRootHash(blockPO.getTxRootHash());
        rootHash.setTxReceiptRootHash(blockPO.getTxReceiptRootHash());
        rootHash.setPolicyRootHash(blockPO.getPolicyRootHash());
        rootHash.setContractRootHash(blockPO.getContractRootHash());
        rootHash.setAccountRootHash(blockPO.getAccountRootHash());
        rootHash.setCaRootHash(blockPO.getCaRootHash());
        rootHash.setStateRoot(blockPO.getStateRootHash());
        blockHeader.setStateRootHash(rootHash);
        block.setBlockHeader(blockHeader);
        block.setSignedTxList(blockPO.getSignedTxs());
        if (blockPO.getHeight() == 1) {
            block.setGenesis(true);
        }

        //txs
        if (initConfig.isUseMySQL()) {
            block.setSignedTxList(transactionRepository.queryTransactions(blockPO.getHeight()));
        }
        return block;
    }

    /**
     * list the blocks
     *
     * @param startHeight start height
     * @param size        size
     * @return list
     */
    public List<Block> listBlocks(long startHeight, int size) {
        List<BlockPO> blockPOs;
        if (initConfig.isUseMySQL()) {
            blockPOs = blockDao.queryBlocks(startHeight, size);

        } else {
            blockPOs = blockRocksDao.queryBlocks(startHeight, size);
        }

        if (CollectionUtils.isEmpty(blockPOs)) {
            return Collections.emptyList();
        }

        List<Block> blocks = new ArrayList<>();
        for (BlockPO po : blockPOs) {
            blocks.add(convertPOToBO(po));
        }
        return blocks;
    }

    /**
     * list the block headers
     *
     * @param startHeight start height
     * @param size        size
     * @return list
     */
    public List<BlockHeader> listBlockHeaders(long startHeight, int size) {
        List<BlockPO> blockPOs;
        if (initConfig.isUseMySQL()) {
            blockPOs = blockDao.queryBlocks(startHeight, size);
        } else {
            blockPOs = blockRocksDao.queryBlocks(startHeight, size);
        }

        if (CollectionUtils.isEmpty(blockPOs)) {
            return Collections.emptyList();
        }
        List<BlockHeader> headers = new ArrayList<>();
        blockPOs.forEach(blockPO -> {
            BlockHeader blockHeader = new BlockHeader();
            blockHeader.setHeight(blockPO.getHeight());
            blockHeader.setBlockHash(blockPO.getBlockHash());
            blockHeader.setBlockTime(blockPO.getBlockTime() != null ? blockPO.getBlockTime().getTime() : null);
            blockHeader.setPreviousHash(blockPO.getPreviousHash());
            blockHeader.setVersion(blockPO.getVersion());
            blockHeader.setTotalTxNum(blockPO.getTotalTxNum());
            StateRootHash rootHash = new StateRootHash();
            rootHash.setRsRootHash(blockPO.getRsRootHash());
            rootHash.setTxRootHash(blockPO.getTxRootHash());
            rootHash.setTxReceiptRootHash(blockPO.getTxReceiptRootHash());
            rootHash.setPolicyRootHash(blockPO.getPolicyRootHash());
            rootHash.setContractRootHash(blockPO.getContractRootHash());
            rootHash.setAccountRootHash(blockPO.getAccountRootHash());
            rootHash.setCaRootHash(blockPO.getCaRootHash());
            rootHash.setStateRoot(blockPO.getStateRootHash());
            blockHeader.setStateRootHash(rootHash);
            headers.add(blockHeader);
        });

        return headers;
    }

    /**
     * get block header data from db
     *
     * @param height the height
     * @return block header
     */
    public BlockHeader getBlockHeader(Long height) {
        BlockPO blockPO;
        if (initConfig.isUseMySQL()) {
            blockPO = blockDao.queryByHeight(height);
        } else {
            blockPO = blockRocksDao.get(String.valueOf(height));
        }

        return blockPO == null ? null : BlockConvert.convertBlockPOToBlockHeader(blockPO);
    }

    /**
     * save to db
     *
     * @param block        the block
     * @param txReceiptMap the tx receipt map
     */
    public void saveBlock(Block block, Map<String, TransactionReceipt> txReceiptMap) {
        if (log.isDebugEnabled()) {
            log.debug("[BlockRepository.saveBlock] is start");
        }
        BlockHeader blockHeader = block.getBlockHeader();
        //block height
        Long blockHeight = blockHeader.getHeight();
        BlockPO blockPO = new BlockPO();
        blockPO.setHeight(blockHeight);
        blockPO.setPreviousHash(blockHeader.getPreviousHash());
        blockPO.setVersion(blockHeader.getVersion());
        Date blockTime = new Date(blockHeader.getBlockTime());
        blockPO.setBlockTime(blockTime);
        blockPO.setBlockHash(blockHeader.getBlockHash());
        StateRootHash rootHash = blockHeader.getStateRootHash();
        blockPO.setTxRootHash(rootHash.getTxRootHash());
        blockPO.setTxReceiptRootHash(rootHash.getTxReceiptRootHash());
        blockPO.setAccountRootHash(rootHash.getAccountRootHash());
        blockPO.setContractRootHash(rootHash.getContractRootHash());
        blockPO.setPolicyRootHash(rootHash.getPolicyRootHash());
        blockPO.setRsRootHash(rootHash.getRsRootHash());
        blockPO.setCaRootHash(rootHash.getCaRootHash());
        blockPO.setStateRootHash(rootHash.getStateRoot());
        List<SignedTransaction> txs = block.getSignedTxList();

        //add transaction number to block table
        int txNum = txs.size();
        blockPO.setTxNum(txNum);
        //set total transaction num
        Long totalTxNum = blockHeader.getTotalTxNum();
        if (totalTxNum == null) {
            totalTxNum = 0L;
        }
        //total=lastNum + currentNum
        blockPO.setTotalTxNum(totalTxNum + txNum);
        //total block size use txs.length,unit:KB
        Profiler.enter("calculate block size");
        String blockData = JSON.toJSONString(txs);
        BigDecimal size = new BigDecimal(blockData.length());
        size = size.divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_DOWN);
        blockPO.setTotalBlockSize(size);
        Profiler.release();
        //save block
        if (initConfig.isUseMySQL()) {
            try {
                blockDao.add(blockPO);
            } catch (DuplicateKeyException e) {
                log.error("[saveBlock] is idempotent blockHeight:{}", blockHeight);
                throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT);
            }
        } else {
            Profiler.enter("save max block height");
            systemPropertyRepository
                .saveWithTransaction(Constant.MAX_BLOCK_HEIGHT, String.valueOf(blockHeight), "max block height");
            Profiler.release();

            blockPO.setSignedTxs(txs);
            Profiler.enter("save block");
            blockRocksDao.save(blockPO);
            Profiler.release();
        }

        Profiler.enter("batch insert transaction");
        //save transactions
        transactionRepository.batchSaveTransaction(blockHeight, blockTime, txs, txReceiptMap);
        Profiler.release();

        if (log.isDebugEnabled()) {
            log.debug("[BlockRepository.saveBlock] is end");
        }
    }

    /**
     * query by condition、page
     *
     * @param height    the height
     * @param blockHash the block hash
     * @param pageNum   the page num
     * @param pageSize  the page size
     * @return list
     */
    public List<BlockVO> queryBlocksWithCondition(Long height, String blockHash, Integer pageNum, Integer pageSize) {
        if (null != blockHash) {
            blockHash = blockHash.trim();
        }
        List<BlockPO> list = blockDao.queryBlocksWithCondition(height, blockHash, (pageNum - 1) * pageSize, pageSize);
        return BeanConvertor.convertList(list, BlockVO.class);
    }

    /**
     * Count blocks with condition long.
     *
     * @param height    the height
     * @param blockHash the block hash
     * @return the long
     */
    @Deprecated public long countBlocksWithCondition(Long height, String blockHash) {
        return blockDao.countBlockWithCondition(height, blockHash);
    }

    /**
     * query block by height
     *
     * @param height the height
     * @return block vo
     */
    public BlockVO queryBlockByHeight(Long height) {
        BlockPO blockPO = blockDao.queryByHeight(height);
        if (blockPO == null) {
            return null;
        }
        return BeanConvertor.convertBean(blockPO, BlockVO.class);
    }
}
