package com.higgschain.trust.consensus.sofajraft.rpc;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsensusCommandClosure implements Closure {
    private AbstractConsensusCommand request;
    private ConsensusCommandResponse response;
    private Closure done;

    public ConsensusCommandClosure(AbstractConsensusCommand request, ConsensusCommandResponse response,
                                  Closure done) {
        super();
        this.request = request;
        this.response = response;
        this.done = done;
    }

    @Override
    public void run(Status status) {
        if (this.done != null) {
            done.run(status);
        }
    }
}
