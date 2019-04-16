package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.vo.CreateCurrencyVO;
import com.higgschain.trust.rs.core.vo.MultiSignHashVO;
import com.higgschain.trust.rs.core.vo.MultiSignRuleVO;
import com.higgschain.trust.rs.core.vo.MultiSignTxVO;
import com.higgschain.trust.common.vo.RespData;

/**
 * The interface Multi sign service.
 *
 * @author liuyu
 * @description
 * @date 2019 -03-20
 */
public interface MultiSignService {

    /**
     * create a address
     *
     * @param rule validation rules for the address
     * @return resp data
     * @throws RsCoreException the rs core exception
     */
    RespData<String> createAddress(MultiSignRuleVO rule) throws RsCoreException;

    /**
     * create currency contract
     *
     * @param vo the vo
     * @return resp data
     * @throws RsCoreException the rs core exception
     */
    RespData<Boolean> createCurrencyContract(CreateCurrencyVO vo) throws RsCoreException;

    /**
     * get sign-hash by tx data
     *
     * @param vo the vo
     * @return sign hash value
     * @throws RsCoreException the rs core exception
     */
    RespData<String> getSignHashValue(MultiSignHashVO vo) throws RsCoreException;

    /**
     * Transfer resp data.
     *
     * @param vo the vo
     * @return resp data
     * @throws RsCoreException the rs core exception
     */
    RespData<Boolean> transfer(MultiSignTxVO vo) throws RsCoreException;

}
