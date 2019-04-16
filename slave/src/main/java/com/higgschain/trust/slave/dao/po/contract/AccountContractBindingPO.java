package com.higgschain.trust.slave.dao.po.contract;

import com.higgschain.trust.common.mybatis.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Account contract binding po.
 */
@Getter @Setter public class AccountContractBindingPO extends BaseEntity<AccountContractBindingPO> {
    private Long id;
    private Long blockHeight;
    private String txId;
    private int actionIndex;
    private String accountNo;
    private String contractAddress;
    private String args;
    private String hash;
    private Date createTime;
}
