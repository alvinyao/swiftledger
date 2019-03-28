package com.higgschain.trust.slave.dao.po.account;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.math.BigDecimal;import java.util.Date;/** * @author liuyu * @description debit&credit record for account * @date 2018-03-27 */@Getter @Setter public class AccountDcRecordPO extends BaseEntity<AccountDcRecordPO> {    /**     * id     */    private Long id;    /**     * business flow number     */    private String bizFlowNo;    /**     * number of account     */    private String accountNo;    /**     * dc flag-DEBIT,CREDIT     */    private String dcFlag;    /**     * happen amount     */    private BigDecimal amount;    /**     * create time     */    private Date createTime;}