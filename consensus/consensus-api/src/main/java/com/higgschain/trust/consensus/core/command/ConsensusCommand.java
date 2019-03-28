package com.higgschain.trust.consensus.core.command;

import java.io.Serializable;

public interface ConsensusCommand<T> extends Serializable {
    T get();
}
