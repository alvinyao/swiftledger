package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.facade.*;
import com.higgschain.trust.evmcontract.facade.compile.ContractInvocation;
import com.higgschain.trust.evmcontract.facade.exception.ContractExecutionException;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.slave.core.Blockchain;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * The type Contract query.
 *
 * @author Chen Jiawei
 * @date 2018 -11-29
 */
@Service
public class ContractQuery {
    private static final Pattern BLOCK_HASH_PATTERN = Pattern.compile("^[0-9a-fA-F]{64}$");
    /**
     * The Blockchain.
     */
    @Autowired
    Blockchain blockchain;
    /**
     * The Node state.
     */
    @Autowired
    NodeState nodeState;
    @Autowired
    private BlockRepository blockRepository;

    /**
     * Execute query list.
     *
     * @param blockHeight     the block height
     * @param contractAddress the contract address
     * @param methodSignature the method signature
     * @param args            the args
     * @return the list
     */
    public List<?> executeQuery(long blockHeight, byte[] contractAddress, String methodSignature, Object... args) {
        ContractInvocation contractInvocation = new ContractInvocation();
        byte[] data = contractInvocation.getBytecodeForInvokeContract(methodSignature, args);

        ContractExecutionContext context = buildContractExecutionContext(blockHeight, contractAddress, data);
        ExecutorFactory<ContractExecutionContext, ContractExecutionResult> factory = new ContractExecutorFactory();
        Executor<ContractExecutionResult> executor = factory.createExecutor(context);
        ContractExecutionResult result = executor.execute();

        if (result.getRevert()) {
            throw new ContractExecutionException(result.getErrorMessage());
        }

        if (result.getException() != null) {
            throw result.getException();
        }

        return contractInvocation.decodeResult(result.getResult(), true);
    }

    private ContractExecutionContext buildContractExecutionContext(
            long blockHeight, byte[] receiverAddress, byte[] data) {
        ContractTypeEnum contractType = ContractTypeEnum.CUSTOMER_CONTRACT_QUERYING;
        byte[] nodeNameBytes = HashUtil.sha3omit12(nodeState.getNodeName().getBytes());
        // every one can query, even if no account exists.
        byte[] senderAddress = nodeNameBytes;
        byte[] nonce = blockchain.getRepository().getNonce(senderAddress).toByteArray();
        byte[] transactionHash = HashUtil.sha256(
                ("10000000" + Hex.toHexString(nodeNameBytes) + System.currentTimeMillis()).getBytes());
        byte[] value = new DataWord(0).getData();

        long number = blockchain.getLastBlockHeader().getHeight();
        String previousHash = blockchain.getLastBlockHeader().getPreviousHash();
        if (!BLOCK_HASH_PATTERN.matcher(previousHash).matches()) {
            throw new IllegalArgumentException("Block hash should be a hex string of 32 bytes");
        }
        byte[] parentHash = Hex.decode(previousHash);
        byte[] minerAddress = nodeNameBytes;
        // Trust use mills second and evm use second.
        long timestamp = blockchain.getLastBlockHeader().getBlockTime() / 1000;
        Repository repository =
                blockHeight <= 1 ? blockchain.getRepository() : blockchain.getRepositorySnapshot(blockHeight);

        return new ContractExecutionContext(contractType, transactionHash, nonce, senderAddress, receiverAddress,
                value, data, parentHash, minerAddress, timestamp, number,
                blockchain.getBlockStore(), repository);
    }
}
