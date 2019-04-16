package com.higgschain.trust.rs.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract query request.
 *
 * @author duhongming
 * @date 2018 /6/24
 */
@Getter
@Setter
public class ContractQueryRequest {
    private String address;
    private String methodName;
    private Object[] bizArgs;
}
