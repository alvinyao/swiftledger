/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.higgschain.trust.evmcontract.db;


import com.higgschain.trust.evmcontract.core.Block;

import java.math.BigInteger;
import java.util.List;

/**
 * The interface Block store.
 *
 * @author Roman Mandeleil
 * @since 08.01.2015
 */
public interface BlockStore {

    /**
     * Get block hash by number byte [ ].
     *
     * @param blockNumber the block number
     * @return the byte [ ]
     */
    byte[] getBlockHashByNumber(long blockNumber);

    /**
     * Gets the block hash by its index.
     * When more than one block with the specified index exists (forks)
     * the select the block which is ancestor of the branchBlockHash
     *
     * @param blockNumber     the block number
     * @param branchBlockHash the branch block hash
     * @return the byte [ ]
     */
    byte[] getBlockHashByNumber(long blockNumber, byte[] branchBlockHash);

    /**
     * Gets chain block by number.
     *
     * @param blockNumber the block number
     * @return the chain block by number
     */
    Block getChainBlockByNumber(long blockNumber);

    /**
     * Gets block by hash.
     *
     * @param hash the hash
     * @return the block by hash
     */
    Block getBlockByHash(byte[] hash);

    /**
     * Is block exist boolean.
     *
     * @param hash the hash
     * @return the boolean
     */
    boolean isBlockExist(byte[] hash);

    /**
     * Gets list hashes end with.
     *
     * @param hash the hash
     * @param qty  the qty
     * @return the list hashes end with
     */
    List<byte[]> getListHashesEndWith(byte[] hash, long qty);

    /**
     * Gets list blocks end with.
     *
     * @param hash the hash
     * @param qty  the qty
     * @return the list blocks end with
     */
    List<Block> getListBlocksEndWith(byte[] hash, long qty);

    /**
     * Save block.
     *
     * @param block           the block
     * @param totalDifficulty the total difficulty
     * @param mainChain       the main chain
     */
    void saveBlock(Block block, BigInteger totalDifficulty, boolean mainChain);

    /**
     * Gets total difficulty for hash.
     *
     * @param hash the hash
     * @return the total difficulty for hash
     */
    BigInteger getTotalDifficultyForHash(byte[] hash);

    /**
     * Gets total difficulty.
     *
     * @return the total difficulty
     */
    BigInteger getTotalDifficulty();

    /**
     * Gets best block.
     *
     * @return the best block
     */
    Block getBestBlock();

    /**
     * Gets max number.
     *
     * @return the max number
     */
    long getMaxNumber();

    /**
     * Flush.
     */
    void flush();

    /**
     * Re branch.
     *
     * @param forkBlock the fork block
     */
    void reBranch(Block forkBlock);

    /**
     * Load.
     */
    void load();

    /**
     * Close.
     */
    void close();
}
