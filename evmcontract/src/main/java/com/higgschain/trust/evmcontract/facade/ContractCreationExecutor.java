package com.higgschain.trust.evmcontract.facade;

import com.higgschain.trust.evmcontract.core.Transaction;
import com.higgschain.trust.evmcontract.facade.exception.ContractExecutionException;
import com.higgschain.trust.evmcontract.util.ByteArraySet;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.VM;
import com.higgschain.trust.evmcontract.vm.program.Program;
import com.higgschain.trust.evmcontract.vm.program.ProgramResult;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvoke;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Chen Jiawei
 * @date 2018-11-15
 */
public class ContractCreationExecutor extends BaseContractExecutor {
    ContractCreationExecutor(ContractExecutionContext contractExecutionContext) {
        super(contractExecutionContext);
    }


    @Override
    protected ContractExecutionResult executeContract() {
        transactionRepository.increaseNonce(senderAddress);


        ProgramResult programResult = new ProgramResult();
        ByteArraySet touchedAccountAddresses = new ByteArraySet();
        try {
            contractRepository.createAccount(receiverAddress);
            transferValue();
            touchedAccountAddresses.add(receiverAddress);

            Transaction transaction = new Transaction(nonce, gasPrice, gasLimit, receiverAddress, value, data);
            transaction.setHash(transactionHash);
            ProgramInvoke programInvoke = buildProgramInvoke();
            VM vm = new VM(systemProperties);
            Program program = new Program(data, programInvoke, transaction, systemProperties);
            vm.play(program);
            programResult = program.getResult();

            processProgramResult(programResult, touchedAccountAddresses);
        } catch (Throwable e) {
            touchedAccountAddresses.remove(receiverAddress);
            contractRepository.rollback();
        }


        for (DataWord address : programResult.getDeleteAccounts()) {
            transactionRepository.delete(address.getLast20Bytes());
        }
        transactionRepository.commit();

        return buildContractExecutionResult(programResult, touchedAccountAddresses);
    }

    @Override
    protected void processProgramResult(final ProgramResult programResult,
                                        final ByteArraySet touchedAccountAddresses) {
        if (!programResult.isRevert()) {
            // The stored code must has size not more than the size of the data.
            if (programResult.getHReturn().length > ArrayUtils.getLength(data)) {
                programResult.setException(new ContractExecutionException(
                        String.format("Stored contract larger than data: %d", programResult.getHReturn().length)));
                programResult.setHReturn(new byte[0]);
            } else {
                contractRepository.saveCode(receiverAddress, programResult.getHReturn());
            }
        }

        super.processProgramResult(programResult, touchedAccountAddresses);
    }
}
