/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.node.command;

public interface TermCommand {

    long getTerm();

    long getPackageHeight();

    String getNodeName();

}
