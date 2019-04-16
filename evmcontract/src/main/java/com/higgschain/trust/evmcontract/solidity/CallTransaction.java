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
package com.higgschain.trust.evmcontract.solidity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.util.FastByteComparisons;
import com.higgschain.trust.evmcontract.vm.LogInfo;
import com.higgschain.trust.evmcontract.crypto.HashUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.higgschain.trust.evmcontract.crypto.HashUtil.sha3;
import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.subarray;
import static org.apache.commons.lang3.StringUtils.stripEnd;

/**
 * Creates a contract function call transaction.
 * Serializes arguments according to the function ABI .
 * <p>
 * Created by Anton Nashatyrev on 25.08.2015.
 */
public class CallTransaction {

    private final static ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

//    public static Transaction createRawTransaction(long nonce, long gasPrice, long gasLimit, String toAddress,
//                                                    long value, byte[] data) {
//        Transaction tx = new Transaction(longToBytesNoLeadZeroes(nonce),
//                longToBytesNoLeadZeroes(gasPrice),
//                longToBytesNoLeadZeroes(gasLimit),
//                toAddress == null ? null : Hex.decode(toAddress),
//                longToBytesNoLeadZeroes(value),
//                data,
//                null);
//        return tx;
//    }



//    public static Transaction createCallTransaction(long nonce, long gasPrice, long gasLimit, String toAddress,
//                        long value, Function callFunc, Object ... funcArgs) {
//
//        byte[] callData = callFunc.encode(funcArgs);
//        return createRawTransaction(nonce, gasPrice, gasLimit, toAddress, value, callData);
//    }

    /**
     * The type Param.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Param {
        /**
         * The Indexed.
         */
        public Boolean indexed;
        /**
         * The Name.
         */
        public String name;
        /**
         * The Type.
         */
        public SolidityType type;

