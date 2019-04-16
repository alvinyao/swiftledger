package com.higgschain.trust.slave.model.bo.contract;

import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract creation v 2 action.
 *
 * @author tangkun
 * @description smart contract create action v2
 * @date 2018 -11-29
 */
@Getter
@Setter
public class ContractCreationV2Action extends Action {

    private String version;

    private String code;

    /**
     * tx create address
     */
    private String from;

    /**
     * contract address
     */
    private String to;

}
