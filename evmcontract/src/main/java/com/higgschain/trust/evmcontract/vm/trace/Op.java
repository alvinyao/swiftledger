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
package com.higgschain.trust.evmcontract.vm.trace;

import com.higgschain.trust.evmcontract.vm.OpCode;

import java.math.BigInteger;

/**
 * The type Op.
 */
public class Op {

    private OpCode code;
    private int deep;
    private int pc;
    private BigInteger gas;
    private OpActions actions;

    /**
     * Gets code.
     *
     * @return the code
     */
    public OpCode getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(OpCode code) {
        this.code = code;
    }

    /**
     * Gets deep.
     *
     * @return the deep
     */
    public int getDeep() {
        return deep;
    }

    /**
     * Sets deep.
     *
     * @param deep the deep
     */
    public void setDeep(int deep) {
        this.deep = deep;
    }

    /**
     * Gets pc.
     *
     * @return the pc
     */
    public int getPc() {
        return pc;
    }

    /**
     * Sets pc.
     *
     * @param pc the pc
     */
    public void setPc(int pc) {
        this.pc = pc;
    }

    /**
     * Gets gas.
     *
     * @return the gas
     */
    public BigInteger getGas() {
        return gas;
    }

    /**
     * Sets gas.
     *
     * @param gas the gas
     */
    public void setGas(BigInteger gas) {
        this.gas = gas;
    }

    /**
     * Gets actions.
     *
     * @return the actions
     */
    public OpActions getActions() {
        return actions;
    }

    /**
     * Sets actions.
     *
     * @param actions the actions
     */
    public void setActions(OpActions actions) {
        this.actions = actions;
    }
}
