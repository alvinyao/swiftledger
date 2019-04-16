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

import com.higgschain.trust.evmcontract.config.SystemProperties;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.OpCode;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvoke;
import org.spongycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

import static com.higgschain.trust.evmcontract.util.ByteUtil.toHexString;
import static java.lang.String.format;

/**
 * The type Program trace.
 */
public class ProgramTrace {

    private List<Op> ops = new ArrayList<>();
    private String result;
    private String error;
    private String contractAddress;

    /**
     * Instantiates a new Program trace.
     */
    public ProgramTrace() {
        this(null, null);
    }

    /**
     * Instantiates a new Program trace.
     *
     * @param config        the config
     * @param programInvoke the program invoke
     */
    public ProgramTrace(SystemProperties config, ProgramInvoke programInvoke) {
        if (programInvoke != null && config.vmTrace()) {
            contractAddress = Hex.toHexString(programInvoke.getOwnerAddress().getLast20Bytes());
        }
    }

    /**
     * Gets ops.
     *
     * @return the ops
     */
    public List<Op> getOps() {
        return ops;
    }

    /**
     * Sets ops.
     *
     * @param ops the ops
     */
    public void setOps(List<Op> ops) {
        this.ops = ops;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets contract address.
     *
     * @return the contract address
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Sets contract address.
     *
     * @param contractAddress the contract address
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     * Result program trace.
     *
     * @param result the result
     * @return the program trace
     */
    public ProgramTrace result(byte[] result) {
        setResult(toHexString(result));
        return this;
    }

    /**
     * Error program trace.
     *
     * @param error the error
     * @return the program trace
     */
    public ProgramTrace error(Exception error) {
        setError(error == null ? "" : format("%s: %s", error.getClass(), error.getMessage()));
        return this;
    }

    /**
     * Add op op.
     *
     * @param code    the code
     * @param pc      the pc
     * @param deep    the deep
     * @param gas     the gas
     * @param actions the actions
     * @return the op
     */
    public Op addOp(byte code, int pc, int deep, DataWord gas, OpActions actions) {
        Op op = new Op();
        op.setActions(actions);
        op.setCode(OpCode.code(code));
        op.setDeep(deep);
        op.setGas(gas.value());
        op.setPc(pc);

        ops.add(op);

        return op;
    }

    /**
     * Used for merging sub calls execution.
     *
     * @param programTrace the program trace
     */
    public void merge(ProgramTrace programTrace) {
        this.ops.addAll(programTrace.ops);
    }

    /**
     * As json string string.
     *
     * @param formatted the formatted
     * @return the string
     */
    public String asJsonString(boolean formatted) {
        return Serializers.serializeFieldsOnly(this, formatted);
    }

    @Override
    public String toString() {
        return asJsonString(true);
    }
}
