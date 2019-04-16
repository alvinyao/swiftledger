package com.higgschain.trust.presstest.vo;

import com.higgschain.trust.rs.core.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Open account vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -08-31
 */
@Getter
@Setter
public class OpenAccountVO extends BaseVO {
    /**
     * 请求号
     */
    private String reqNo;
    /**
     * 账号
     */
    private String accountNo;
    /**
     * 币种
     */
    private String currencyName;
    /**
     * 余额方向
     * 0：借
     * 1：贷
     */
    private int fundDirection;
}
