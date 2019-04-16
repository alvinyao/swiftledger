package com.higgschain.trust.rs.core.service;

import com.higgschain.trust.rs.core.api.BizTypeService;
import com.higgschain.trust.rs.core.repository.BizTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Biz type service.
 */
@Service @Slf4j public class BizTypeServiceImpl implements BizTypeService {
    /**
     * The Biz type repository.
     */
    @Autowired BizTypeRepository bizTypeRepository;

    @Override public String getByPolicyId(String policyId) {
        return bizTypeRepository.getByPolicyId(policyId);
    }
}
