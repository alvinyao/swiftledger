package com.higgschain.trust.evmcontract.facade;

import com.higgschain.trust.evmcontract.core.Transaction;
import com.higgschain.trust.evmcontract.facade.exception.ContractContextException;
import com.higgschain.trust.evmcontract.util.ByteArraySet;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.VM;
import com.higgschain.trust.evmcontract.vm.program.Program;
import com.higgschain.trust.evmcontract.vm.program.ProgramResult;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvoke;
import org.apache.commons.lang3.ArrayUtils;
import org.spongycastle.util.encoders.Hex;

import java.util.Objects;

/**
 * @author Chen Jiawei
 * @date 2018-11-15
 */
public class CustomerContractInvocationExecutor extends BaseContractExecutor {
    CustomerContractInvocationExecutor(ContractExecutionContext contractExecutionContext) {
        super(contractExecutionContext);
    }


    @Override
    protected void checkReceiverAccount() {
        checkReceiverAddress();

        if (Objects.isNull(contractRepository.getAccountState(receiverAddress))) {
            throw new ContractContextException(
                    "Account with receiver address does not exist, receiverAddress: " + Hex.toHexString(receiverAddress));
        }
    }

    @Override
    protected void checkCode() {
        checkReceiverAddress();

        byte[] code = transactionRepository.getCode(receiverAddress);
        if (ArrayUtils.isEmpty(code)) {
            throw new ContractContextException("Contract code is empty");
        }
    }


    @Override
    protected ContractExecutionResult executeContract() {
        transactionRepository.increaseNonce(senderAddress);


        ProgramResult programResult = new ProgramResult();
        ByteArraySet touchedAccountAddresses = new ByteArraySet();
        try {
            transferValue();
            touchedAccountAddresses.add(receiverAddress);

            byte[] codeHash = transactionRepository.getCodeHash(receiverAddress);
            byte[] code = transactionRepository.getCode(receiverAddress);
            Transaction transaction = new Transaction(nonce, gasPrice, gasLimit, receiverAddress, value, data);
            transaction.setHash(transactionHash);
            ProgramInvoke programInvoke = buildProgramInvoke();
            VM vm = new VM(systemProperties);
            Program program = new Program(codeHash, code, programInvoke, transaction, systemProperties);
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
    protected byte[] formatMessageData() {
        return data;
    }
}
