package com.higgschain.trust.slave.model.bo.consensus;

import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Persist command.
 *
 * @Description:persist p2p command for consensus
 * @author: pengdi
 */
@Getter @Setter @NoArgsConstructor public class PersistCommand extends ValidCommand<BlockHeader> {
    private static final long serialVersionUID = -1L;

    /**
     * Instantiates a new Persist command.
     *
     * @param seqNum the seq num
     * @param header the header
     * @param view   the view
     */
    public PersistCommand(Long seqNum, BlockHeader header, long view) {
        super(header, view);
    }

    @Override public String messageDigest() {
        return get().toString();
    }
}
