package com.higgschain.trust.slave.model.convert;

import com.higgschain.trust.slave.model.bo.DataIdentity;

import java.util.Date;

/**
 * Data identity convert
 *
 * @author lingchao
 * @create 2018年04月17日20 :32
 */
public class DataIdentityConvert {

    /**
     * build a data identity bo
     *
     * @param identity   the identity
     * @param chainOwner the chain owner
     * @param dataOwner  the data owner
     * @return data identity
     */
    public static  DataIdentity buildDataIdentity(String identity, String chainOwner, String dataOwner){
        DataIdentity dataIdentity = new DataIdentity();
        dataIdentity.setIdentity(identity);
        dataIdentity.setChainOwner(chainOwner);
        dataIdentity.setDataOwner(dataOwner);
        dataIdentity.setCreateTime(new Date());
        return dataIdentity;
    }
}
