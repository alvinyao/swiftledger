package com.higgschain.trust.slave.model.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Data identity.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-10
 */
@Getter @Setter public class DataIdentity extends BaseBO {
    /**
     * identity of data
     */
    private String identity;
    /**
     * chain of owner
     */
    private String chainOwner;
    /**
     * data owner
     */
    private String dataOwner;
    /**
     * create time
     */
    private Date createTime;
}
