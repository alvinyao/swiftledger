/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.command;

/**
 * The interface Signature command.
 *
 * @author suimi
 * @date 2018 /6/1
 */
public interface SignatureCommand {

    /**
     * Gets view.
     *
     * @return the view
     */
    long getView();

    /**
     * Gets node name.
     *
     * @return the node name
     */
    String getNodeName();

    /**
     * Gets sign value.
     *
     * @return the sign value
     */
    String getSignValue();

    /**
     * Gets signature.
     *
     * @return the signature
     */
    String getSignature();
}
