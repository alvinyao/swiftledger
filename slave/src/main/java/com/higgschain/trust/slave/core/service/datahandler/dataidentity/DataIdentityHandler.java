package com.higgschain.trust.slave.core.service.datahandler.dataidentity;

import com.higgschain.trust.slave.model.bo.DataIdentity;

public interface DataIdentityHandler {

    /**
     * get dataIdentity by identity
     * @param identity
     * @return
     */
     DataIdentity getDataIdentity(String identity);


    /**
     * save dataIdentity
     * @param dataIdentity
     * @return
     */
    void saveDataIdentity(DataIdentity dataIdentity);


}
