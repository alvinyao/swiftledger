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
package com.higgschain.trust.evmcontract.vm;

/**
 * The type Call create.
 *
 * @author Roman Mandeleil
 * @since 03.07.2014
 */
public class CallCreate {

    /**
     * The Data.
     */
    final byte[] data;
    /**
     * The Destination.
     */
    final byte[] destination;
    /**
     * The Gas limit.
     */
    final byte[] gasLimit;
    /**
     * The Value.
     */
    final byte[] value;

    /**
     * Instantiates a new Call create.
     *
     * @param data        the data
     * @param destination the destination
     * @param gasLimit    the gas limit
     * @param value       the value
     */
    public CallCreate(byte[] data, byte[] destination, byte[] gasLimit, byte[] value) {
        this.data = data;
        this.destination = destination;
        this.gasLimit = gasLimit;
        this.value = value;
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Get destination byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getDestination() {
        return destination;
    }

    /**
     * Get gas limit byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getGasLimit() {
        return gasLimit;
    }

    /**
     * Get value byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getValue() {
        return value;
    }
}
