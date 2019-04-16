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
 * A wrapper for a message call from a contract to another account.
 * This can either be a normal CALL, CALLCODE, DELEGATECALL or POST call.
 */
public class MessageCall {

    /**
     * Type of internal call. Either CALL, CALLCODE or POST
     */
    private final OpCode type;

    /**
     * gas to pay for the call, remaining gas will be refunded to the caller
     */
    private final DataWord gas;
    /**
     * address of account which code to call
     */
    private final DataWord codeAddress;
    /**
     * the value that can be transfer along with the code execution
     */
    private final DataWord endowment;
    /**
     * start of memory to be input data to the call
     */
    private final DataWord inDataOffs;
    /**
     * size of memory to be input data to the call
     */
    private final DataWord inDataSize;
    /**
     * start of memory to be output of the call
     */
    private DataWord outDataOffs;
    /**
     * size of memory to be output data to the call
     */
    private DataWord outDataSize;

    /**
     * Instantiates a new Message call.
     *
     * @param type        the type
     * @param gas         the gas
     * @param codeAddress the code address
     * @param endowment   the endowment
     * @param inDataOffs  the in data offs
     * @param inDataSize  the in data size
     */
    public MessageCall(OpCode type, DataWord gas, DataWord codeAddress,
                       DataWord endowment, DataWord inDataOffs, DataWord inDataSize) {
        this.type = type;
        this.gas = gas;
        this.codeAddress = codeAddress;
        this.endowment = endowment;
        this.inDataOffs = inDataOffs;
        this.inDataSize = inDataSize;
    }

    /**
     * Instantiates a new Message call.
     *
     * @param type        the type
     * @param gas         the gas
     * @param codeAddress the code address
     * @param endowment   the endowment
     * @param inDataOffs  the in data offs
     * @param inDataSize  the in data size
     * @param outDataOffs the out data offs
     * @param outDataSize the out data size
     */
    public MessageCall(OpCode type, DataWord gas, DataWord codeAddress,
                       DataWord endowment, DataWord inDataOffs, DataWord inDataSize,
                       DataWord outDataOffs, DataWord outDataSize) {
        this(type, gas, codeAddress, endowment, inDataOffs, inDataSize);
        this.outDataOffs = outDataOffs;
        this.outDataSize = outDataSize;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public OpCode getType() {
        return type;
    }

    /**
     * Gets gas.
     *
     * @return the gas
     */
    public DataWord getGas() {
        return gas;
    }

    /**
     * Gets code address.
     *
     * @return the code address
     */
    public DataWord getCodeAddress() {
        return codeAddress;
    }

    /**
     * Gets endowment.
     *
     * @return the endowment
     */
    public DataWord getEndowment() {
        return endowment;
    }

    /**
     * Gets in data offs.
     *
     * @return the in data offs
     */
    public DataWord getInDataOffs() {
        return inDataOffs;
    }

    /**
     * Gets in data size.
     *
     * @return the in data size
     */
    public DataWord getInDataSize() {
        return inDataSize;
    }

    /**
     * Gets out data offs.
     *
     * @return the out data offs
     */
    public DataWord getOutDataOffs() {
        return outDataOffs;
    }

    /**
     * Gets out data size.
     *
     * @return the out data size
     */
    public DataWord getOutDataSize() {
        return outDataSize;
    }
}
