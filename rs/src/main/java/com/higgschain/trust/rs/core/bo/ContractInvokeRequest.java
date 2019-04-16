package com.higgschain.trust.rs.core.bo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract invoke request.
 *
 * @author duhongming
 * @date 2018 /5/18
 */
@Getter
@Setter
public class ContractInvokeRequest extends BaseBO {
    private String address;
    private Object[] bizArgs;
}
