package com.higgschain.trust.slave.core.managment.master;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.listener.MasterChangeListener;
import com.higgschain.trust.slave.api.enums.TxTypeEnum;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.core.repository.PackageRepository;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Master package cache.
 *
 * @author tangfashuang
 * @date 2018 /06/13 17:41
 * @desc set packHeight=null when master change
 */
@Slf4j
@Service
public class MasterPackageCache implements MasterChangeListener {

    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private NodeState nodeState;

    private AtomicLong packHeight = new AtomicLong(0);
    private AtomicLong packTime = new AtomicLong(0);
    private Deque<TermedTransaction> pendingTxQueue = new ConcurrentLinkedDeque<>();
    private ConcurrentLinkedHashMap existTxMap = new ConcurrentLinkedHashMap.Builder<String, String>().maximumWeightedCapacity(Constant.MAX_EXIST_MAP_SIZE).build();
    private BlockingQueue<PackageCommand> pendingPack = new LinkedBlockingDeque<>();

    @Override
    public void beforeChange(String masterName) {
        synchronized (this) {
            packHeight.set(0);
            packTime.set(0);
            pendingPack.clear();
            initPackHeight();
        }
    }

    @Override
    public void masterChanged(String masterName) {

    }

    /**
     * get maxBlockHeight from db, packHeight from memory.
     * if maxBlockHeight is null, log error, return null.
     * if packHeight is null, return maxBlockHeight.(if exchange master, maxPackHeight must be initialized)
     * if package is null which height = packHeight, then return null
     * else return packHeight
     */
    private void initPackHeight() {
        Long maxBlockHeight = blockRepository.getMaxHeight();
        //genius block must be exist
        if (null == maxBlockHeight) {
            log.error("please initialize genius block");
            return;
        }
        long packageHeight = maxBlockHeight;
        Long blockTime = blockRepository.getBlockHeader(maxBlockHeight).getBlockTime();
        //when exchange master, packHeight must be null
        if (null == packHeight || 0 == packHeight.get()) {
            Long maxPackHeight = packageRepository.getMaxHeight();

            if (null != maxPackHeight) {
                if(maxBlockHeight < maxPackHeight){
                    packageHeight = maxPackHeight;
                    blockTime = packageRepository.load(maxPackHeight).getPackageTime();
                }
            }
            synchronized (this) {
                log.info("set master package height:{}, last package time:{}", packageHeight, blockTime);
                packHeight.set(packageHeight);
                packTime.set(blockTime);
            }
        }
    }

    /**
     * Gets pack height.
     *
     * @return the pack height
     */
    public Long getPackHeight() {
        return packHeight.get();
    }

    /**
     * Get last pack time long.
     *
     * @return the long
     */
    public Long getLastPackTime(){
        return packTime.get();
    }

    /**
     * Get pending tx queue object [ ].
     *
     * @param count the count
     * @return the object [ ]
     */
    public Object[] getPendingTxQueue(int count) {
        if (null == pendingTxQueue.peekFirst()) {
            return null;
        }
        Object[] objs = new Object[2];
        int num = 0;
        List<SignedTransaction> list = new ArrayList<>();
        Set<String> txIdSet = new HashSet<>();
        long currentTerm = nodeState.getCurrentTerm();
        while (num++ < count) {
            TermedTransaction termedTransaction = pendingTxQueue.pollFirst();
            if (termedTransaction == null) {
                break;
            }
            if (termedTransaction.getCurrentTerm() != currentTerm) {
                existTxMap.remove(termedTransaction.getTx().getCoreTx().getTxId());
                continue;
            }
            SignedTransaction signedTx = termedTransaction.getTx();
            if (!txIdSet.contains(signedTx.getCoreTx().getTxId())) {
                list.add(signedTx);
                txIdSet.add(signedTx.getCoreTx().getTxId());
                TxTypeEnum txTypeEnum = TxTypeEnum.getBycode(signedTx.getCoreTx().getTxType());
                //for consensus
                if (txTypeEnum != null && txTypeEnum == TxTypeEnum.NODE) {
                    objs[1] = signedTx;
                    break;
                }
            }
        }
        objs[0] = list;
        return objs;
    }

    /**
     * Append deque first.
     *
     * @param signedTransaction the signed transaction
     */
    public void appendDequeFirst(SignedTransaction signedTransaction) {
        pendingTxQueue.offerFirst(new TermedTransaction(signedTransaction, nodeState.getCurrentTerm()));
    }

    /**
     * Append deque last boolean.
     *
     * @param signedTx the signed tx
     * @return if exist will return false
     */
    public boolean appendDequeLast(SignedTransaction signedTx) {
        String txId = signedTx.getCoreTx().getTxId();
        if (existTxMap.containsKey(txId)) {
            return false;
        }
        pendingTxQueue.offerLast(new TermedTransaction(signedTx, nodeState.getCurrentTerm()));
        existTxMap.put(txId, txId);
        return true;
    }

    /**
     * Gets pending tx queue size.
     *
     * @return the pending tx queue size
     */
    public int getPendingTxQueueSize() {
        return pendingTxQueue.size();
    }

    /**
     * set height for package
     *
     * @param pack the pack
     */
    public void setPackageHeight(Package pack) {
        try {
            long packageHeight = packHeight.incrementAndGet();
            pack.setHeight(packageHeight);
        } catch (Throwable e) {
            //reset packHeight
            packHeight.getAndDecrement();
            throw e;
        }
    }

    /**
     * put command to queue
     *
     * @param command the command
     * @throws InterruptedException the interrupted exception
     */
    public void putPendingPack(PackageCommand command) throws InterruptedException {
        pendingPack.offer(command, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets pending pack size.
     *
     * @return the pending pack size
     */
    public int getPendingPackSize() {
        return pendingPack.size();
    }

    /**
     * Gets package.
     *
     * @return the package
     */
    public PackageCommand getPackage() {
        return pendingPack.poll();
    }

    /**
     * The type Termed transaction.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    class TermedTransaction {
        /**
         * the transaction
         */
        SignedTransaction tx;
        /**
         * the term of cluster
         */
        long currentTerm;
    }
}
