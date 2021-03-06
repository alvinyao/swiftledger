package com.higgschain.trust.evmcontract.facade;

import com.higgschain.trust.evmcontract.core.Bloom;
import com.higgschain.trust.evmcontract.util.ByteArraySet;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.LogInfo;
import com.higgschain.trust.evmcontract.vm.program.InternalTransaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * An instance of this class is used to save result of contract execution.
 *
 * @author Chen Jiawei
 * @date 2018 -11-15
 */
public class ContractExecutionResult {

    private static final ThreadLocal<ContractExecutionResult> CURRENT_RESULT = new ThreadLocal<>();

    @Getter
    @Setter
    private String method;

    /**
     * Address list of accounts participating in the contract execution.
     */
    @Getter
    private ByteArraySet touchedAccountAddresses = new ByteArraySet();

    /**
     * Hash of transaction which contains the contract.
     */
    @Getter
    @Setter
    private byte[] transactionHash;

    /**
     * Amount of asset transferred to receiver.
     */
    @Getter
    @Setter
    private byte[] value;

    /**
     * Address of receiver.
     */
    @Getter
    @Setter
    private byte[] receiverAddress;

    /**
     * Returned event records.
     */
    @Getter
    private List<LogInfo> logInfoList;

    /**
     * Sets log info list.
     *
     * @param logInfoList the log info list
     */
    public void setLogInfoList(List<LogInfo> logInfoList) {
        if (logInfoList == null) {
            return;
        }
        this.logInfoList = logInfoList;
        for (LogInfo loginfo : logInfoList) {
            bloomFilter.or(loginfo.getBloom());
        }
    }

    /**
     * Bloom filter for transaction.
     */
    @Getter
    private Bloom bloomFilter = new Bloom();

    /**
     * Byte code stored after contract execution.
     */
    @Getter
    @Setter
    private byte[] result;

    /**
     * Deleted account address during contract execution.
     */
    @Getter
    @Setter
    private Set<DataWord> deleteAccounts;

    /**
     * Internal transaction happened during contract execution.
     */
    @Getter
    @Setter
    private List<InternalTransaction> internalTransactions;

    /**
     * Exception thrown during contract execution.
     */
    @Getter
    @Setter
    private RuntimeException exception;

    /**
     * Root hash of global state after contract execution.
     */
    @Getter
    @Setter
    private byte[] stateRoot;

    /**
     * Revert or exception information, null if contract is executed
     * successfully.
     */
    @Getter
    @Setter
    private String errorMessage;

    /**
     * If returning revert.
     * boolean Getter is not supported.
     */
    @Setter
    private boolean revert;

    /**
     * Gets revert.
     *
     * @return the revert
     */
    public boolean getRevert() {
        return revert;
    }

    /**
     * Time cost for contract execution.
     */
    @Getter
    @Setter
    private long timeCost;

    /**
     * Set the result to ThreadLocal
     *
     * @param result the result
     */
    public static void setCurrentResult(ContractExecutionResult result) {
        CURRENT_RESULT.set(result);
    }

    /**
     * Get result from ThreadLocal
     *
     * @return current result
     */
    public static ContractExecutionResult getCurrentResult() {
        return CURRENT_RESULT.get();
    }

    /**
     * Clear result on ThreadLocal
     */
    public static void clearCurrentResult() {
        CURRENT_RESULT.remove();
    }
}
