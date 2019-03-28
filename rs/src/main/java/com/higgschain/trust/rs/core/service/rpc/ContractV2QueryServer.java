package com.higgschain.trust.rs.core.service.rpc;

import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.bo.ContractQueryStateV2BO;
import com.higgschain.trust.slave.api.ContractQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lingchao
 * @datetime:2019-01-06 19:16
 **/
@ConditionalOnProperty(name = "higgs.trust.isSlave", havingValue = "true", matchIfMissing = true)
@Slf4j
@Service
public class ContractV2QueryServer implements InitializingBean {

    @Autowired
    private ContractQueryService contractQueryService;

    @Autowired
    private NetworkManage networkManage;

    private List<?> query(ContractQueryStateV2BO contractQueryStateV2BO) {
        if (StringUtils.isBlank(contractQueryStateV2BO.getAddress()) || StringUtils.isBlank(contractQueryStateV2BO.getMethod())) {
            log.error("address :{} or method :{} can not be null!", contractQueryStateV2BO.getAddress(), contractQueryStateV2BO.getMethod());
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_PARAM_ERROR);
        }
        List<?> list = new ArrayList<>();
        try {
            list = contractQueryService.query2(contractQueryStateV2BO.getBlockHeight(), contractQueryStateV2BO.getAddress(), contractQueryStateV2BO.getMethod(), contractQueryStateV2BO.getParameters());
        } catch (Throwable e) {
            log.error("query error for contractQueryStateV2BO:{}", contractQueryStateV2BO);
        }
        return list;
    }

    @Override
    public void afterPropertiesSet() {
        networkManage.registerHandler("/contract/v2/query", this::query);
    }
}
