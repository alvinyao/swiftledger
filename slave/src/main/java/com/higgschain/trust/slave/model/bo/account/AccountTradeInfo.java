package com.higgschain.trust.slave.model.bo.account;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * The type Account trade info.
 *
 * @author liuyu
 * @description Account transaction information
 * @date 2018 -03-28
 */
@Getter @Setter public class AccountTradeInfo extends BaseBO {
    /**
     * number of account
     */
    @NotNull @Length(min = 1, max = 64) private String accountNo;
    /**
     * happen amount,allow the negative
     */
    @NotNull private BigDecimal amount;

    /**
     * Instantiates a new Account trade info.
     */
    public AccountTradeInfo() {
    }

    /**
     * Instantiates a new Account trade info.
     *
     * @param accountNo the account no
     * @param amount    the amount
     */
    public AccountTradeInfo(String accountNo, BigDecimal amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }
}
