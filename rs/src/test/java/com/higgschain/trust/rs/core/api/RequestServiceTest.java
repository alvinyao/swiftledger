package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.IntegrateBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Request service test.
 *
 * @description: com.higgschain.trust.rs.core.api
 * @author: lingchao
 * @datetime:2018年12月03日16:20
 */
public class RequestServiceTest extends IntegrateBaseTest {
    @Autowired
    private RequestService requestService;

    /**
     * Test.
     */
    @Test
    public void test() {
        String requestId = System.currentTimeMillis() + "";
     //   System.out.println(requestService.insertRequest(requestId, RequestEnum.PROCESS, "000001", "msg"));
     //   System.out.println(requestService.insertRequest(requestId, RequestEnum.PROCESS, "000001", "msg"));
        System.out.println(requestService.queryByRequestId(requestId));
        System.out.println(requestService.queryByRequestId(requestId+"11"));
        System.out.println(requestService.requestIdempotent(requestId));
        System.out.println(requestService.requestIdempotent(requestId+"1"));

    }

}