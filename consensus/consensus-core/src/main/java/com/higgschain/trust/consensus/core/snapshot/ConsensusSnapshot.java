/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.snapshot;

import com.higgschain.trust.consensus.term.ITermManager;
import com.higgschain.trust.consensus.term.TermInfo;
import com.higgschain.trust.consensus.view.ClusterView;
import com.higgschain.trust.consensus.view.IClusterViewManager;
import com.higgschain.trust.consensus.view.LastPackage;
import com.higgschain.trust.consensus.snapshot.IConsensusSnapshot;
import io.atomix.utils.serializer.Namespace;
import io.atomix.utils.serializer.Namespaces;
import io.atomix.utils.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Consensus snapshot.
 *
 * @author suimi
 * @date 2018 /6/4
 */
@Slf4j @Component public class ConsensusSnapshot implements IConsensusSnapshot {

    @Autowired private ITermManager ITermManager;

    @Autowired private IClusterViewManager viewManager;

    private Serializer serializer;

    /**
     * Instantiates a new Consensus snapshot.
     */
    public ConsensusSnapshot() {
        //@formatter:off
        Namespace namespace = Namespace.builder()
            .setRegistrationRequired(false)
            .setCompatible(true)
            .register(Namespaces.BASIC)
            .register(SnapshotInfo.class)
            .register(TermInfo.class)
            .register(ClusterView.class)
            .register(LastPackage.class)
            .build();
        //@formatter:on
        serializer = Serializer.using(namespace);
    }

    @Override public byte[] getSnapshot() {
        SnapshotInfo snapshotInfo = new SnapshotInfo();
        snapshotInfo.setTerms(ITermManager.getTerms());
        snapshotInfo.setVies(viewManager.getViews());
        snapshotInfo.setLastPackage(viewManager.getLastPackage());
        log.info("get snapshot:{}", snapshotInfo);
        return serializer.encode(snapshotInfo);
    }

    @Override public void installSnapshot(byte[] snapshot) {
        SnapshotInfo snapshotInfo = serializer.decode(snapshot);
        log.info("install snapshot:{}", snapshotInfo);
        ITermManager.resetTerms(snapshotInfo.getTerms());
        viewManager.resetViews(snapshotInfo.getVies());
        viewManager.resetLastPackage(snapshotInfo.getLastPackage());
    }

}