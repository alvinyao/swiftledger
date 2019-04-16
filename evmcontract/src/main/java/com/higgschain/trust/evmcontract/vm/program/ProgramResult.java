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
package com.higgschain.trust.evmcontract.vm.program;

import com.higgschain.trust.evmcontract.util.ByteArraySet;
import com.higgschain.trust.evmcontract.vm.CallCreate;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.LogInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.higgschain.trust.evmcontract.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.size;

/**
 * The type Program result.
 *
 * @author Roman Mandeleil
 * @since 07.06.2014
 */
public class ProgramResult {

    private long gasUsed;
    private byte[] hReturn = EMPTY_BYTE_ARRAY;
    private RuntimeException exception;
    private boolean revert;

    private Set<DataWord> deleteAccounts;
    private ByteArraySet touchedAccounts = new ByteArraySet();
    private List<InternalTransaction> internalTransactions;
    private List<LogInfo> logInfoList;
    private long futureRefund = 0;

    /**
     * for testing runs ,
     * call/create is not executed
     * but dummy recorded
     */
    private List<CallCreate> callCreateList;

    /**
     * Create empty program result.
     *
     * @return the program result
     */
    public static ProgramResult createEmpty() {
        ProgramResult result = new ProgramResult();
        result.setHReturn(EMPTY_BYTE_ARRAY);
        return result;
    }

    /**
     * Spend gas.
     *
     * @param gas the gas
     */
    public void spendGas(long gas) {
        gasUsed += gas;
    }

    /**
     * Sets revert.
     */
    public void setRevert() {
        this.revert = true;
    }

    /**
     * Is revert boolean.
     *
     * @return the boolean
     */
    public boolean isRevert() {
        return revert;
    }

    /**
     * Refund gas.
     *
     * @param gas the gas
     */
    public void refundGas(long gas) {
        gasUsed -= gas;
    }

    /**
     * Get h return byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHReturn() {
        return hReturn;
    }

    /**
     * Sets h return.
     *
     * @param hReturn the h return
     */
    public void setHReturn(byte[] hReturn) {
        this.hReturn = hReturn;

    }

    /**
     * Gets exception.
     *
     * @return the exception
     */
    public RuntimeException getException() {
        return exception;
    }

    /**
     * Sets exception.
     *
     * @param exception the exception
     */
    public void setException(RuntimeException exception) {
        this.exception = exception;
    }

    /**
     * Gets gas used.
     *
     * @return the gas used
     */
    public long getGasUsed() {
        return gasUsed;
    }

    /**
     * Gets delete accounts.
     *
     * @return the delete accounts
     */
    public Set<DataWord> getDeleteAccounts() {
        if (deleteAccounts == null) {
            deleteAccounts = new HashSet<>();
        }
        return deleteAccounts;
    }

    /**
     * Add delete account.
     *
     * @param address the address
     */
    public void addDeleteAccount(DataWord address) {
        getDeleteAccounts().add(address);
    }

    /**
     * Add delete accounts.
     *
     * @param accounts the accounts
     */
    public void addDeleteAccounts(Set<DataWord> accounts) {
        if (!isEmpty(accounts)) {
            getDeleteAccounts().addAll(accounts);
        }
    }

    /**
     * Add touch account.
     *
     * @param addr the addr
     */
    public void addTouchAccount(byte[] addr) {
        touchedAccounts.add(addr);
    }

    /**
     * Gets touched accounts.
     *
     * @return the touched accounts
     */
    public Set<byte[]> getTouchedAccounts() {
        return touchedAccounts;
    }

    /**
     * Add touch accounts.
     *
     * @param accounts the accounts
     */
    public void addTouchAccounts(Set<byte[]> accounts) {
        if (!isEmpty(accounts)) {
            getTouchedAccounts().addAll(accounts);
        }
    }

    /**
     * Gets log info list.
     *
     * @return the log info list
     */
    public List<LogInfo> getLogInfoList() {
        if (logInfoList == null) {
            logInfoList = new ArrayList<>();
        }
        return logInfoList;
    }

    /**
     * Add log info.
     *
     * @param logInfo the log info
     */
    public void addLogInfo(LogInfo logInfo) {
        getLogInfoList().add(logInfo);
    }

    /**
     * Add log infos.
     *
     * @param logInfos the log infos
     */
    public void addLogInfos(List<LogInfo> logInfos) {
        if (!isEmpty(logInfos)) {
            getLogInfoList().addAll(logInfos);
        }
    }

    /**
     * Gets call create list.
     *
     * @return the call create list
     */
    public List<CallCreate> getCallCreateList() {
        if (callCreateList == null) {
            callCreateList = new ArrayList<>();
        }
        return callCreateList;
    }

    /**
     * Add call create.
     *
     * @param data        the data
     * @param destination the destination
     * @param gasLimit    the gas limit
     * @param value       the value
     */
    public void addCallCreate(byte[] data, byte[] destination, byte[] gasLimit, byte[] value) {
        getCallCreateList().add(new CallCreate(data, destination, gasLimit, value));
    }

    /**
     * Gets internal transactions.
     *
     * @return the internal transactions
     */
    public List<InternalTransaction> getInternalTransactions() {
        if (internalTransactions == null) {
            internalTransactions = new ArrayList<>();
        }
        return internalTransactions;
    }

    /**
     * Add internal transaction internal transaction.
     *
     * @param parentHash     the parent hash
     * @param deep           the deep
     * @param nonce          the nonce
     * @param gasPrice       the gas price
     * @param gasLimit       the gas limit
     * @param senderAddress  the sender address
     * @param receiveAddress the receive address
     * @param value          the value
     * @param data           the data
     * @param note           the note
     * @return the internal transaction
     */
    public InternalTransaction addInternalTransaction(byte[] parentHash, int deep, byte[] nonce, DataWord gasPrice, DataWord gasLimit,
                                                      byte[] senderAddress, byte[] receiveAddress, byte[] value, byte[] data, String note) {
        InternalTransaction transaction = new InternalTransaction(parentHash, deep, size(internalTransactions), nonce, gasPrice, gasLimit, senderAddress, receiveAddress, value, data, note);
        getInternalTransactions().add(transaction);
        return transaction;
    }

    /**
     * Add internal transactions.
     *
     * @param internalTransactions the internal transactions
     */
    public void addInternalTransactions(List<InternalTransaction> internalTransactions) {
        getInternalTransactions().addAll(internalTransactions);
    }

    /**
     * Reject internal transactions.
     */
    public void rejectInternalTransactions() {
        for (InternalTransaction internalTx : getInternalTransactions()) {
            internalTx.reject();
        }
    }

    /**
     * Add future refund.
     *
     * @param gasValue the gas value
     */
    public void addFutureRefund(long gasValue) {
        futureRefund += gasValue;
    }

    /**
     * Gets future refund.
     *
     * @return the future refund
     */
    public long getFutureRefund() {
        return futureRefund;
    }

    /**
     * Reset future refund.
     */
    public void resetFutureRefund() {
        futureRefund = 0;
    }

    /**
     * Merge.
     *
     * @param another the another
     */
    public void merge(ProgramResult another) {
        addInternalTransactions(another.getInternalTransactions());
        if (another.getException() == null && !another.isRevert()) {
            addDeleteAccounts(another.getDeleteAccounts());
            addLogInfos(another.getLogInfoList());
            addFutureRefund(another.getFutureRefund());
            addTouchAccounts(another.getTouchedAccounts());
        }
    }
}
