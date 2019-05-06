package com.higgschain.trust.consensus.sofajraft.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sofajraft")
@Getter
@Setter
@ToString
public class SofajraftProperties {
    private String groupId = "trust";

    private String dataPath = "/tmp/sofajraft";

    private String serverIdStr = "127.0.0.1:9001";

    private String initConfStr = "127.0.0.1:9001,127.0.0.1:9002,127.0.0.1:9003";

    //default 10 minutes
    private int snapshotIntervalSecs = 600;

    private int electionTimeoutMs = 1000;
}
