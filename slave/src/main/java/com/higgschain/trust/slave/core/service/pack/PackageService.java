package com.higgschain.trust.slave.core.service.pack;

import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;
import com.higgschain.trust.slave.model.bo.context.PackContext;

import java.util.List;

/**
 * The interface Package service.
 *
 * @Description:package services include build, validate, persist
 * @author: pengdi
 */
public interface PackageService {
    /**
     * create new package from pending transactions
     *
     * @param signedTransactions   the signed transactions
     * @param currentPackageHeight the current package height
     * @return package
     */
    Package create(List<SignedTransaction> signedTransactions, Long currentPackageHeight);

    /**
     * receive new package from somewhere, almost from consensus
     *
     * @param pack the pack
     */
    void receive(Package pack);

    /**
     * create pack context for main process
     *
     * @param pack the pack
     * @return pack context
     */
    PackContext createPackContext(Package pack);

    /**
     * execute package persisting
     *
     * @param packContext the pack context
     * @param isFailover  the is failover
     * @param isBatchSync the is batch sync
     */
    void process(PackContext packContext,boolean isFailover, boolean isBatchSync);

    /**
     * remove the package if it is done
     *
     * @param pack the pack
     */
    void remove(Package pack);

    /**
     * submit package to consensus
     *
     * @param packs the packs
     */
    void submitConsensus(PackageCommand packs);

    /**
     * persisted
     *
     * @param header    the header
     * @param isCompare the is compare
     */
    void persisted(BlockHeader header,boolean isCompare);
}
