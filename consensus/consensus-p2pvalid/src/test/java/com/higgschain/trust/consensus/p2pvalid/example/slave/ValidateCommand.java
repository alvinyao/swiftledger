package com.higgschain.trust.consensus.p2pvalid.example.slave;

import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Validate command.
 *
 * @Description:validate p2p command for consensus
 * @author: pengdi
 */
@Getter
@Setter
@NoArgsConstructor
public class ValidateCommand extends ValidCommand<BlockHeader> {
    private static final long serialVersionUID = -1L;

    /**
     * Instantiates a new Validate command.
     *
     * @param seqNum the seq num
     * @param header the header
     * @param view   the view
     */
    public ValidateCommand(Long seqNum, BlockHeader header, long view) {
        super(header, view);
    }

    @Override
    public String messageDigest() {
        return get().toString();
    }
}
