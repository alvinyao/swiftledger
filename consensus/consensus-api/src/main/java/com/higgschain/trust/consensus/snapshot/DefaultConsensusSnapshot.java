/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.snapshot;

/**
 * The type Default consensus snapshot.
 */
public class DefaultConsensusSnapshot implements IConsensusSnapshot {

    @Override public byte[] getSnapshot() {
        return "N/A".getBytes();
    }

    @Override public void installSnapshot(byte[] snapshot) {
        return;
    }
}
