package com.higgschain.trust.slave.model.bo.config;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Config.
 *
 * @author WangQuanzhou
 * @desc node configuration
 * @date 2018 /6/5 10:27
 */
@Getter @Setter public class Config extends BaseBO {

    /**
     * Instantiates a new Config.
     */
    public Config() {
    }

    /**
     * Instantiates a new Config.
     *
     * @param nodeName the node name
     */
    public Config(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Instantiates a new Config.
     *
     * @param nodeName the node name
     * @param usage    the usage
     */
    public Config(String nodeName, String usage) {
        this.nodeName = nodeName;
        this.usage = usage;
    }

    private String version;

    private boolean valid;

    private String pubKey;

    private String priKey;

    private String usage;

    private String tmpPubKey;

    private String tmpPriKey;

    private String nodeName;
}
