package com.higgschain.trust.slave.core.service.consensus.log;

import com.higgschain.trust.common.utils.TraceUtils;
import com.higgschain.trust.consensus.annotation.Replicator;
import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.common.util.beanvalidator.BeanValidateResult;
import com.higgschain.trust.slave.common.util.beanvalidator.BeanValidator;
import com.higgschain.trust.slave.core.service.pack.PackageProcess;
import com.higgschain.trust.slave.core.service.pack.PackageService;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.zip.DataFormatException;

/**
 * The type Package commit replicate.
 */
@Replicator @Slf4j @Component public class PackageCommitReplicate implements ApplicationContextAware, InitializingBean {

    /**
     * The Package service.
     */
    @Autowired
    PackageService packageService;

    /**
     * The Package thread pool.
     */
    @Autowired ExecutorService packageThreadPool;

    /**
     * The Package process.
     */
    @Autowired PackageProcess packageProcess;

    private ApplicationContext applicationContext;

    private List<PackageListener> listeners = new ArrayList<>();

    /**
     * package has been replicated by raft/copycat-smart/pbft/etc
     *
     * @param commit the commit
     * @return
     */
    public void packageReplicated(ConsensusCommit<PackageCommand> commit) {
        // validate param
        if (null == commit) {
            log.error(
                "[LogReplicateHandler.packageReplicated]param validate failed, cause package command commit is null ");
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        Package pack = null;
        try {
            pack = commit.operation().getValueFromByte(Package.class);
        } catch (DataFormatException e) {
            log.error("[LogReplicateHandler.packageReplicated]param validate failed, decompress package error:{}",e.getCause());
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        // validate param
        if (pack == null) {
            log.error("[LogReplicateHandler.packageReplicated]param validate failed, cause package is null ");
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        Long height = pack.getHeight();
        log.debug("package reached consensus, height: {}", height);

        Span span = TraceUtils.createSpan();
        try {
            BeanValidateResult result = BeanValidator.validate(pack);
            if (!result.isSuccess()) {
                log.error(
                    "[LogReplicateHandler.packageReplicated]param validate failed, cause: " + result.getFirstMsg());
                throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
            }

            // receive package
            try {
                packageService.receive(pack);
                Package finalPack = pack;
                listeners.forEach(listener -> listener.received(finalPack));
            } catch (SlaveException e) {
                //idempotent as success, other exceptions make the consensus layer retry
                if (e.getCode() != SlaveErrorEnum.SLAVE_IDEMPOTENT) {
                    throw e;
                }
            }
        } finally {
            TraceUtils.closeSpan(span);
        }
        commit.close();
    }

    @Override public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override public void afterPropertiesSet() {
        Map<String, PackageListener> beansOfType = applicationContext.getBeansOfType(PackageListener.class);
        listeners.addAll(beansOfType.values());
    }
}
