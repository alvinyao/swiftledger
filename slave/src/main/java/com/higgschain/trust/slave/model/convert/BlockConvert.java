package com.higgschain.trust.slave.model.convert;

import com.higgschain.trust.slave.dao.po.block.BlockPO;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.StateRootHash;

/**
 * The type Block convert.
 *
 * @Description:
 * @author: pengdi
 */
public class BlockConvert {

    /**
     * Convert block po to block header block header.
     *
     * @param blockPO the block po
     * @return the block header
     */
    public static BlockHeader convertBlockPOToBlockHeader(BlockPO blockPO) {
        BlockHeader header = new BlockHeader();
        header.setBlockTime(blockPO.getBlockTime().getTime());
        header.setHeight(blockPO.getHeight());
        header.setBlockHash(blockPO.getBlockHash());
        header.setPreviousHash(blockPO.getPreviousHash());
        header.setVersion(blockPO.getVersion());
        header.setTotalTxNum(blockPO.getTotalTxNum());

        StateRootHash stateRootHash = new StateRootHash();
        stateRootHash.setTxRootHash(blockPO.getTxRootHash());
        stateRootHash.setTxReceiptRootHash(blockPO.getTxReceiptRootHash());
        stateRootHash.setAccountRootHash(blockPO.getAccountRootHash());
        stateRootHash.setContractRootHash(blockPO.getContractRootHash());
        stateRootHash.setPolicyRootHash(blockPO.getPolicyRootHash());
        stateRootHash.setRsRootHash(blockPO.getRsRootHash());
        stateRootHash.setCaRootHash(blockPO.getCaRootHash());
        stateRootHash.setStateRoot(blockPO.getStateRootHash());
        header.setStateRootHash(stateRootHash);
        return header;
    }

}
