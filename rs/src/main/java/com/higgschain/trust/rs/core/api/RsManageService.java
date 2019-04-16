package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.core.vo.manage.CancelRsVO;
import com.higgschain.trust.rs.core.vo.manage.RegisterPolicyVO;
import com.higgschain.trust.rs.core.vo.manage.RegisterRsVO;
import com.higgschain.trust.common.vo.RespData;

/**
 * The interface Rs manage service.
 *
 * @author tangfashuang
 * @date 2018 /05/18 13:56
 * @desc rs manage service
 */
public interface RsManageService {

    /**
     * register rs
     *
     * @param registerRsVO the register rs vo
     * @return resp data
     */
    RespData registerRs(RegisterRsVO registerRsVO);

    /**
     * register policy
     *
     * @param registerPolicyVO the register policy vo
     * @return resp data
     */
    RespData registerPolicy(RegisterPolicyVO registerPolicyVO);

    /**
     * cancel RS
     *
     * @param cancelRsVO the cancel rs vo
     * @return resp data
     */
    RespData cancelRs(CancelRsVO cancelRsVO);
}
