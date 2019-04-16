package com.higgschain.trust.slave.api.vo;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract query vo.
 *
 * @author duhongming
 * @date 2018 /5/16
 */
@Getter
@Setter
public class ContractQueryVO extends BaseBO {
    private Long height;
    private String txId;
    private Integer pageIndex;
    private Integer pageSize;
}
