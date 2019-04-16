package com.higgschain.trust.evmcontract.facade.compile;

import com.higgschain.trust.evmcontract.solidity.Abi;
import com.higgschain.trust.evmcontract.solidity.compiler.CompilationResult;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;

/**
 * The type Contract creation.
 *
 * @author Chen Jiawei
 * @date 2018 -11-29
 */
public class ContractCreation {
    /**
     * Encode input byte [ ].
     *
     * @param metadata             the metadata
     * @param constructorSignature the constructor signature
     * @param args                 the args
     * @return the byte [ ]
     */
    public static byte[] encodeInput(
            CompilationResult.ContractMetadata metadata, String constructorSignature, Object... args) {
        return Abi.Constructor.of(constructorSignature, Hex.decode(metadata.bin), args);
    }

    /**
     * Get bytecode for deploy contract byte [ ].
     *
     * @param filePath             the file path
     * @param contractName         the contract name
     * @param constructorSignature the constructor signature
     * @param args                 the args
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] getBytecodeForDeployContract(String filePath, String contractName, String constructorSignature, Object... args) throws IOException {
        CompilationResult compilationResult = CompileManager.getCompilationResultByFile(filePath);
        CompilationResult.ContractMetadata metadata = compilationResult.getContract(contractName);

        return encodeInput(metadata, constructorSignature, args);
    }
}
