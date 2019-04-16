package com.higgschain.trust.rs.core.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liuyu
 * @description
 * @date 2019-03-27
 */
@Getter
@Setter
public class CreateCurrencyVO extends BaseVO{
    @NotNull
    @Length(min = 1,max = 64)
    private String requestId;
    @NotNull
    private String currency;
    @NotNull
    @Length(min = 1,max = 64)
    private String address;
    @NotNull
    private BigDecimal amount;
}
