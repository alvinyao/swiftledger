package com.higgschain.trust.slave.model.bo.context;

import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.action.Action;

/**
 * @Description:
 * @author: pengdi
 **/
public interface CommonData {

    /**
     * get the block generating by this package
     *
     * @return
     */
    Block getCurrentBlock();

    /**
     * get the package
     *
     * @return
     */
    Package getCurrentPackage();

    /**
     * get the executing transaction in this package processing
     *
     * @return
     */
    SignedTransaction getCurrentTransaction();

    /**
     * get the executing action in this transaction processing
     *
     * @return
     */
    Action getCurrentAction();
}
