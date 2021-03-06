/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.atomix.core.primitive;

import com.higgschain.trust.consensus.core.AbstractCommitReplicateComposite;
import com.higgschain.trust.consensus.core.IConsensusSnapshot;
import com.higgschain.trust.consensus.core.command.AbstractConsensusCommand;
import io.atomix.primitive.service.AbstractPrimitiveService;
import io.atomix.primitive.service.BackupInput;
import io.atomix.primitive.service.BackupOutput;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * The type Command primitive service.
 *
 * @author suimi
 * @date 2018 /7/6
 */
@Slf4j public class CommandPrimitiveService extends AbstractPrimitiveService implements ICommandPrimitiveService {

    private AbstractCommitReplicateComposite replicateComposite;

    private IConsensusSnapshot snapshot;

    /**
     * Instantiates a new Command primitive service.
     *
     * @param type               the type
     * @param replicateComposite the replicate composite
     * @param snapshot           the snapshot
     */
    public CommandPrimitiveService(CommandPrimitiveType type, AbstractCommitReplicateComposite replicateComposite,
        IConsensusSnapshot snapshot) {
        super(type, ICommandPrimitive.class);
        this.replicateComposite = replicateComposite;
        this.snapshot = snapshot;
    }

    @Override public void submit(AbstractConsensusCommand command) {
        Function function = replicateComposite.registerCommit().get(command.getClass());
        function.apply(command);
    }

    @Override public void backup(BackupOutput output) {
        output.writeObject(snapshot.getSnapshot());
    }

    @Override public void restore(BackupInput input) {
        byte[] snapshotStr = input.readObject();
        snapshot.installSnapshot(snapshotStr);
    }
}
