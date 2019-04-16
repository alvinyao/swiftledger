package com.higgschain.trust.slave.dao.po.transaction;

import com.higgschain.trust.common.mybatis.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Transaction receipt po.
 *
 * @author tangfashuang
 */
@Setter
@Getter
public class TransactionReceiptPO extends BaseEntity<TransactionReceiptPO> {
    /**
     * the id of the transaction
     */
    private String txId;
    /**
     * the execution result of the transaction
     */
    private boolean result;
    /**
     * error code for transaction execution
     */
    private String errorCode;

    /**
     * error message for transaction execution
     */
    private String errorMessage;

    /**
     * block height
     */
    private Long blockHeight;
}
