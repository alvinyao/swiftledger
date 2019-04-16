package com.higgschain.trust.slave.model.bo.contract;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Contract.
 *
 * @author duhongming
 * @description the modal of Contract
 * @date 2018 -04-08
 */
@Getter @Setter public class Contract extends BaseBO {
    private String address;
    private Long blockHeight;
    private String txId;
    private Integer actionIndex;
    private String language;
    private String version;
    private String code;
    private Date createTime;
}
