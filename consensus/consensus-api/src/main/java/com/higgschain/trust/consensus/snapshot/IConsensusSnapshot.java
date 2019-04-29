/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.snapshot;

/**
 * The interface Consensus snapshot.
 */
public interface IConsensusSnapshot {

    /**
     * Get snapshot byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getSnapshot();

    /**
     * Install snapshot.
     *
     * @param snapshot the snapshot
     */
    void installSnapshot(byte[] snapshot);
}