        /**
         * Gets type.
         *
         * @return the type
         */
        @JsonGetter("type")
        public String getType() {
            return type.getName();
        }
    }

    /**
     * The enum State mutability type.
     */
    public enum StateMutabilityType {/**
     * Pure state mutability type.
     */
    pure,
        /**
         * View state mutability type.
         */
        view,
        /**
         * Nonpayable state mutability type.
         */
        nonpayable,
        /**
         * Payable state mutability type.
         */
        payable
    }

    /**
     * The enum Function type.
     */
    public enum FunctionType {/**
     * Constructor function type.
     */
    constructor,
        /**
         * Function function type.
         */
        function,
        /**
         * Event function type.
         */
        event,
        /**
         * Fallback function type.
         */
        fallback
    }

    /**
     * The type Function.
     */
    public static class Function {
        /**
         * The Anonymous.
         */
        public boolean anonymous;
        /**
         * The Constant.
         */
        public boolean constant;
        /**
         * The Payable.
         */
        public boolean payable;
        /**
         * The Name.
         */
        public String name = "";
        /**
         * The Inputs.
         */
        public Param[] inputs = new Param[0];
        /**
         * The Outputs.
         */
        public Param[] outputs = new Param[0];
        /**
         * The Type.
         */
        public FunctionType type;
        /**
         * The State mutability.
         */
        public StateMutabilityType stateMutability;

        private Function() {}

        /**
         * Encode byte [ ].
         *
         * @param args the args
         * @return the byte [ ]
         */
        public byte[] encode(Object ... args) {
            return ByteUtil.merge(encodeSignature(), encodeArguments(args));
        }

        /**
         * Encode arguments byte [ ].
         *
         * @param args the args
         * @return the byte [ ]
         */
        public byte[] encodeArguments(Object ... args) {
            if (args.length > inputs.length) {
                throw new RuntimeException("Too many arguments: " + args.length + " > " + inputs.length);
            }

            int staticSize = 0;
            int dynamicCnt = 0;
            // calculating static size and number of dynamic params
            for (int i = 0; i < args.length; i++) {
                Param param = inputs[i];
                if (param.type.isDynamicType()) {
                    dynamicCnt++;
                }
                staticSize += param.type.getFixedSize();
            }

            byte[][] bb = new byte[args.length + dynamicCnt][];

            int curDynamicPtr = staticSize;
            int curDynamicCnt = 0;
            for (int i = 0; i < args.length; i++) {
                if (inputs[i].type.isDynamicType()) {
                    byte[] dynBB = inputs[i].type.encode(args[i]);
                    bb[i] = SolidityType.IntType.encodeInt(curDynamicPtr);
                    bb[args.length + curDynamicCnt] = dynBB;
                    curDynamicCnt++;
                    curDynamicPtr += dynBB.length;
                } else {
                    bb[i] = inputs[i].type.encode(args[i]);
                }
            }
            return ByteUtil.merge(bb);
        }

        private Object[] decode(byte[] encoded, Param[] params) {
            Object[] ret = new Object[params.length];

            int off = 0;
            for (int i = 0; i < params.length; i++) {
                if (params[i].type.isDynamicType()) {
                    ret[i] = params[i].type.decode(encoded, SolidityType.IntType.decodeInt(encoded, off).intValue());
                } else {
                    ret[i] = params[i].type.decode(encoded, off);
                }
                off += params[i].type.getFixedSize();
            }
            return ret;
        }

        /**
         * Decode object [ ].
         *
         * @param encoded the encoded
         * @return the object [ ]
         */
        public Object[] decode(byte[] encoded) {
            return decode(subarray(encoded, 4, encoded.length), inputs);
        }

        /**
         * Decode result object [ ].
         *
         * @param encodedRet the encoded ret
         * @return the object [ ]
         */
        public Object[] decodeResult(byte[] encodedRet) {
            return decode(encodedRet, outputs);
        }

        /**
         * Format signature string.
         *
         * @return the string
         */
        public String formatSignature() {
            StringBuilder paramsTypes = new StringBuilder();
            for (Param param : inputs) {
                paramsTypes.append(param.type.getCanonicalName()).append(",");
            }

            return format("%s(%s)", name, stripEnd(paramsTypes.toString(), ","));
        }

        /**
         * Encode signature long byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] encodeSignatureLong() {
            String signature = formatSignature();
            byte[] sha3Fingerprint = HashUtil.sha3(signature.getBytes());
            return sha3Fingerprint;
        }

        /**
         * Encode signature byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] encodeSignature() {
            return Arrays.copyOfRange(encodeSignatureLong(), 0, 4);
        }

        @Override
        public String toString() {
            return formatSignature();
        }

        /**
         * From json interface function.
         *
         * @param json the json
         * @return the function
         */
        public static Function fromJsonInterface(String json) {
            try {
                return DEFAULT_MAPPER.readValue(json, Function.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * From signature function.
         *
         * @param funcName   the func name
         * @param paramTypes the param types
         * @return the function
         */
        public static Function fromSignature(String funcName, String ... paramTypes) {
            return fromSignature(funcName, paramTypes, new String[0]);
        }

        /**
         * From signature function.
         *
         * @param funcName    the func name
         * @param paramTypes  the param types
         * @param resultTypes the result types
         * @return the function
         */
        public static Function fromSignature(String funcName, String[] paramTypes, String[] resultTypes) {
            Function ret = new Function();
            ret.name = funcName;
            ret.constant = false;
            ret.type = FunctionType.function;
            ret.inputs = new Param[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                ret.inputs[i] = new Param();
                ret.inputs[i].name = "param" + i;
                ret.inputs[i].type = SolidityType.getType(paramTypes[i]);
            }
            ret.outputs = new Param[resultTypes.length];
            for (int i = 0; i < resultTypes.length; i++) {
                ret.outputs[i] = new Param();
                ret.outputs[i].name = "res" + i;
                ret.outputs[i].type = SolidityType.getType(resultTypes[i]);
            }
            return ret;
        }


    }

    /**
     * The type Contract.
     */
    public static class Contract {
        /**
         * Instantiates a new Contract.
         *
         * @param jsonInterface the json interface
         */
        public Contract(String jsonInterface) {
            try {
                functions = new ObjectMapper().readValue(jsonInterface, Function[].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Gets by name.
         *
         * @param name the name
         * @return the by name
         */
        public Function getByName(String name) {
            for (Function function : functions) {
                if (name.equals(function.name)) {
                    return function;
                }
            }
            return null;
        }

        /**
         * Gets constructor.
         *
         * @return the constructor
         */
        public Function getConstructor() {
            for (Function function : functions) {
                if (function.type == FunctionType.constructor) {
                    return function;
                }
            }
            return null;
        }

        private Function getBySignatureHash(byte[] hash) {
            if (hash.length == 4 ) {
                for (Function function : functions) {
                    if (FastByteComparisons.equal(function.encodeSignature(), hash)) {
                        return function;
                    }
                }
            } else if (hash.length == 32 ) {
                for (Function function : functions) {
                    if (FastByteComparisons.equal(function.encodeSignatureLong(), hash)) {
                        return function;
                    }
                }
            } else {
                throw new RuntimeException("Function signature hash should be 4 or 32 bytes length");
            }
            return null;
        }

        /**
         * Parses function and its arguments from transaction invocation binary data
         *
         * @param data the data
         * @return the invocation
         */
        public Invocation parseInvocation(byte[] data) {
            if (data.length < 4) {
                throw new RuntimeException("Invalid data length: " + data.length);
            }
            Function function = getBySignatureHash(Arrays.copyOfRange(data, 0, 4));
            if (function == null) {
                throw new RuntimeException("Can't find function/event by it signature");
            }
            Object[] args = function.decode(data);
            return new Invocation(this, function, args);
        }

        /**
         * Parses Solidity Event and its data members from transaction receipt LogInfo
         *
         * @param eventLog the event log
         * @return the invocation
         */
        public Invocation parseEvent(LogInfo eventLog) {
            CallTransaction.Function event = getBySignatureHash(eventLog.getTopics().get(0).getData());
            int indexedArg = 1;
            if (event == null) {
                return null;
            }
            List<Object> indexedArgs = new ArrayList<>();
            List<Param> unindexed = new ArrayList<>();
            for (Param input : event.inputs) {
                if (input.indexed) {
                    indexedArgs.add(input.type.decode(eventLog.getTopics().get(indexedArg++).getData()));
                    continue;
                }
                unindexed.add(input);
            }

            Object[] unindexedArgs = event.decode(eventLog.getData(), unindexed.toArray(new Param[unindexed.size()]));
            Object[] args = new Object[event.inputs.length];
            int unindexedIndex = 0;
            int indexedIndex = 0;
            for (int i = 0; i < args.length; i++) {
                if (event.inputs[i].indexed) {
                    args[i] = indexedArgs.get(indexedIndex++);
                    continue;
                }
                args[i] = unindexedArgs[unindexedIndex++];
            }
            return new Invocation(this, event, args);
        }

        /**
         * The Functions.
         */
        public Function[] functions;
    }

    /**
     * Represents either function invocation with its arguments
     * or Event instance with its data members
     */
    public static class Invocation {
        /**
         * The Contract.
         */
        public final Contract contract;
        /**
         * The Function.
         */
        public final Function function;
        /**
         * The Args.
         */
        public final Object[] args;

        /**
         * Instantiates a new Invocation.
         *
         * @param contract the contract
         * @param function the function
         * @param args     the args
         */
        public Invocation(Contract contract, Function function, Object[] args) {
            this.contract = contract;
            this.function = function;
            this.args = args;
        }

        @Override
        public String toString() {
            return "[" + "contract=" + contract +
                    (function.type == FunctionType.event ? ", event=" : ", function=")
                    + function + ", args=" + Arrays.toString(args) + ']';
        }
    }
}
