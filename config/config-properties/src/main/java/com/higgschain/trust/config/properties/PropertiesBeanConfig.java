/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.properties;

import com.higgschain.trust.consensus.core.DefaultConsensusSnapshot;
import com.higgschain.trust.consensus.core.IConsensusSnapshot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Properties bean config.
 *
 * @author suimi
 * @date 2018 /9/5
 */
@Configuration public class PropertiesBeanConfig {

    /**
     * Snapshot consensus snapshot.
     *
     * @return the consensus snapshot
     */
    @Bean @ConditionalOnMissingBean(IConsensusSnapshot.class) public IConsensusSnapshot snapshot() {
        return new DefaultConsensusSnapshot();
    }

}
