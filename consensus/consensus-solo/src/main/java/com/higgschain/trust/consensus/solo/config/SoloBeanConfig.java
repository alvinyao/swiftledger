package com.higgschain.trust.consensus.solo.config;

import com.higgschain.trust.consensus.filter.CompositeCommandFilter;
import com.higgschain.trust.consensus.core.replicate.AbstractCommitReplicateComposite;
import com.higgschain.trust.consensus.core.replicate.DefaultCommitReplicateComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Solo bean config.
 *
 * @author suimi
 * @date 2019 /2/20
 */
@Configuration public class SoloBeanConfig {

    /**
     * Replicate composite abstract commit replicate composite.
     *
     * @param filter the filter
     * @return the abstract commit replicate composite
     */
    @Autowired @Bean public AbstractCommitReplicateComposite replicateComposite(CompositeCommandFilter filter) {
        return new DefaultCommitReplicateComposite(filter);
    }

}
