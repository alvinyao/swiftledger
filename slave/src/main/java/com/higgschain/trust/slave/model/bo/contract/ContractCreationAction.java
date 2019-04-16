package com.higgschain.trust.slave.model.bo.contract;

import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract creation action.
 *
 * @author duhongming
 * @description smart contract create action
 * @date 2018 -04-08
 */
@Getter @Setter public class ContractCreationAction extends Action {

    private String language;
    private String version;
    private String code;
    private Object[] initArgs;
}
