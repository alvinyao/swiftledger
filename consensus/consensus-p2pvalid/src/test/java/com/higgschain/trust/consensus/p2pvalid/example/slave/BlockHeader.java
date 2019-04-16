package com.higgschain.trust.consensus.p2pvalid.example.slave;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Block header.
 *
 * @Description: block p2p information
 * @author: pengdi
 */
@Getter
@Setter
public class BlockHeader extends BaseBO {
    private String version;

    private String previousHash;

    private String blockHash;

    @NotNull private Long height;

    private Long blockTime;
}
