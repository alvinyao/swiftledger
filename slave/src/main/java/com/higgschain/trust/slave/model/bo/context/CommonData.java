package com.higgschain.trust.slave.model.bo.context;

import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.action.Action;

/**
 * The interface Common data.
 *
 * @Description:
 * @author: pengdi
 */
public interface CommonData {

    /**
     * get the block generating by this package
     *
     * @return current block
     */
    Block getCurrentBlock();

    /**
     * get the package
     *
     * @return current package
     */
    Package getCurrentPackage();

    /**
     * get the executing transaction in this package processing
     *
     * @return current transaction
     */
    SignedTransaction getCurrentTransaction();

    /**
     * get the executing action in this transaction processing
     *
     * @return current action
     */
    Action getCurrentAction();
}
