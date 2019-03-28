package com.higgschain.trust.slave.core.api.impl;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.AccountInfoService;
import com.higgschain.trust.slave.api.vo.AccountInfoVO;
import com.higgschain.trust.slave.api.vo.PageVO;
import com.higgschain.trust.slave.api.vo.QueryAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class AccountInfoServiceImplTest extends BaseTest {

    @Autowired
    private AccountInfoService accountInfoService;

    @Test public void testQueryByAccountNos() throws Exception {

    }

    @Test public void testQueryAccountInfo() throws Exception {
        QueryAccountVO req = new QueryAccountVO();
        req.setAccountNo("");
        req.setDataOwner("TRUST-NODE97");
        PageVO<AccountInfoVO> pageVO = accountInfoService.queryAccountInfo(req);
        System.out.println("pageNo="+ pageVO.getPageNo() + "; pageSize=" + pageVO.getPageSize()
            + "; total=" + pageVO.getTotal());
        for (AccountInfoVO vo : pageVO.getData()) {
            System.out.println(vo);
        }
    }
}