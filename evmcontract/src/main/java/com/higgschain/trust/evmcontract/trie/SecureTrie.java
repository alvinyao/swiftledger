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
package com.higgschain.trust.evmcontract.trie;

import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.datasource.Source;
import com.higgschain.trust.evmcontract.util.ByteUtil;

/**
 * The type Secure trie.
 */
public class SecureTrie extends TrieImpl {

    /**
     * Instantiates a new Secure trie.
     *
     * @param root the root
     */
    public SecureTrie(byte[] root) {
        super(root);
    }

    /**
     * Instantiates a new Secure trie.
     *
     * @param cache the cache
     */
    public SecureTrie(Source<byte[], byte[]> cache) {
        super(cache, null);
    }

    /**
     * Instantiates a new Secure trie.
     *
     * @param cache the cache
     * @param root  the root
     */
    public SecureTrie(Source<byte[], byte[]> cache, byte[] root) {
        super(cache, root);
    }

    @Override
    public byte[] get(byte[] key) {
        return super.get(HashUtil.sha3(key));
    }

    @Override
    public void put(byte[] key, byte[] value) {
        super.put(HashUtil.sha3(key), value);
    }

    @Override
    public void delete(byte[] key) {
        put(key, ByteUtil.EMPTY_BYTE_ARRAY);
    }
}
