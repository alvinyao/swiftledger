package com.higgschain.trust.slave.dao.po.utxo;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.util.Date;/** * TxOutPO po * * @author lingchao * @create 2018年03月27日19:19 */@Getter @Setter public class TxOutPO extends BaseEntity<TxOutPO> {    private static final long serialVersionUID = 1L;    /**     * transaction id     */    private String txId;    /**     * index for the out in the transaction     */    private Integer index;    /**     * index for the action of the out in the transaction     */    private Integer actionIndex;    /**     * identity id for the attribution of the row:data owner and chain owner     */    private String identity;    /**     * the state class name     */    private String stateClass;    /**     * sate data     */    private String state;    /**     * contract address     */    private String contractAddress;    /**     * the status of the out: 1.UNSPENT 2.SPENT     */    private String status;    /**     * the transaction id to spend the out     */    private String sTxId;    /**     * create time     */    private Date createTime;    /**     * update time     */    private Date updateTime;}