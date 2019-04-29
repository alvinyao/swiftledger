/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.term;

/**
 * The interface Term command.
 */
public interface TermCommand {

    /**
     * Gets term.
     *
     * @return the term
     */
    long getTerm();

    /**
     * Gets package height.
     *
     * @return the package height
     */
    long getPackageHeight();

    /**
     * Gets node name.
     *
     * @return the node name
     */
    String getNodeName();

}
