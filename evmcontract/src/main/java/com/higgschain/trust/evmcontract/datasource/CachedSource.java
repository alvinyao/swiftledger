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

import java.util.Collection;

/**
 * Source which internally caches underlying Source key-value pairs
 * <p>
 * Created by Anton Nashatyrev on 21.10.2016.
 *
 * @param <Key>   the type parameter
 * @param <Value> the type parameter
 */
public interface CachedSource<Key, Value> extends Source<Key, Value> {

    /**
     * Gets source.
     *
     * @return The underlying Source
     */
    Source<Key, Value> getSource();

    /**
     * Gets modified.
     *
     * @return Modified entry keys if this is a write cache
     */
    Collection<Key> getModified();

    /**
     * Has modified boolean.
     *
     * @return indicates the cache has modified entries
     */
    boolean hasModified();

    /**
     * Estimates the size of cached entries in bytes.
     * This value shouldn't be precise size of Java objects
     *
     * @return cache size in bytes
     */
    long estimateCacheSize();

    /**
     * Just a convenient shortcut to the most popular Sources with byte[] key
     *
     * @param <Value> the type parameter
     */
    interface BytesKey<Value> extends CachedSource<byte[], Value> {
    }
}
