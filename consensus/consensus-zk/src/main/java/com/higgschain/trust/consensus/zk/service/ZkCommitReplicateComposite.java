package com.higgschain.trust.consensus.zk.service;

import com.higgschain.trust.consensus.core.AbstractCommitReplicateComposite;
import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.core.filter.CompositeCommandFilter;
import com.higgschain.trust.consensus.zk.adapter.ZkCommitAdapter;
import org.springframework.stereotype.Component;

/**
 * The type Zk commit replicate composite.
 *
 * @author: zhouyafeng
 * @create: 2018 /09/07 10:29
 * @description:
 */
@Component
public class ZkCommitReplicateComposite extends AbstractCommitReplicateComposite {
    /**
     * Instantiates a new Zk commit replicate composite.
     *
     * @param filter the filter
     */
    public ZkCommitReplicateComposite(CompositeCommandFilter filter) {
        super(filter);
    }

    @Override public ConsensusCommit commitAdapter(Object request) {
        return new ZkCommitAdapter(request);
    }
}