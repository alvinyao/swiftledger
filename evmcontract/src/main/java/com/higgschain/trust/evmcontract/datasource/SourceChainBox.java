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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chain of Sources as a single Source
 * All calls to this Source are delegated to the last Source in the chain
 * On flush all Sources in chain are flushed in reverse order
 * <p>
 * Created by Anton Nashatyrev on 07.12.2016.
 *
 * @param <Key>         the type parameter
 * @param <Value>       the type parameter
 * @param <SourceKey>   the type parameter
 * @param <SourceValue> the type parameter
 */
public class SourceChainBox<Key, Value, SourceKey, SourceValue>
        extends AbstractChainedSource<Key, Value, SourceKey, SourceValue> {

    /**
     * The Chain.
     */
    List<Source> chain = new ArrayList<>();
    /**
     * The Last source.
     */
    Source<Key, Value> lastSource;

    /**
     * Instantiates a new Source chain box.
     *
     * @param source the source
     */
    public SourceChainBox(Source<SourceKey, SourceValue> source) {
        super(source);
    }

    /**
     * Adds next Source in the chain to the collection
     * Sources should be added from most bottom (connected to the backing Source)
     * All calls to the SourceChainBox will be delegated to the last added
     * Source
     *
     * @param src the src
     */
    public void add(Source src) {
        chain.add(src);
        lastSource = src;
    }

    @Override
    public void put(Key key, Value val) {
        lastSource.put(key, val);
    }

    @Override
    public Value get(Key key) {
        return lastSource.get(key);
    }

    @Override
    public void delete(Key key) {
        lastSource.delete(key);
    }

//    @Override
//    public boolean flush() {
////        boolean ret = false;
////        for (int i = chain.size() - 1; i >= 0 ; i--) {
////            ret |= chain.get(i).flush();
////        }
//        return lastSource.flush();
//    }

    @Override
    protected boolean flushImpl() {
        return lastSource.flush();
    }
}
