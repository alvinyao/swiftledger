package com.higgschain.trust.evmcontract.facade.compile;

import com.higgschain.trust.evmcontract.solidity.Abi;

import java.util.List;

/**
 * The type Contract invocation.
 *
 * @author Chen Jiawei
 * @date 2018 -11-29
 */
public class ContractInvocation {
    private Abi.Function function;

    /**
     * Encode input byte [ ].
     *
     * @param methodSignature the method signature
     * @param args            the args
     * @return the byte [ ]
     */
    public byte[] encodeInput(String methodSignature, Object... args) {
        Abi.Function function = Abi.Function.of(methodSignature);
        this.function = function;
        return function.encode(args);
    }

    /**
     * Decode result list.
     *
     * @param result        the result
     * @param humanReadable the human readable
     * @return the list
     */
    public List<?> decodeResult(byte[] result, boolean humanReadable) {
        return function == null ? null : function.decodeResult(result, humanReadable);
    }

    /**
     * Get bytecode for invoke contract byte [ ].
     *
     * @param methodSignature the method signature
     * @param args            the args
     * @return the byte [ ]
     */
    public byte[] getBytecodeForInvokeContract(String methodSignature, Object... args) {
        return encodeInput(methodSignature, args);
    }
}
