/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.filter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;

import java.util.List;

/**
 * The type Virtual command filter chain.
 *
 * @author suimi
 * @date 2018 /6/1
 */
public class VirtualCommandFilterChain implements CommandFilterChain {

    private List<CommandFilter> filters;

    private int currentPosition = 0;

    /**
     * Instantiates a new Virtual command filter chain.
     *
     * @param filters the filters
     */
    public VirtualCommandFilterChain(List<CommandFilter> filters) {
        this.filters = filters;
    }

    public void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit) {
        if (this.currentPosition != this.filters.size()) {
            this.currentPosition++;
            CommandFilter nextFilter = this.filters.get(this.currentPosition - 1);
            nextFilter.doFilter(commit, this);
        }
    }

}
