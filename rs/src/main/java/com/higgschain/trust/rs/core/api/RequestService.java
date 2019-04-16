package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.common.enums.RequestEnum;
import com.higgschain.trust.rs.core.dao.po.RequestPO;
import com.higgschain.trust.rs.core.vo.RequestVO;
import com.higgschain.trust.rs.core.vo.RsCoreTxVO;
import com.higgschain.trust.common.vo.RespData;

import java.util.List;

/**
 * The interface Request service.
 *
 * @description: RequestService
 * @author: lingchao
 * @datetime:2018年11月28日19:42
 */
public interface RequestService {
    /**
     * check request not Idempotent
     *
     * @param requestId the request id
     * @return resp data
     */
    RespData<?> requestIdempotent(String requestId);

    /**
     * check request idempotent
     *
     * @param requestId the request id
     * @return request vo
     */
    RequestVO queryByRequestId(String requestId);

    /**
     * insert request
     *
     * @param requestId   the request id
     * @param requestEnum the request enum
     * @param respCode    the resp code
     * @param respMsg     the resp msg
     * @return
     */
    void insertRequest(String requestId, RequestEnum requestEnum, String respCode, String respMsg);

    /**
     * batch insert
     *
     * @param requestPOList the request po list
     */
    void batchInsert(List<RequestPO> requestPOList);

    /**
     * Update resp data.
     *
     * @param requestId the request id
     * @param respCode  the resp code
     * @param respMsg   the resp msg
     */
    void updateRespData(String requestId, String respCode, String respMsg);

    /**
     * Update status and resp data.
     *
     * @param requestId  the request id
     * @param fromStatus the from status
     * @param toStatus   the to status
     * @param respCode   the resp code
     * @param respMsg    the resp msg
     */
    void updateStatusAndRespData(String requestId, RequestEnum fromStatus, RequestEnum toStatus, String respCode, String respMsg);

    /**
     * batch update
     *
     * @param rsCoreTxVOS the rs core tx vos
     * @param from        the from
     * @param to          the to
     */
    void batchUpdateStatus(List<RsCoreTxVO> rsCoreTxVOS, RequestEnum from, RequestEnum to);
}
