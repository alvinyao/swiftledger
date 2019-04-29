package com.higgschain.trust.consensus.bftsmartcustom.started.custom;

import com.higgschain.trust.consensus.bftsmartcustom.started.custom.adapter.SmartCommitAdapter;
import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.filter.CompositeCommandFilter;
import com.higgschain.trust.consensus.core.replicate.AbstractCommitReplicateComposite;
import org.springframework.stereotype.Component;

/**
 * The type Smart commit replicate composite.
 *
 * @author: zhouyafeng
 * @create: 2018 /06/05 17:47
 * @description:
 */
@Component public class SmartCommitReplicateComposite extends AbstractCommitReplicateComposite {

    /**
     * Instantiates a new Smart commit replicate composite.
     *
     * @param filter the filter
     */
    public SmartCommitReplicateComposite(CompositeCommandFilter filter) {
        super(filter);
    }

    @Override public ConsensusCommit commitAdapter(Object request) {
        return new SmartCommitAdapter(request);
    }
}