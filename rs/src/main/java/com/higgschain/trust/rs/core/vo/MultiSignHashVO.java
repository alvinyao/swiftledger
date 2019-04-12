package com.higgschain.trust.rs.core.vo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liuyu
 * @description
 * @date 2019-03-20
 */
@Getter
@Setter
public class MultiSignHashVO extends BaseBO {
    /**
     * from address the Multi-Sign contract address
     */
    @NotNull
    @Length(max = 64)
    private String fromAddr;
    /**
     *to address
     */
    @NotNull
    @Length(max = 64)
    private String toAddr;
    /**
     * the amount incurred
     */
    @NotNull
    private BigDecimal amount;
    /**
     * If not null, look for the token contract method;
     * otherwise, use fromAddr for the multi-sign contract address
     */
    private String currency;
    /**
     * is multi-sign
     */
    private boolean multiSign;
}
