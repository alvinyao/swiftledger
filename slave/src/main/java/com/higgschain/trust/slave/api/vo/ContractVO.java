package com.higgschain.trust.slave.api.vo;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Contract vo.
 *
 * @author duhongming
 * @date 2018 /5/16
 */
@Getter
@Setter
public class ContractVO  extends BaseBO {
    private String address;
    private Long blockHeight;
    private String txId;
    private Integer actionIndex;
    private String language;
    private String version;
    private String code;
    private Date createTime;
}
