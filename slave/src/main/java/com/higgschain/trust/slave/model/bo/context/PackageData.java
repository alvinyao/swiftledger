package com.higgschain.trust.slave.model.bo.context;

import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.SignedTransaction;

import java.util.Map;

/**
 * The interface Package data.
 *
 * @Description:
 * @author: pengdi
 */
public interface PackageData extends CommonData {

    /**
     * set current transaction
     *
     * @param transaction the transaction
     * @return
     */
    void setCurrentTransaction(SignedTransaction transaction);

    /**
     * parse context transaction data in this package processing
     * use parse no get for JSON
     *
     * @return transaction data
     */
    TransactionData parseTransactionData();

    /**
     * set the package
     *
     * @param currentPackage the current package
     */
    void setCurrentPackage(Package currentPackage);

    /**
     * set the block
     *
     * @param block the block
     */
    void setCurrentBlock(Block block);

    /**
     * Sets rs pub key map.
     *
     * @param rsPubKeyMap the rs pub key map
     */
    void setRsPubKeyMap(Map<String, String> rsPubKeyMap);

    /**
     * Gets rs pub key map.
     *
     * @return the rs pub key map
     */
    Map<String, String> getRsPubKeyMap();
}
