package com.higgschain.trust.consensus.sofajraft.rpc;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.entity.Task;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import com.higgschain.trust.consensus.sofajraft.config.SofajraftServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Slf4j
@Component
public class ConsensusCommandProcessor extends AsyncUserProcessor<AbstractConsensusCommand> {

    private SofajraftServer sofajraftServer;

    private String interest;

    public ConsensusCommandProcessor(SofajraftServer sofajraftServer) {
        this.sofajraftServer = sofajraftServer;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, AbstractConsensusCommand request) {
        final ConsensusCommandResponse response = new ConsensusCommandResponse();

        if (!this.sofajraftServer.getRaftGroupService().getRaftNode().isLeader()) {
            response.setSuccess(false);
            response.setErrorMsg("this node is not leader");
            asyncCtx.sendResponse(response);
            return;
        }

        final ConsensusCommandClosure closure = new ConsensusCommandClosure(request, response,
                status -> {
                    if (!status.isOk()) {
                        response.setErrorMsg(status.getErrorMsg());
                        response.setSuccess(false);
                    }
                    asyncCtx.sendResponse(response);
                });

        try {
            final Task task = new Task();
            task.setDone(closure);
            task.setData(ByteBuffer
                    .wrap(SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(request)));

            // apply task to raft group.
            sofajraftServer.getRaftGroupService().getRaftNode().apply(task);
        } catch (final CodecException e) {
            log.error("Fail to encode AbstractConsensusCommand", e);
            response.setSuccess(false);
            response.setErrorMsg(e.getMessage());
            asyncCtx.sendResponse(response);
        }
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    @Override
    public String interest() {
        return interest;
    }
}
