package com.higgschain.trust.slave.core;

import com.higgschain.trust.evmcontract.core.Bloom;
import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.core.TransactionResultInfo;
import com.higgschain.trust.evmcontract.datasource.DbSource;
import com.higgschain.trust.evmcontract.db.BlockStore;
import com.higgschain.trust.evmcontract.db.TransactionStore;
import com.higgschain.trust.evmcontract.facade.BlockStoreAdapter;
import com.higgschain.trust.evmcontract.trie.TrieImpl;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.util.RLP;
import com.higgschain.trust.evmcontract.util.RLPList;
import com.higgschain.trust.slave.common.listener.CompositeTrustListener;
import com.higgschain.trust.slave.common.listener.TrustListener;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Blockchain.
 *
 * @author duhongming
 * @date 2018 /11/30
 */
@Component
@Slf4j
public class Blockchain implements TrustListener, InitializingBean {

    private final BlockStore blockStore;
    private boolean initialized;
    private TransactionStore transactionStore;
    private BlockHeader lastBlockHeader;
    private Repository repositorySnapshot;
    private List<TransactionResultInfo> receipts;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private DbSource<byte[]> dbSource;
    @Autowired
    private Repository repository;

    private CompositeTrustListener listeners;

    /**
     * Instantiates a new Blockchain.
     */
    public Blockchain() {
        this.blockStore = createBlockStore();
        this.listeners = new CompositeTrustListener();
    }

