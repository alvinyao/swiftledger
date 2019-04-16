package com.higgschain.trust.rs.core.vo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * The type Multi sign tx vo.
 *
 * @author liuyu
 * @description
 * @date 2019 -03-20
 */
@Getter
@Setter
public class MultiSignTxVO extends BaseBO {
    /**
     *
     */
    @NotNull
    @Length(min = 1,max = 64)
    private String requestId;
    /**
     * currency name
     */
    @NotNull
    private String currency;
    /**
     * from address the Multi-Sign contract address
     */
    @NotNull
    @Length(min = 1,max = 64)
    private String fromAddr;
    /**
     *to address
     */
    @NotNull
    @Length(min = 1,max = 64)
    private String toAddr;
    /**
     * the amount incurred
     */
    @NotNull
    private BigDecimal amount;
    /**
     * is multi-sign
     */
    private boolean multiSign;
    /**
     *  signature information
     */
    @NotEmpty
    private List<String> signs;
}
