package com.higgschain.trust.slave.model.bo.contract;

import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.Getter;
import lombok.Setter;

/**
 * @author duhongming
 * @description the action of contract invoke
 * @date 2018-04-08
 */
@Getter @Setter public class ContractInvokeAction extends Action {

    private String address;
    private Object[] args;

}
