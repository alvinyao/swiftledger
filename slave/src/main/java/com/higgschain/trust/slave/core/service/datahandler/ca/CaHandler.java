package com.higgschain.trust.slave.core.service.datahandler.ca;

import com.higgschain.trust.slave.dao.po.ca.CaPO;
import com.higgschain.trust.slave.model.bo.ca.Ca;

/**
 * The interface Ca handler.
 *
 * @author WangQuanzhou
 * @desc CA handler
 * @date 2018 /6/6 10:33
 */
public interface CaHandler {

    /**
     * Auth ca.
     *
     * @param ca the ca
     * @return
     * @desc insert CA into db
     */
    void authCa(Ca ca);

    /**
     * Update ca.
     *
     * @param ca the ca
     * @return
     * @desc update CA information
     */
    void updateCa(Ca ca);

    /**
     * Cancel ca.
     *
     * @param ca the ca
     * @return
     * @desc cancel CA information
     */
    void cancelCa(Ca ca);

    /**
     * Gets ca.
     *
     * @param nodeName the node name
     * @param usage    the usage
     * @return Ca ca
     * @desc get CA information by nodeName
     */
    CaPO getCa(String nodeName, String usage);
}
