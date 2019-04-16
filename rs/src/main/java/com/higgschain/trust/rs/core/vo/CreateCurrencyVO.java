package com.higgschain.trust.rs.core.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * The type Create currency vo.
 *
 * @author liuyu
 * @description
 * @date 2019 -03-27
 */
@Getter
@Setter
public class CreateCurrencyVO extends BaseVO{
    @NotNull
    private String requestId;
    @NotNull
    private String currency;
    @NotNull
    @Max(64)
    private String address;
    @NotNull
    private BigDecimal amount;
}
