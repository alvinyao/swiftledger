/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core;

public interface IConsensusSnapshot {

    byte[] getSnapshot();

    void installSnapshot(byte[] snapshot);
}
