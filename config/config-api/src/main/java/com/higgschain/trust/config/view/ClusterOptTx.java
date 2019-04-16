/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Cluster opt tx.
 *
 * @author suimi
 * @date 2018 /9/4
 */
@Data @NoArgsConstructor public class ClusterOptTx {

    /**
     * the node name
     */
    private String nodeName;

    /**
     * node public key
     */
    private String pubKey;

    /**
     * self sign
     */
    private String selfSign;

    /**
     * self sign value
     */
    private String selfSignValue;

    /**
     * operation tx signature value
     */
    private String signatureValue;

    private Operation operation;

    /**
     * the signature of current cluster
     */
    private List<SignatureInfo> signatureList;

    /**
     * The enum Operation.
     */
    public enum Operation {/**
     * Join operation.
     */
    JOIN,
        /**
         * Leave operation.
         */
        LEAVE
    }

    /**
     * The type Signature info.
     */
    @Data @AllArgsConstructor @NoArgsConstructor public static class SignatureInfo {
        private String signer;
        private String sign;
    }

}
