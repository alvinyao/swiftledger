package com.higgschain.trust.slave.model.bo.action;

import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: abstract action class
 * @author: pengdi
 **/
@Getter @Setter public abstract class Action extends BaseBO {
    private static final long serialVersionUID = -9206591383343379207L;
    /**
     * action type
     */
    @NotNull private ActionTypeEnum type;

    /**
     * action index
     */
    @NotNull private Integer index;

}
