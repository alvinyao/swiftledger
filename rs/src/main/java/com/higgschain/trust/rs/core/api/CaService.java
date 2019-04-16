package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.slave.api.vo.CaVO;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.ca.Ca;

import java.util.List;

/**
 * The interface Ca service.
 *
 * @author WangQuanzhou
 * @date 2018 /6/5 15:40
 */
public interface CaService {

    /**
     * Auth key pair string.
     *
     * @param user the user
     * @return string
     * @desc generate pubKey and PriKey ,then insert into db
     */
    String authKeyPair(String user);

    /**
     * Auth ca tx resp data.
     *
     * @param list the list
     * @return resp data
     * @desc construct ca auth tx and send to slave
     */
    RespData authCaTx(List<CaVO> list);

    /**
     * Update key pair resp data.
     *
     * @param user the user
     * @return resp data
     * @desc update pubKey and priKey ,then insert into db
     */
    RespData updateKeyPair(String user);

    /**
     * Update ca tx resp data.
     *
     * @param caVO the ca vo
     * @return resp data
     * @desc construct ca update tx and send to slave
     */
    RespData updateCaTx(CaVO caVO);

    /**
     * Cancel key pair resp data.
     *
     * @param user the user
     * @return resp data
     * @desc cancel pubKey and PriKey ,then update db
     */
    RespData cancelKeyPair(String user);

    /**
     * Cancel ca tx resp data.
     *
     * @param caVO the ca vo
     * @return resp data
     * @desc construct ca cancel tx and send to slave
     */
    RespData cancelCaTx(CaVO caVO);

    /**
     * Callback ca.
     *
     * @param
     * @return
     * @desc after ca tx has bean authoritied by the current cluster, then update table config column valid to TRUE
     */
    void callbackCa();

    /**
     * Acquire ca resp data.
     *
     * @param user the user
     * @return resp data
     * @desc acquire CA information by user
     */
    RespData<Ca> acquireCA(String user);

    /**
     * Gets ca.
     *
     * @param user the user
     * @return ca
     */
    Ca getCa(String user);
}
