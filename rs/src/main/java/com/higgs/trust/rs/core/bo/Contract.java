package com.higgs.trust.rs.core.bo;

import com.higgs.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duhongming
 * @date 2018/5/15
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
