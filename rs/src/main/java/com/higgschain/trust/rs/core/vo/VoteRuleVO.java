package com.higgschain.trust.rs.core.vo;

import com.higgschain.trust.rs.common.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Vote rule vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-08
 */
@Getter @Setter public class VoteRuleVO extends BaseBO {
    /**
     * rs vote pattern 1.SYNC 2.ASYNC
     */
    @NotNull private String votePattern;
    /**
     * callback type of slave 1.ALL 2.SELF
     */
    @NotNull private String callbackType;
}
