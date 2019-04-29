package com.higgschain.trust.consensus.config;

/**
 * The enum Node state enum.
 */
public enum NodeStateEnum {

    /**
     * Starting node state enum.
     */
    Starting("启动中"),
    /**
     * Initialize node state enum.
     */
    Initialize("初始化"),
    /**
     * Starting consensus node state enum.
     */
    StartingConsensus("启动协议层"),
    /**
     * Self checking node state enum.
     */
    SelfChecking("自检"),
    /**
     * Auto sync node state enum.
     */
    AutoSync("自动同步"),
    /**
     * Artificial sync node state enum.
     */
    ArtificialSync("人工同步"),
    /**
     * Standby node state enum.
     */
    Standby("备用"),
    /**
     * Running node state enum.
     */
    Running("运行中"),
    /**
     * Offline node state enum.
     */
    Offline("下线");
    /**
     * 描述说明
     */
    private final String description;

    NodeStateEnum(String description) {
        this.description = description;
    }

}
