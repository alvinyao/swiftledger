package com.higgschain.trust.slave.model.bo.manage;

import com.higgschain.trust.slave.api.enums.manage.DecisionTypeEnum;
import com.higgschain.trust.slave.core.service.snapshot.agent.MerkleTreeSnapshotAgent;
import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Policy.
 *
 * @author tangfashuang
 * @desc policy bo
 * @date 2018 -04-02
 */
@Setter @Getter public class Policy extends BaseBO implements MerkleTreeSnapshotAgent.MerkleDataNode {

    /**
     * policy id
     */
    private String policyId;

    /**
     * policy name
     */
    private String policyName;

    /**
     * rs ids of related to policy
     */
    private List<String> rsIds;
    /**
     * the decision type for vote ,1.FULL_VOTE,2.ONE_VOTE,3.ASSIGN_NUM
     */
    private DecisionTypeEnum decisionType;
    /**
     * the contract address for vote rule
     */
    private String contractAddr;
    /**
     * the number to verify
     */
    private int verifyNum;
    /**
     * rs-ids that must be verified
     */
    private List<String> mustRsIds;

    @Override public String getUniqKey() {
        return policyId;
    }
}
