package com.higgs.trust.rs.core.dao.po;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.util.Date;@Getter @Setter public class VoteReceiptPO extends BaseEntity<VoteReceiptPO> {    /**     * id     */    private Long id;    /**     * transaction id     */    private String txId;    /**     * the rsId of the sender for the tx     */    private String voter;    /**     * the sign data of voter     */    private String sign;    /**     * vote result 1.AGREE 2.DISAGREE     */    private String voteResult;    /**     * create time     */    private Date createTime;}