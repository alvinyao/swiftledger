package com.higgschain.trust.rs.core.repository;

import com.higgschain.trust.IntegrateBaseTest;
import com.higgschain.trust.rs.common.enums.RequestEnum;
import com.higgschain.trust.rs.core.dao.rocks.RequestRocksDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Request repository test.
 */
public class RequestRepositoryTest extends IntegrateBaseTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestRocksDao requestRocksDao;

    /**
     * Test.
     */
    @Test
    public void test() {
        String requestId = System.currentTimeMillis() + "";
     //   System.out.println("add:" + requestRepository.insertRequest(requestId, RequestEnum.PROCESS, "000001", "msg"));
        System.out.println("query: " + requestRepository.queryByRequestId(requestId));
        System.out.println("requestIdempotent:" + requestRepository.requestIdempotent(requestId));
        System.out.println("query: " + requestRepository.queryByRequestId(requestId + "-"));
        requestRepository.updateCode(requestId, "000002", "");
        System.out.println("query: " + requestRepository.queryByRequestId(requestId));
        requestRepository.updateCode(requestId, "000003", "ooo");
        System.out.println("query: " + requestRepository.queryByRequestId(requestId));
        requestRepository.updateStatusAndCode(requestId, RequestEnum.PROCESS, RequestEnum.DONE,"000004", "ok");
        System.out.println("query: " + requestRepository.queryByRequestId(requestId));
    }

    /**
     * Request idempotent.
     */
    @Test
    public void requestIdempotent(){
        System.out.println("requestIdempotent:" + requestRepository.requestIdempotent("12321"));
    }
}