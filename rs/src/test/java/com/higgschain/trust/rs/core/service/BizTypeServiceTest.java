package com.higgschain.trust.rs.core.service;

import com.higgschain.trust.IntegrateBaseTest;
import com.higgschain.trust.rs.core.api.BizTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * @author liuyu
 * @description
 * @date 2018-06-13
 */
public class BizTypeServiceTest extends IntegrateBaseTest {
    @Autowired BizTypeService bizTypeService;

    @Test
    public void test(){
        String a = bizTypeService.getByPolicyId("abc");
        System.out.println(a);
    }
}
