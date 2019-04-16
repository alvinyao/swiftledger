package com.higgschain.trust.slave.model.bo.contract;

import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract state migration action.
 *
 * @author duhongming
 * @date 2018 /6/21
 */
@Getter
@Setter
public class ContractStateMigrationAction extends Action {
    private String formInstanceAddress;
    private String toInstanceAddress;
}
