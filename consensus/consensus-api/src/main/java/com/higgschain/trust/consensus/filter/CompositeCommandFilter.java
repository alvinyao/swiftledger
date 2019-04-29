/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.filter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Composite command filter.
 *
 * @author suimi
 * @date 2018 /6/1
 */
@Component public class CompositeCommandFilter {
    
    @Autowired private List<CommandFilter> filters;

    /**
     * Do filter.
     *
     * @param commit the commit
     */
    public void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit) {
        new VirtualCommandFilterChain(filters).doFilter(commit);
    }
}
