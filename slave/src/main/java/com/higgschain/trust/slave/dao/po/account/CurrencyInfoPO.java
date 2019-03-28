package com.higgschain.trust.slave.dao.po.account;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.util.Date;/** * @author liuyu * @description currency info * @date 2018-03-27 */@Getter @Setter public class CurrencyInfoPO extends BaseEntity<CurrencyInfoPO> {    /**     * id     */    private Long id;    /**     * currency     */    private String currency;    /**     * remark     */    private String remark;    /**     * create time     */    private Date createTime;    /**     * currency crypto id     */    private String homomorphicPk;    /**     * contract address     */    private String contractAddress;}