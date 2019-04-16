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
package com.higgschain.trust.evmcontract.datasource;

import com.higgschain.trust.evmcontract.crypto.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Special optimization when the majority of get requests to the slower underlying source
 * are targeted to missing entries. The BloomFilter handles most of these requests.
 * <p>
 * Created by Anton Nashatyrev on 16.01.2017.
 */
public class BloomedSource extends AbstractChainedSource<byte[], byte[], byte[], byte[]> {
    private final static Logger logger = LoggerFactory.getLogger("com/higgsblock/db");

    private byte[] filterKey = HashUtil.sha3("filterKey".getBytes());

    /**
     * The Filter.
     */
    QuotientFilter filter;
    /**
     * The Hits.
     */
    int hits = 0;
    /**
     * The Misses.
     */
    int misses = 0;
    /**
     * The False misses.
     */
    int falseMisses = 0;
    /**
     * The Dirty.
     */
    boolean dirty = false;
    /**
     * The Max bloom size.
     */
    int maxBloomSize;

    /**
     * Instantiates a new Bloomed source.
     *
     * @param source       the source
     * @param maxBloomSize the max bloom size
     */
    public BloomedSource(Source<byte[], byte[]> source, int maxBloomSize) {
        super(source);
        this.maxBloomSize = maxBloomSize;
        byte[] filterBytes = source.get(filterKey);
        if (filterBytes != null) {
            if (filterBytes.length > 0) {
                filter = QuotientFilter.deserialize(filterBytes);
            } else {
                // filter size exceeded limit and is disabled forever
                filter = null;
            }
        } else {
            if (maxBloomSize > 0) {
                filter = QuotientFilter.create(50_000_000, 100_000);
            } else {
                // we can't re-enable filter later
                getSource().put(filterKey, new byte[0]);
            }
        }

    }

    /**
     * Start blooming.
     *
     * @param filter the filter
     */
    public void startBlooming(QuotientFilter filter) {
        this.filter = filter;
    }

    /**
     * Stop blooming.
     */
    public void stopBlooming() {
        filter = null;
    }

    @Override
    public void put(byte[] key, byte[] val) {
        if (filter != null) {
            filter.insert(key);
            dirty = true;
            if (filter.getAllocatedBytes() > maxBloomSize) {
                logger.info("Bloom filter became too large (" + filter.getAllocatedBytes() + " exceeds max threshold " + maxBloomSize + ") and is now disabled forever.");
                getSource().put(filterKey, new byte[0]);
                filter = null;
                dirty = false;
            }
        }
        getSource().put(key, val);
    }

    @Override
    public byte[] get(byte[] key) {
        if (filter == null) {
            return getSource().get(key);
        }

        if (!filter.maybeContains(key)) {
            hits++;
            return null;
        } else {
            byte[] ret = getSource().get(key);
            if (ret == null) {
                falseMisses++;
            } else {
                misses++;
            }
            return ret;
        }
    }

    @Override
    public void delete(byte[] key) {
        if (filter != null) {
            filter.remove(key);
        }
        getSource().delete(key);
    }

    @Override
    protected boolean flushImpl() {
        if (filter != null && dirty) {
            getSource().put(filterKey, filter.serialize());
            dirty = false;
            return true;
        } else {
            return false;
        }
    }
}
