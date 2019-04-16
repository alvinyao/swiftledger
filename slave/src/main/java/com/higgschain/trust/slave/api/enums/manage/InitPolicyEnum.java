package com.higgschain.trust.slave.api.enums.manage;

/**
 * The enum Init policy enum.
 *
 * @author tangfashuang
 * @date 2018 /04/13 16:33
 * @desc initial policy
 */
public enum InitPolicyEnum {/**
 * The Na.
 */
NA("NA", "000000", VotePatternEnum.SYNC,DecisionTypeEnum.FULL_VOTE, "NA type"),
    /**
     * The Register policy.
     */
    REGISTER_POLICY("REGISTER_POLICY", "000001",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE, "register policy"),
    /**
     * The Register rs.
     */
    REGISTER_RS("REGISTER_RS", "000002",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE, "register rs"),
    /**
     * The Utxo issue.
     */
    UTXO_ISSUE("UTXO_ISSUE", "000003", VotePatternEnum.SYNC,DecisionTypeEnum.FULL_VOTE,"utxo issue"),
    /**
     * The Utxo destroy.
     */
    UTXO_DESTROY("UTXO_DESTROY", "000004", VotePatternEnum.SYNC,DecisionTypeEnum.FULL_VOTE,"utxo destroy"),
    /**
     * The Contract issue.
     */
    CONTRACT_ISSUE("CONTRACT_ISSUE", "000005", VotePatternEnum.SYNC,DecisionTypeEnum.FULL_VOTE,"contract issue"),
    /**
     * The Contract destroy.
     */
    CONTRACT_DESTROY("CONTRACT_DESTROY", "000006",VotePatternEnum.SYNC,DecisionTypeEnum.FULL_VOTE, "contract destroy"),
    /**
     * The Ca auth.
     */
    CA_AUTH("CA_AUTH", "000007",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE,"ca auth"),
    /**
     * The Ca update.
     */
    CA_UPDATE("CA_UPDATE", "000008",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE,"ca update"),
    /**
     * The Ca cancel.
     */
    CA_CANCEL("CA_CANCEL", "000009",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE,"ca cancel"),
    /**
     * The Cancel rs.
     */
    CANCEL_RS("CANCEL_RS", "000010", VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE, "cancel rs"),
    /**
     * The Node join.
     */
    NODE_JOIN("NODE_JOIN", "000011",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE,"node join"),
    /**
     * The Node leave.
     */
    NODE_LEAVE("NODE_LEAVE", "000012",VotePatternEnum.ASYNC,DecisionTypeEnum.FULL_VOTE,"node leave"),
    /**
     * The Contract invoke.
     */
    CONTRACT_INVOKE("CONTRACT_INVOKE", "000013",VotePatternEnum.SYNC,DecisionTypeEnum.FULL_VOTE, "contract invoke")
    ;
    private String type;

    private String policyId;

    private VotePatternEnum votePattern;

    private DecisionTypeEnum decisionType;

    private String desc;

    InitPolicyEnum(String type, String policyId,VotePatternEnum votePattern,DecisionTypeEnum decisionType,String desc) {
        this.type = type;
        this.policyId = policyId;
        this.votePattern = votePattern;
        this.decisionType = decisionType;
        this.desc = desc;
    }

    /**
     * Gets init policy enum by type.
     *
     * @param type the type
     * @return the init policy enum by type
     */
    public static InitPolicyEnum getInitPolicyEnumByType(String type) {
        for (InitPolicyEnum initPolicyEnum : InitPolicyEnum.values()) {
            if (initPolicyEnum.getType().equals(type)) {
                return initPolicyEnum;
            }
        }
        return null;
    }

    /**
     * Gets init policy enum by policy id.
     *
     * @param policyId the policy id
     * @return the init policy enum by policy id
     */
    public static InitPolicyEnum getInitPolicyEnumByPolicyId(String policyId) {
        for (InitPolicyEnum initPolicyEnum : InitPolicyEnum.values()) {
            if (initPolicyEnum.getPolicyId().equals(policyId)) {
                return initPolicyEnum;
            }
        }
        return null;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets policy id.
     *
     * @return the policy id
     */
    public String getPolicyId() {
        return policyId;
    }

    /**
     * Get vote pattern vote pattern enum.
     *
     * @return the vote pattern enum
     */
    public VotePatternEnum getVotePattern(){ return votePattern;}

    /**
     * Gets decision type.
     *
     * @return the decision type
     */
    public DecisionTypeEnum getDecisionType() {
        return decisionType;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
