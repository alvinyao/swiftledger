package com.higgschain.trust.evmcontract.facade;

import com.higgschain.trust.evmcontract.facade.exception.ContractContextException;
import com.higgschain.trust.evmcontract.facade.exception.ContractExecutionException;
import com.higgschain.trust.evmcontract.util.ByteArraySet;

import java.util.Objects;

/**
 * The type Asset transfer executor.
 *
 * @author Chen Jiawei
 * @date 2018 -11-16
 */
public class AssetTransferExecutor extends BaseContractExecutor {
    /**
     * Instantiates a new Asset transfer executor.
     *
     * @param contractExecutionContext the contract execution context
     */
    AssetTransferExecutor(ContractExecutionContext contractExecutionContext) {
        super(contractExecutionContext);
    }


    @Override
    protected void checkData() {
        if (Objects.nonNull(data)) {
            throw new ContractContextException("Payload shall be null for asset transfer");
        }
    }

    @Override
    protected void checkReceiverAccount() {
        checkReceiverAddress();

        if (Objects.isNull(contractRepository.getAccountState(receiverAddress))) {
            throw new ContractContextException("Account with specific address does not exist");
        }
    }

    @Override
    protected void checkCode() {

    }


    @Override
    protected ContractExecutionResult executeContract() {
        transactionRepository.increaseNonce(senderAddress);


        RuntimeException exception = null;
        ByteArraySet touchedAccountAddresses = new ByteArraySet();
        try {
            transferValue();
            touchedAccountAddresses.add(receiverAddress);
            contractRepository.commit();
        } catch (Throwable e) {
            exception = new RuntimeException(e.getMessage(), e);
            touchedAccountAddresses.remove(receiverAddress);
            contractRepository.rollback();
        }


        transactionRepository.commit();


        ContractExecutionResult contractExecutionResult = new ContractExecutionResult();
        if (exception != null) {
            contractExecutionResult.setErrorMessage(exception.getMessage());
            contractExecutionResult.setException(
                    new ContractExecutionException("Exception happens when transferring asset", exception));
        }
        contractExecutionResult.getTouchedAccountAddresses().addAll(touchedAccountAddresses);
        contractExecutionResult.setTransactionHash(transactionHash);
        contractExecutionResult.setValue(value);
        contractExecutionResult.setReceiverAddress(receiverAddress);
        contractExecutionResult.setStateRoot(blockRepository.getRoot());

        return contractExecutionResult;
    }
}
