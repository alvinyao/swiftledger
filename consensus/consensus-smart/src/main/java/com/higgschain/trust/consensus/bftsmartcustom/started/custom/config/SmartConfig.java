package com.higgschain.trust.consensus.bftsmartcustom.started.custom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * The type Smart config.
 */
@Configuration() @ConfigurationProperties(prefix = "bftSmart.systemConfigs") public class SmartConfig {

    private Map<String, String> configs;

    private String hostsConfig;

    private String ttpPubKey;

    private String myId;

    private String myClientId;

    private Map<String, String> idNodeNameMap;

    private String defaultDir;

    /**
     * Gets default dir.
     *
     * @return the default dir
     */
    public String getDefaultDir() {
        return defaultDir;
    }

    /**
     * Sets default dir.
     *
     * @param defaultDir the default dir
     */
    public void setDefaultDir(String defaultDir) {
        this.defaultDir = defaultDir;
    }

    /**
     * Gets id node name map.
     *
     * @return the id node name map
     */
    public Map<String, String> getIdNodeNameMap() {
        return idNodeNameMap;
    }

    /**
     * Sets id node name map.
     *
     * @param idNodeNameMap the id node name map
     */
    public void setIdNodeNameMap(Map<String, String> idNodeNameMap) {
        this.idNodeNameMap = idNodeNameMap;
    }

    /**
     * Gets configs.
     *
     * @return the configs
     */
    public Map<String, String> getConfigs() {
        return configs;
    }

    /**
     * Sets configs.
     *
     * @param configs the configs
     */
    public void setConfigs(Map<String, String> configs) {
        this.configs = configs;
    }

    /**
     * Gets hosts config.
     *
     * @return the hosts config
     */
    public String getHostsConfig() {
        return hostsConfig;
    }

    /**
     * Sets hosts config.
     *
     * @param hostsConfig the hosts config
     */
    public void setHostsConfig(String hostsConfig) {
        this.hostsConfig = hostsConfig;
    }

    /**
     * Gets my id.
     *
     * @return the my id
     */
    public String getMyId() {
        return myId;
    }

    /**
     * Sets my id.
     *
     * @param myId the my id
     */
    public void setMyId(String myId) {
        this.myId = myId;
    }

    /**
     * Gets my client id.
     *
     * @return the my client id
     */
    public String getMyClientId() {
        return myClientId;
    }

    /**
     * Sets my client id.
     *
     * @param myClientId the my client id
     */
    public void setMyClientId(String myClientId) {
        this.myClientId = myClientId;
    }

    /**
     * Gets ttp pub key.
     *
     * @return the ttp pub key
     */
    public String getTtpPubKey() {
        return ttpPubKey;
    }

    /**
     * Sets ttp pub key.
     *
     * @param ttpPubKey the ttp pub key
     */
    public void setTtpPubKey(String ttpPubKey) {
        this.ttpPubKey = ttpPubKey;
    }
}
