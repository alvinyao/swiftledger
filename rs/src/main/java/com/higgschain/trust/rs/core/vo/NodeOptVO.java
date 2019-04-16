package com.higgschain.trust.rs.core.vo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Node opt vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -09-05
 */
@Getter
@Setter
public class NodeOptVO extends BaseBO {
    /**
     * node name of join or leave
     */
    @NotNull private String nodeName;
    /**
     * the sign of original value
     */
    @NotNull private String sign;
    /**
     * the original value for sign
     */
    @NotNull private String signValue;
    /**
     * node pubKey
     */
    @NotNull private String pubKey;
}
