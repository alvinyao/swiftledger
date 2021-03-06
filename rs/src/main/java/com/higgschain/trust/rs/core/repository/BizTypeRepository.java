package com.higgschain.trust.rs.core.repository;

import com.higgschain.trust.rs.common.config.RsConfig;
import com.higgschain.trust.rs.core.dao.BizTypeDao;
import com.higgschain.trust.rs.core.dao.po.BizTypePO;
import com.higgschain.trust.rs.core.dao.rocks.BizTypeRocksDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Biz type repository.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-13
 */
@Slf4j @Repository public class BizTypeRepository implements InitializingBean {
    @Autowired private RsConfig rsConfig;
    @Autowired private BizTypeDao bizTypeDao;
    @Autowired private BizTypeRocksDao bizTypeRocksDao;

    private Map<String, String> bizTypeMap = new HashMap<>();

    @Override public void afterPropertiesSet() throws Exception {
        List<BizTypePO> bizTypePOList;
        if (rsConfig.isUseMySQL()) {
            bizTypePOList = bizTypeDao.queryAll();
        } else {
            bizTypePOList = bizTypeRocksDao.queryAll();
        }

        if (!CollectionUtils.isEmpty(bizTypePOList)) {
            for (BizTypePO bizTypePO : bizTypePOList) {
                bizTypeMap.put(bizTypePO.getPolicyId(), bizTypePO.getBizType());
            }
        }
    }

    /***
     * get bizType by policyId
     *
     * @param policyId the policy id
     * @return by policy id
     */
    public String getByPolicyId(String policyId) {
        if (bizTypeMap.containsKey(policyId)) {
            return bizTypeMap.get(policyId);
        }
        BizTypePO bizTypePO;
        if (!rsConfig.isUseMySQL()) {
            bizTypePO = bizTypeRocksDao.get(policyId);
        } else {
            bizTypePO = bizTypeDao.queryByPolicyId(policyId);
        }
        if (bizTypePO == null) {
            log.info("[getByPolicyId]bizTypePO is empty policyId:{}", policyId);
            return null;
        }
        bizTypeMap.put(policyId, bizTypePO.getBizType());
        return bizTypePO.getBizType();
    }

    /**
     * Add string.
     *
     * @param policyId the policy id
     * @param bizType  the biz type
     * @return the string
     */
    public String add(String policyId, String bizType) {
        if (bizTypeMap.containsKey(policyId)) {
            return "biz type already exist";
        }

        BizTypePO bizTypePO = new BizTypePO();
        bizTypePO.setPolicyId(policyId);
        bizTypePO.setBizType(bizType);

        try {
            if (rsConfig.isUseMySQL()) {
                if (null != bizTypeDao.queryByPolicyId(policyId)) {
                    return "biz type already exist";
                }
                bizTypeDao.add(bizTypePO);
            } else {
                bizTypeRocksDao.add(bizTypePO);
            }
        } catch (Throwable e) {
            log.error("add biz type failed, policyId={}", policyId, e);
            return "add biz type failed";
        }
        bizTypeMap.put(policyId, bizType);
        return "add biz type success";
    }

    /**
     * Update string.
     *
     * @param policyId the policy id
     * @param bizType  the biz type
     * @return the string
     */
    public String update(String policyId, String bizType) {
        if (!bizTypeMap.containsKey(policyId)) {
            return "biz type is not exist";
        }

        try {
            if (rsConfig.isUseMySQL()) {
                if (null == bizTypeDao.queryByPolicyId(policyId)) {
                    return "biz type is not exist";
                }
                bizTypeDao.update(policyId, bizType);
            } else {
                bizTypeRocksDao.update(policyId, bizType);
            }
        } catch (Throwable e) {
            log.error("update biz type failed, policyId={}", policyId, e);
            return "update biz type failed";
        }
        bizTypeMap.put(policyId, bizType);
        return "update biz type success";
    }

}
