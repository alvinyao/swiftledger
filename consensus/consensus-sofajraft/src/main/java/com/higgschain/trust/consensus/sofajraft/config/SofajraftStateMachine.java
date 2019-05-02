package com.higgschain.trust.consensus.sofajraft.config;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.alipay.sofa.jraft.util.Utils;
import com.higgschain.trust.consensus.command.ConsensusCommand;
import com.higgschain.trust.consensus.core.replicate.AbstractCommitReplicateComposite;
import com.higgschain.trust.consensus.snapshot.IConsensusSnapshot;
import com.higgschain.trust.consensus.sofajraft.rpc.ConsensusCommandClosure;
import com.higgschain.trust.consensus.sofajraft.snapshot.SofajraftSnapshotFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Slf4j
@Component
public class SofajraftStateMachine extends StateMachineAdapter {

    /**
     * Leader term
     */
    private final AtomicLong leaderTerm = new AtomicLong(-1);

    @Autowired
    private AbstractCommitReplicateComposite replicateComposite;

    @Autowired
    private IConsensusSnapshot exampleSnapshot;

    public boolean isLeader() {
        return this.leaderTerm.get() > 0;
    }

    @Override
    public void onApply(Iterator iter) {
        while (iter.hasNext()) {
            ConsensusCommandClosure closure = null;
            ConsensusCommand command = null;
            if (iter.done() != null) {
                // This task is applied by this node, get value from closure to avoid additional parsing.
                closure = (ConsensusCommandClosure) iter.done();
                command = closure.getRequest();
            } else {
                // Have to parse FetchAddRequest from this user log.
                final ByteBuffer data = iter.getData();
                try {
                    command = SerializerManager.getSerializer(SerializerManager.Hessian2)
                            .deserialize(data.array(), ConsensusCommand.class.getName());
                } catch (final CodecException e) {
                    log.error("Fail to decode AbstractConsensusCommand", e);
                }
            }

            Function function = replicateComposite.registerCommit().get(command.getClass());
            function.apply(command);

            if (closure != null) {
                closure.getResponse().setSuccess(true);
                closure.run(Status.OK());
            }
            iter.next();
        }
    }

    @Override
    public void onSnapshotSave(SnapshotWriter writer, Closure done) {
        Utils.runInThread(() -> {
            final SofajraftSnapshotFile snapshotFile = new SofajraftSnapshotFile(writer.getPath() + File.separator + "data");
            if (snapshotFile.save(exampleSnapshot.getSnapshot())) {
                if (writer.addFile("data")) {
                    done.run(Status.OK());
                } else {
                    done.run(new Status(RaftError.EIO, "Fail to add file to writer"));
                }
            } else {
                done.run(new Status(RaftError.EIO, "Fail to save counter exampleSnapshot %s", snapshotFile.getPath()));
            }
        });
    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        if (isLeader()) {
            log.warn("Leader is not supposed to load exampleSnapshot");
            return false;
        }
        if (reader.getFileMeta("data") == null) {
            log.error("Fail to find data file in {}", reader.getPath());
            return false;
        }
        final SofajraftSnapshotFile snapshotFile = new SofajraftSnapshotFile(reader.getPath() + File.separator + "data");
        try {
            exampleSnapshot.installSnapshot(snapshotFile.load());
            return true;
        } catch (final IOException e) {
            log.error("Fail to load exampleSnapshot from {}", snapshotFile.getPath());
            return false;
        }
    }

    @Override
    public void onLeaderStart(final long term) {
        this.leaderTerm.set(term);
        super.onLeaderStart(term);

    }

    @Override
    public void onLeaderStop(final Status status) {
        this.leaderTerm.set(-1);
        super.onLeaderStop(status);
    }
}
