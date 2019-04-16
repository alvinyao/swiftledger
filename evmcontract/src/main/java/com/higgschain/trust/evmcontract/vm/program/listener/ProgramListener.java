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
package com.higgschain.trust.evmcontract.vm.program.listener;

import com.higgschain.trust.evmcontract.vm.DataWord;

/**
 * The interface Program listener.
 */
public interface ProgramListener {

    /**
     * On memory extend.
     *
     * @param delta the delta
     */
    void onMemoryExtend(int delta);

    /**
     * On memory write.
     *
     * @param address the address
     * @param data    the data
     * @param size    the size
     */
    void onMemoryWrite(int address, byte[] data, int size);

    /**
     * On stack pop.
     */
    void onStackPop();

    /**
     * On stack push.
     *
     * @param value the value
     */
    void onStackPush(DataWord value);

    /**
     * On stack swap.
     *
     * @param from the from
     * @param to   the to
     */
    void onStackSwap(int from, int to);

    /**
     * On storage put.
     *
     * @param key   the key
     * @param value the value
     */
    void onStoragePut(DataWord key, DataWord value);

    /**
     * On storage clear.
     */
    void onStorageClear();
}