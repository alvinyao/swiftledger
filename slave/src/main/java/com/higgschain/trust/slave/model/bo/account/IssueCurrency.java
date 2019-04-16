package com.higgschain.trust.slave.model.bo.account;

import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * The type Issue currency.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-19
 */
@Getter @Setter public class IssueCurrency extends Action {

    @NotNull @Length(min = 1, max = 24) private String currencyName;
    private String remark;

    private String HomomorphicPk;
    /**
     * contract address
     */
    private String contractAddress;
}
