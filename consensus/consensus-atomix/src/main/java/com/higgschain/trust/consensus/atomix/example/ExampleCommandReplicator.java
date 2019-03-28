package com.higgschain.trust.consensus.atomix.example;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zhu_Yuanxiang
 * @create 2018-08-01
 */

@Slf4j
//@Component @Replicator
public class ExampleCommandReplicator {

    @Autowired ExampleSnapshot exampleSnapshot;

    public void artificialChangeMaster(ConsensusCommit<ExampleCommand> commit) {
        ExampleCommand operation = commit.operation();
        log.debug("ExampleCommandReplicator received ExampleCommand:{}", operation.getMsg());
        exampleSnapshot.updateIndex(operation.getIndex());
        commit.close();
    }
}
