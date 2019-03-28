package com.higgschain.trust.rs.core.controller.explorer.vo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author liuyu
 * @description
 * @date 2018-07-25
 */
@Getter @Setter public class QueryBlockByHeightVO extends BaseBO {
    @NotNull
    private Long height;
}
