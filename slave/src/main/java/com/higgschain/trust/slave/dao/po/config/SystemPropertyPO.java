package com.higgschain.trust.slave.dao.po.config;import com.higgschain.trust.common.mybatis.BaseEntity;import lombok.Getter;import lombok.Setter;import java.util.Date;/** * @author lingchao * @create 2018年06月27日15:58 */@Setter@Getterpublic class SystemPropertyPO extends BaseEntity<SystemPropertyPO> {    /**     * property key     */    private String key;    /**     *  property value     */    private String value;    /**     * desc for the property     */    private String desc;    /**     * the create time     */    private Date createTime;    /**     * the update time     */    private Date updateTime;}