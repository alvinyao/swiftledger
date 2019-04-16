package com.higgschain.trust.rs.core.vo;

import com.higgschain.trust.rs.common.BaseBO;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Voting request.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-07
 */
@Setter @Getter @AllArgsConstructor public class VotingRequest extends BaseBO {
    /**
     * voting sender
     */
    @NotNull private String sender;
    /**
     * transaction info
     */
    @NotNull private CoreTransaction coreTransaction;
    /**
     * voting pattern SYNC/ASYNC
     */
    @NotNull private String votePattern;
}
