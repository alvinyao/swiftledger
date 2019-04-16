package com.higgschain.trust.rs.core.api;

import java.util.List;

/**
 * The interface Contract v 2 query service.
 *
 * @author: lingchao
 * @datetime:2019-01-05 23 :28
 */
public interface ContractV2QueryService {
    /**
     * query state for distribute RS and local RS with slave
     *
     * @param blockHeight     the block height
     * @param contractAddress the contract address
     * @param methodSignature the method signature
     * @param methodInputArgs the method input args
     * @return list
     */
    List<?> query(Long blockHeight, String contractAddress, String methodSignature, Object... methodInputArgs);
}
