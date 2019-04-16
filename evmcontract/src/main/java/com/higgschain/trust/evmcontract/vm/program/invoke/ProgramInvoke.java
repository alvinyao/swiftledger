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
package com.higgschain.trust.evmcontract.vm.program.invoke;

import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.db.BlockStore;
import com.higgschain.trust.evmcontract.vm.DataWord;

/**
 * The interface Program invoke.
 *
 * @author Roman Mandeleil
 * @since 03.06.2014
 */
public interface ProgramInvoke {

    /**
     * Gets owner address.
     *
     * @return the owner address
     */
    DataWord getOwnerAddress();

    /**
     * Gets balance.
     *
     * @return the balance
     */
    DataWord getBalance();

    /**
     * Gets origin address.
     *
     * @return the origin address
     */
    DataWord getOriginAddress();

    /**
     * Gets caller address.
     *
     * @return the caller address
     */
    DataWord getCallerAddress();

    /**
     * Gets min gas price.
     *
     * @return the min gas price
     */
    DataWord getMinGasPrice();

    /**
     * Gets gas.
     *
     * @return the gas
     */
    DataWord getGas();

    /**
     * Gets gas long.
     *
     * @return the gas long
     */
    long getGasLong();

    /**
     * Gets call value.
     *
     * @return the call value
     */
    DataWord getCallValue();

    /**
     * Gets data size.
     *
     * @return the data size
     */
    DataWord getDataSize();

    /**
     * Gets data value.
     *
     * @param indexData the index data
     * @return the data value
     */
    DataWord getDataValue(DataWord indexData);

    /**
     * Get data copy byte [ ].
     *
     * @param offsetData the offset data
     * @param lengthData the length data
     * @return the byte [ ]
     */
    byte[] getDataCopy(DataWord offsetData, DataWord lengthData);

    /**
     * Gets prev hash.
     *
     * @return the prev hash
     */
    DataWord getPrevHash();

    /**
     * Gets coinbase.
     *
     * @return the coinbase
     */
    DataWord getCoinbase();

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    DataWord getTimestamp();

    /**
     * Gets number.
     *
     * @return the number
     */
    DataWord getNumber();

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    DataWord getDifficulty();

    /**
     * Gets gaslimit.
     *
     * @return the gaslimit
     */
    DataWord getGaslimit();

    /**
     * By transaction boolean.
     *
     * @return the boolean
     */
    boolean byTransaction();

    /**
     * By testing suite boolean.
     *
     * @return the boolean
     */
    boolean byTestingSuite();

    /**
     * Gets call deep.
     *
     * @return the call deep
     */
    int getCallDeep();

    /**
     * Gets repository.
     *
     * @return the repository
     */
    Repository getRepository();

    /**
     * Gets block store.
     *
     * @return the block store
     */
    BlockStore getBlockStore();

    /**
     * Is static call boolean.
     *
     * @return the boolean
     */
    boolean isStaticCall();
}
