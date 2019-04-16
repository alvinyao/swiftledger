package com.higgschain.trust.slave.api.vo.diagnosis;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract code vo.
 *
 * @author Chen Jiawei
 * @date 2019 -01-21
 */
@Getter
@Setter
public class ContractCodeVO extends BaseBO {
    /**
     * Contract address, a hex string with 40 characters.
     */
    private String address;
    /**
     * Height of block in which contract exists.
     */
    private Long height;
    /**
     * Contract stored code.
     */
    private String code;
}
