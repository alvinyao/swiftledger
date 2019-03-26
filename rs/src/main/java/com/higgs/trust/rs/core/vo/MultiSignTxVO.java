package com.higgs.trust.rs.core.vo;

import com.higgs.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2019-03-20
 */
@Getter
@Setter
public class MultiSignTxVO extends BaseBO {
    /**
     *
     */
    @NotNull
    @Length(max = 64)
    private String requestId;
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
     *  signature information
     */
    @NotEmpty
    private List<String> signs;
}
