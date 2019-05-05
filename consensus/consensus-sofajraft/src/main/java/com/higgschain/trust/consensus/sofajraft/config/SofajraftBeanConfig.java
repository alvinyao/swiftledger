package com.higgschain.trust.consensus.sofajraft.config;

import com.alipay.sofa.jraft.CliService;
import com.alipay.sofa.jraft.core.CliServiceImpl;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.impl.cli.BoltCliClientService;
import com.higgschain.trust.consensus.core.replicate.AbstractCommitReplicateComposite;
import com.higgschain.trust.consensus.core.replicate.DefaultCommitReplicateComposite;
import com.higgschain.trust.consensus.filter.CompositeCommandFilter;
import com.higgschain.trust.consensus.snapshot.DefaultConsensusSnapshot;
import com.higgschain.trust.consensus.snapshot.IConsensusSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SofajraftBeanConfig {

    @Autowired
    SofajraftProperties properties;

    /**
     * Replicate composite abstract commit replicate composite.
     *
     * @param filter the filter
     * @return the abstract commit replicate composite
     */
    @Autowired
    @Bean
    public AbstractCommitReplicateComposite replicateComposite(CompositeCommandFilter filter) {
        return new DefaultCommitReplicateComposite(filter);
    }

    /**
     * Snapshot consensus snapshot.
     *
     * @return the consensus snapshot
     */
    @Bean
    @ConditionalOnMissingBean(IConsensusSnapshot.class)
    public IConsensusSnapshot snapshot() {
        return new DefaultConsensusSnapshot();
    }

    @Bean
    public ThreadPoolTaskExecutor p2pSendExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        return threadPoolTaskExecutor;
    }


    /**
     * sofajaft client rpc service
     *
     * @return
     */
    @Bean
    public BoltCliClientService cliClientService() {
        BoltCliClientService cliClientService = new BoltCliClientService();
        cliClientService.init(new CliOptions());
        return cliClientService;
    }

    @Bean
    public CliService cliService() {
        CliService service = new CliServiceImpl();
        service.init(new CliOptions());
        return service;
    }

    @Bean
    public PeerId serverId() {
        final PeerId peerId = new PeerId();
        if (!peerId.parse(properties.getServerIdStr())) {
            throw new IllegalArgumentException("Fail to parse serverId:" + properties.getServerIdStr());
        }
        return peerId;
    }

    @Bean
    public com.alipay.sofa.jraft.conf.Configuration initConf() {
        final com.alipay.sofa.jraft.conf.Configuration initConf = new com.alipay.sofa.jraft.conf.Configuration();
        if (!initConf.parse(properties.getInitConfStr())) {
            throw new IllegalArgumentException("Fail to parse initConf:" + properties.getInitConfStr());
        }
        return initConf;
    }

}