    private BlockStore createBlockStore() {
        BlockStore blockStore = new BlockStoreAdapter() {
            @Override
            public byte[] getBlockHashByNumber(long blockNumber, byte[] branchBlockHash) {
                Block block = blockRepository.getBlock(blockNumber);
                if (block != null) {
                    return Hex.decode(block.getBlockHeader().getBlockHash());
                }
                return null;
            }
        };
        return blockStore;
    }

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        if (initialized) {
            return;
        }
        init0();
    }

    private synchronized void init0() {
        if (initialized) {
            return;
        }
        Long maxHeight = blockRepository.getMaxHeight();
        BlockHeader blockHeader = blockRepository.getBlockHeader(maxHeight);
        setLastBlockHeader(blockHeader);
        if (blockHeader != null) {
            initialized = true;
            this.transactionStore = new TransactionStore(dbSource);
        }
    }

    @Override
    public void afterPropertiesSet() {
        registerListener();
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(TrustListener listener) {
        if (listener instanceof Blockchain || listener instanceof CompositeTrustListener) {
            return;
        }
        listeners.addListener(listener);
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(TrustListener listener) {
        listeners.removeListener(listener);
    }

    /**
     * Start execute block.
     */
    public synchronized void startExecuteBlock() {
        String root = lastBlockHeader.getStateRootHash().getStateRoot();
        receipts = new ArrayList<>();
        if (StringUtils.isNotEmpty(root)) {
            repositorySnapshot = repository.getSnapshotTo(Hex.decode(root));
        } else {
            repositorySnapshot = repository.getSnapshotTo(null);
        }
    }

    /**
     * Finish execute block.
     *
     * @param blockHeader the block header
     */
    public synchronized void finishExecuteBlock(BlockHeader blockHeader) {
        Bloom logBloom = new Bloom();
        for (TransactionResultInfo result : receipts) {
            logBloom.or(result.getBloomFilter());
        }

        TrieImpl trie = new TrieImpl();
        for (TransactionResultInfo result : receipts) {
            transactionStore.put(result.getTxHash(), result);
            trie.put(result.getTxHash(), result.getEncoded());
        }

        long height = blockHeader.getHeight();
        MiniBlock miniBlock = new MiniBlock(height, logBloom.getData(), trie.getRootHash());
        dbSource.put(ByteUtil.longToBytes(height), miniBlock.getEncoded());
        transactionStore.flush();
        repositorySnapshot.commit();

        repository = repositorySnapshot;
        repositorySnapshot = null;
        lastBlockHeader = blockHeader;

        onBlock(blockHeader);

        for (TransactionResultInfo result : receipts) {
            onTransactionExecuted(result);
        }
        receipts = null;
    }

    /**
     * Put result info.
     *
     * @param result the result
     */
    public void putResultInfo(TransactionResultInfo result) {
        receipts.add(result);
    }

    /**
     * Gets last block header.
     *
     * @return the last block header
     */
    public BlockHeader getLastBlockHeader() {
        return lastBlockHeader;
    }

    /**
     * Sets last block header.
     *
     * @param blockHeader the block header
     */
    public synchronized void setLastBlockHeader(BlockHeader blockHeader) {
        lastBlockHeader = blockHeader;
        if (blockHeader != null && blockHeader.getStateRootHash().getStateRoot() != null) {
            repository = repository.getSnapshotTo(Hex.decode(blockHeader.getStateRootHash().getStateRoot()));
        }
    }

    /**
     * Gets repository.
     *
     * @return the repository
     */
    public Repository getRepository() {
        return repository;
    }

    /**
     * Gets repository snapshot.
     *
     * @return the repository snapshot
     */
    public Repository getRepositorySnapshot() {
        return repositorySnapshot;
    }

    /**
     * Gets repository snapshot.
     *
     * @param root the root
     * @return the repository snapshot
     */
    public Repository getRepositorySnapshot(byte[] root) {
        return repository.getSnapshotTo(root);
    }

    /**
     * Gets repository snapshot.
     *
     * @param blockHeight the block height
     * @return the repository snapshot
     */
    public Repository getRepositorySnapshot(long blockHeight) {
        if (blockHeight > lastBlockHeader.getHeight()) {
            log.warn("Target blockHeight mast less than last block height");
            throw new IllegalArgumentException("Target blockHeight mast less than last block height");
        }
        BlockHeader blockHeader = blockRepository.getBlockHeader(blockHeight);
        return repository.getSnapshotTo(Hex.decode(blockHeader.getStateRootHash().getStateRoot()));
    }

    /**
     * Gets block store.
     *
     * @return the block store
     */
    public BlockStore getBlockStore() {
        return blockStore;
    }

    /**
     * Gets transaction result info.
     *
     * @param txId the tx id
     * @return the transaction result info
     */
    public TransactionResultInfo getTransactionResultInfo(String txId) {
        if (transactionStore == null) {
            return null;
        }
        return transactionStore.get(txId.getBytes());
    }

    @Override
    public void onBlock(BlockHeader header) {
        listeners.onBlock(header);
    }

    @Override
    public void onTransactionExecuted(TransactionResultInfo resultInfo) {
        listeners.onTransactionExecuted(resultInfo);
    }

    private void registerListener() {
        Map<String, TrustListener> map = applicationContext.getBeansOfType(TrustListener.class);
        map.forEach((key,value) -> {
            addListener(value);
        });
    }

    private class MiniBlock {
        private long height;
        private byte[] logsBloom;
        private byte[] receiptsRoot;

        private byte[] rlpEncoded;

        /**
         * Instantiates a new Mini block.
         *
         * @param height       the height
         * @param logsBloom    the logs bloom
         * @param receiptsRoot the receipts root
         */
        public MiniBlock(long height, byte[] logsBloom, byte[] receiptsRoot) {
            this.height = height;
            this.logsBloom = logsBloom;
            this.receiptsRoot = receiptsRoot;
        }

        /**
         * Instantiates a new Mini block.
         *
         * @param rlp the rlp
         */
        public MiniBlock(byte[] rlp) {
            this.rlpEncoded = rlp;
            RLPList rlpList = RLP.decode2(rlp);

            BigInteger integer = RLP.decodeBigInteger(rlpList.get(0).getRLPData(), 0);
            this.height = integer.longValue();
            this.logsBloom = rlpList.get(1).getRLPData();
            this.receiptsRoot = rlpList.get(2).getRLPData();
        }

        /**
         * Get encoded byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] getEncoded() {
            if (rlpEncoded != null) {
                return rlpEncoded;
            }

            rlpEncoded = RLP.encodeList(
                    RLP.encodeElement(RLP.encodeBigInteger(BigInteger.valueOf(height))),
                    RLP.encodeElement(logsBloom),
                    RLP.encodeElement(receiptsRoot)
            );

            return rlpEncoded;
        }

        /**
         * Gets height.
         *
         * @return the height
         */
        public long getHeight() {
            return height;
        }

        /**
         * Get logs bloom byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] getLogsBloom() {
            return logsBloom;
        }

        /**
         * Get receipts root byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] getReceiptsRoot() {
            return receiptsRoot;
        }
    }
}
