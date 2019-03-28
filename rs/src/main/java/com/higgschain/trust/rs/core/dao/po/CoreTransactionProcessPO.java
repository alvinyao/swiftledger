package com.higgschain.trust.rs.core.dao.po;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.util.Date;@Getter@Setterpublic class CoreTransactionProcessPO extends BaseEntity<CoreTransactionProcessPO> {    private static final long serialVersionUID = 1L;    /**     * transaction id     */    private String txId;    /**     * tx status: 1.INIT 2.NEED_VOTE/WAIT 3.PERSISTED 4.END     */    private String status;    /**     * create time     */    private Date createTime;    /**     * update time     */    private Date updateTime;}