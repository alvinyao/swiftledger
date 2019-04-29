/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.exception;

import com.higgschain.trust.common.exception.ErrorInfo;

/**
 * The enum Consensus error.
 *
 * @author suimi
 * @date 2018 /6/12
 */
public enum ConsensusError implements ErrorInfo {

    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    //                         错误码段[100-199]                                //
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//

    /**
     * The Config node state change failed.
     */
    //@formatter:off
    CONFIG_NODE_STATE_CHANGE_FAILED("101", "node state change failed ", false),
    /**
     * The Config node state invalid.
     */
    CONFIG_NODE_STATE_INVALID("101", "node state invalid", false),
    /**
     * The Config node master term incorrect.
     */
    //--------master[110-120]
    CONFIG_NODE_MASTER_TERM_INCORRECT("110","the term is incorrect",false),
    /**
     * The Config node master term package height incorrect.
     */
    CONFIG_NODE_MASTER_TERM_PACKAGE_HEIGHT_INCORRECT("112","the package height is incorrect",false),
    /**
     * The Config node master node incorrect.
     */
    CONFIG_NODE_MASTER_NODE_INCORRECT("113","the master node is incorrect",false),
    /**
     * The Config node state change invoke failed.
     */
    CONFIG_NODE_STATE_CHANGE_INVOKE_FAILED("114","the state change listener invoke failed",false),
    /**
     * The Config view package height incorrect.
     */
    CONFIG_VIEW_PACKAGE_HEIGHT_INCORRECT("115","the package height is incorrect at view",false),
    /**
     * The Config view unsupported operation.
     */
    CONFIG_VIEW_UNSUPPORTED_OPERATION("116","unsupported view operation",false),
    ;

    //@formatter:on

    /**
     * 枚举编码
     */
    private final String code;

    /**
     * 描述说明
     */
    private final String description;

    /**
     * 是否需要重试
     */
    private final boolean needRetry;

    /**
     * 私有构造函数。
     *
     * @param code        枚举编码
     * @param description 描述说明
     */
    private ConsensusError(String code, String description, boolean needRetry) {
        this.code = code;
        this.description = description;
        this.needRetry = needRetry;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public boolean isNeedRetry() {
        return needRetry;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code 枚举编码
     * @return 错误场景枚举 by code
     */
    public static ConsensusError getByCode(String code) {
        for (ConsensusError scenario : values()) {
            if (scenario.getCode().equals(code)) {

                return scenario;
            }
        }
        return null;
    }
}
