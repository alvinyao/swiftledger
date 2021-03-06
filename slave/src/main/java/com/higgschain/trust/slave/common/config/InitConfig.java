package com.higgschain.trust.slave.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.evmcontract.config.SystemProperties;
import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.datasource.DbSource;
import com.higgschain.trust.evmcontract.datasource.rocksdb.RocksDbDataSource;
import com.higgschain.trust.evmcontract.db.RepositoryRoot;
import com.higgschain.trust.slave.common.util.asynctosync.HashBlockingMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceThreadPoolTaskExecutor;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.support.NamedDaemonThreadFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * The type Init config.
 *
 * @Description:
 * @author: pengdi
 */
@Slf4j
@Configuration
@Getter
public class InitConfig {
    /**
     * The Bean factory.
     */
    @Autowired
    BeanFactory beanFactory;
    @Value("${trust.redisson.address:redis://127.0.0.1:6379}")
    private String redissonAddress;
    @Value("${trust.redisson.sentinel.masterName}")
    private String masterName;
    @Value("${trust.redisson.password}")
    private String password;

    @NotNull @Value("${trust.useMySQL:true}")
    private boolean useMySQL;
    @NotNull @Value("${trust.mockRS:false}")
    private boolean mockRS;
    @Value("${trust.utxo.display:2}")
    private int DISPLAY;

    @Value("${trust.rocksdb.file.root:/data/home/admin/trust/rocks/}")
    private String dbFileRoot;

    /**
     * Tx required transaction template.
     *
     * @param platformTransactionManager the platform transaction manager
     * @return the transaction template
     */
    @Bean(name = "txRequired")
    public TransactionTemplate txRequired(PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }

