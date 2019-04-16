package com.higgschain.trust.slave.core.service.datahandler.account;

import com.higgschain.trust.slave.model.bo.account.*;

/**
 * The interface Account handler.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-16
 */
public interface AccountHandler {

    /**
     * get account info from cache or db
     *
     * @param accountNo the account no
     * @return account info
     */
    AccountInfo getAccountInfo(String accountNo);

    /**
     * query currency info
     *
     * @param currency the currency
     * @return currency info
     */
    CurrencyInfo queryCurrency(String currency);

    /**
     * Open account.
     *
     * @param openAccount the open account
     */
    void openAccount(OpenAccount openAccount);

    /**
     * validate account operation
     *
     * @param accountOperation the account operation
     * @param policyId         the policy id
     */
    void validateForOperation(AccountOperation accountOperation, String policyId);

    /**
     * persist account operation
     *
     * @param accountOperation the account operation
     * @param blockHeight      the block height
     */
    void persistForOperation(AccountOperation accountOperation, Long blockHeight);

    /**
     * get account freeze record
     *
     * @param bizFlowNo the biz flow no
     * @param accountNo the account no
     * @return account freeze record
     */
    AccountFreezeRecord getAccountFreezeRecord(String bizFlowNo, String accountNo);

    /**
     * freeze
     *
     * @param accountFreeze the account freeze
     * @param blockHeight   the block height
     */
    void freeze(AccountFreeze accountFreeze, Long blockHeight);

    /**
     * unfreeze
     *
     * @param accountUnFreeze the account un freeze
     * @param freezeRecord    the freeze record
     * @param blockHeight     the block height
     */
    void unfreeze(AccountUnFreeze accountUnFreeze, AccountFreezeRecord freezeRecord, Long blockHeight);

    /**
     * issue new currency
     *
     * @param bo the bo
     */
    void issueCurrency(IssueCurrency bo);
}
