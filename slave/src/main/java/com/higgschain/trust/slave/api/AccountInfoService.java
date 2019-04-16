package com.higgschain.trust.slave.api;

import com.higgschain.trust.slave.api.vo.AccountInfoVO;
import com.higgschain.trust.slave.api.vo.PageVO;
import com.higgschain.trust.slave.api.vo.QueryAccountVO;

import java.util.List;

/**
 * The interface Account info service.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-09
 */
public interface AccountInfoService {
    /**
     * batch query the account info
     *
     * @param accountNos the account nos
     * @return list
     */
    List<AccountInfoVO> queryByAccountNos(List<String> accountNos);

    /**
     * Query account info page vo.
     *
     * @param req the req
     * @return the page vo
     */
    PageVO<AccountInfoVO> queryAccountInfo(QueryAccountVO req);

    /**
     * query accounts
     *
     * @param req the req
     * @return list
     */
    List<AccountInfoVO> queryAccountsByPage(QueryAccountVO req);
}