    /**
     * Tx nested transaction template.
     *
     * @param platformTransactionManager the platform transaction manager
     * @return the transaction template
     */
    @Bean(name = "txNested")
    public TransactionTemplate txNested(PlatformTransactionManager platformTransactionManager) {
        TransactionTemplate tx = new TransactionTemplate(platformTransactionManager);
        tx.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_NESTED);
        return tx;
    }

    /**
     * Tx requires new transaction template.
     *
     * @param platformTransactionManager the platform transaction manager
     * @return the transaction template
     */
    @Bean(name = "txRequiresNew")
    public TransactionTemplate txRequiresNew(PlatformTransactionManager platformTransactionManager) {
        TransactionTemplate tx = new TransactionTemplate(platformTransactionManager);
        tx.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return tx;
    }

    /**
     * Package thread pool executor service.
     *
     * @return the executor service
     */
    @Bean(name = "packageThreadPool")
    public ExecutorService packageThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("package-pool-%d").build();
        ExecutorService packageExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        return new TraceableExecutorService(beanFactory, packageExecutor);
    }

    /**
     * Http message converters http message converters.
     *
     * @return the http message converters
     */
    @Bean
    public HttpMessageConverters HttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue, SerializerFeature.SortField, SerializerFeature.MapSortField);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        return new HttpMessageConverters(fastConverter);
    }

    /**
     * Persisted result map hash blocking map.
     *
     * @return the hash blocking map
     */
    @Bean
    public HashBlockingMap persistedResultMap() {
        return new HashBlockingMap<>(Constant.MAX_BLOCKING_QUEUE_SIZE);
    }

    /**
     * Cluster persisted result map hash blocking map.
     *
     * @return the hash blocking map
     */
    @Bean
    public HashBlockingMap clusterPersistedResultMap() {
        return new HashBlockingMap<>(Constant.MAX_BLOCKING_QUEUE_SIZE);
    }

    /**
     * Sync voting executor pool thread pool task executor.
     *
     * @return the thread pool task executor
     */
    @Bean(name = "syncVotingExecutorPool")
    public ThreadPoolTaskExecutor syncVotingExecutorPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(5000);
        threadPoolTaskExecutor.setThreadNamePrefix("syncVotingExecutor-");
        threadPoolTaskExecutor.initialize();
        return new LazyTraceThreadPoolTaskExecutor(beanFactory, threadPoolTaskExecutor);
    }

    /**
     * Async voting executor pool thread pool task executor.
     *
     * @return the thread pool task executor
     */
    @Bean(name = "asyncVotingExecutorPool")
    public ThreadPoolTaskExecutor asyncVotingExecutorPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(5000);
        threadPoolTaskExecutor.setThreadNamePrefix("asyncVotingExecutorPool-");
        threadPoolTaskExecutor.initialize();
        return new LazyTraceThreadPoolTaskExecutor(beanFactory, threadPoolTaskExecutor);
    }

    /**
     * Tx consumer executor executor.
     *
     * @return the executor
     */
    @Bean(name = "txConsumerExecutor")
    public Executor txConsumerExecutor() {
        NamedDaemonThreadFactory namedDaemonThreadFactory = new NamedDaemonThreadFactory("txConsumerExecutor-");
        ExecutorService service = Executors.newSingleThreadExecutor(namedDaemonThreadFactory);
        return new LazyTraceExecutor(beanFactory, service);
    }

    /**
     * P 2 p send executor thread pool task executor.
     *
     * @return the thread pool task executor
     */
    @Bean (name = "p2pSendExecutor")  public ThreadPoolTaskExecutor p2pSendExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(50);
        threadPoolTaskExecutor.setQueueCapacity(5000);
        threadPoolTaskExecutor.setKeepAliveSeconds(3600);
        threadPoolTaskExecutor.setThreadNamePrefix("p2pSendExecutor-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPoolTaskExecutor.initialize();
        return new LazyTraceThreadPoolTaskExecutor(beanFactory, threadPoolTaskExecutor);
    }

    /**
     * P 2 p receive executor thread pool task executor.
     *
     * @return the thread pool task executor
     */
    @Bean (name = "p2pReceiveExecutor")  public ThreadPoolTaskExecutor p2pReceiveExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(1024);
        threadPoolTaskExecutor.setKeepAliveSeconds(3600);
        threadPoolTaskExecutor.setThreadNamePrefix("p2pReceivedExecutor-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPoolTaskExecutor.initialize();
        return new LazyTraceThreadPoolTaskExecutor(beanFactory, threadPoolTaskExecutor);
    }

    /**
     * redisson single server bean
     *
     * @return redisson client
     */
    @Bean
    @ConditionalOnProperty(name = "trust.redisson.single", havingValue = "true", matchIfMissing = true)
    RedissonClient redissonSingle() {
        Config config = new Config();

        //check address
        if (StringUtils.isBlank(redissonAddress)) {
            log.error("Redisson address  is blank error !");
            throw new NullPointerException("Redisson address is null exception !");
        }

        //config single server
        SingleServerConfig serverConfig = config.useSingleServer().setAddress(redissonAddress);
        if (StringUtils.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }

        return Redisson.create(config);
    }

    /**
     * redisson sentinel server bean
     *
     * @return redisson client
     */
    @Bean
    @ConditionalOnProperty(name = "trust.redisson.single", havingValue = "false")
    RedissonClient redissonSentinel() {
        Config config = new Config();
        //check address
        if (StringUtils.isBlank(redissonAddress)) {
            log.error("Redisson address  is blank error !");
            throw new NullPointerException("Redisson address is null exception !");
        }
        //check masterName
        if (StringUtils.isBlank(masterName)) {
            log.error("Redisson masterName  is blank error !");
            throw new NullPointerException("Redisson masterName is null exception !");
        }
        String[] allAddress = redissonAddress.split(",");

        //config Sentinel server
        SentinelServersConfig serverConfig = config.useSentinelServers().setMasterName(masterName).addSentinelAddress(allAddress);
        if (StringUtils.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }

        return Redisson.create(config);
    }

    /**
     * Default key value db source db source.
     *
     * @return the db source
     */
    @Bean
    public DbSource<byte[]> defaultKeyValueDbSource() {
        RocksDbDataSource rocksDbDataSource = new RocksDbDataSource("StateDB", defaultSystemProperties()) {
            @Override
            public void delete(byte[] key) {
            }
        };
        rocksDbDataSource.init();
        return rocksDbDataSource;
    }

    /**
     * Default repository repository.
     *
     * @return the repository
     */
    @Bean
    public Repository defaultRepository() {
        DbSource<byte[]> db = defaultKeyValueDbSource();
        return new RepositoryRoot(db, null);
    }

    /**
     * Default system properties system properties.
     *
     * @return the system properties
     */
    @Bean
    public SystemProperties defaultSystemProperties() {
        System.setProperty("database.dir", dbFileRoot);
        SystemProperties properties = new SystemProperties();
        return properties;
    }

}
