package com.higgschain.trust.rs.core.service;

import com.higgschain.trust.rs.core.api.ContractV2QueryService;
import com.higgschain.trust.slave.api.ContractQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Contract v 2 query service.
 *
 * @author: lingchao
 * @datetime:2019-01-05 23 :28
 */
@ConditionalOnProperty(name = "higgs.trust.isSlave", havingValue = "true", matchIfMissing = true)
@Slf4j
@Service
public class ContractV2QueryServiceImpl implements ContractV2QueryService {
    @Autowired
    private ContractQueryService contractQueryService;

    /**
     * query state for distribute RS and local RS with slave
     * @param blockHeight
     * @param contractAddress
     * @param methodSignature
     * @param methodInputArgs
     * @return
     */
    @Override
    public List<?> query(Long blockHeight, String contractAddress, String methodSignature, Object... methodInputArgs){
        return contractQueryService.query2(blockHeight, contractAddress, methodSignature, methodInputArgs);
    }
}
