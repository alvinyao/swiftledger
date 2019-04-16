package com.higgschain.trust.slave.core.service.datahandler.dataidentity;

import com.higgschain.trust.slave.model.bo.DataIdentity;

/**
 * The interface Data identity handler.
 */
public interface DataIdentityHandler {

    /**
     * get dataIdentity by identity
     *
     * @param identity the identity
     * @return data identity
     */
    DataIdentity getDataIdentity(String identity);

    /**
     * save dataIdentity
     *
     * @param dataIdentity the data identity
     * @return
     */
    void saveDataIdentity(DataIdentity dataIdentity);


}
