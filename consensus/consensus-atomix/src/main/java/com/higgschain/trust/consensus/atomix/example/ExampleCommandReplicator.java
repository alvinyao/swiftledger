package com.higgschain.trust.consensus.atomix.example;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Example command replicator.
 *
 * @author Zhu_Yuanxiang
 * @create 2018 -08-01
 */
@Slf4j
//@Component @Replicator
public class ExampleCommandReplicator {

    /**
     * The Example snapshot.
     */
    @Autowired ExampleSnapshot exampleSnapshot;

    /**
     * Artificial change master.
     *
     * @param commit the commit
     */
    public void artificialChangeMaster(ConsensusCommit<ExampleCommand> commit) {
        ExampleCommand operation = commit.operation();
        log.debug("ExampleCommandReplicator received ExampleCommand:{}", operation.getMsg());
        exampleSnapshot.updateIndex(operation.getIndex());
        commit.close();
    }
}
