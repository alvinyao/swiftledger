package com.higgs.trust.rs.core.dao.po;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.util.Date;@Getter @Setter public class VoteRequestRecordPO extends BaseEntity<VoteRequestRecordPO> {    /**     * id     */    private Long id;    /**     * transaction id     */    private String txId;    /**     * the rsId of the sender for the tx     */    private String sender;    /**     * the tx data     */    private String txData;    /**     * sign     */    private String sign;    /**     * vote result 1.INIT 2.AGREE 3.DISAGREE     */    private String voteResult;    /**     * create time     */    private Date createTime;    /**     * update time     */    private Date updateTime;}