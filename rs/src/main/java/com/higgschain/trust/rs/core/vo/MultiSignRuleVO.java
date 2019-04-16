package com.higgschain.trust.rs.core.vo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Multi sign rule vo.
 *
 * @author liuyu
 * @description
 * @date 2019 -03-20
 */
@Getter
@Setter
public class MultiSignRuleVO extends BaseBO {
    /**
     *
     */
    @NotNull
    @Length(max = 64)
    private String requestId;
    /**
     *  addresses that participate in multiple signatures
     */
    @NotEmpty
    private List<String> addrs;
    /**
     * the number to verify
     */
    @NotNull
    @Min(1)
    private Integer verifyNum;
    /**
     * addresses that must be verify
     */
    private List<String> mustAddrs;
}
