package com.higgschain.trust.rs.core.integration;

import com.higgschain.trust.network.HttpClient;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.rs.core.vo.NodeOptVO;
import com.higgschain.trust.common.vo.RespData;
import org.springframework.stereotype.Component;

/**
 * The type Http node client.
 *
 * @author duhongming
 * @date 2018 /9/14
 */
@Component
public class HttpNodeClient implements NodeClient {
    @Override
    public RespData<String> nodeJoin(String nodeName, NodeOptVO vo) {
        String url = "/node/join";
        HttpClient httpClient = NetworkManage.getInstance().httpClient();
        return httpClient.postJson(nodeName, url, vo, RespData.class);
    }
}
