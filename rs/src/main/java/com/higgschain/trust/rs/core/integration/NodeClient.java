package com.higgschain.trust.rs.core.integration;

import com.higgschain.trust.rs.core.vo.NodeOptVO;
import com.higgschain.trust.common.vo.RespData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Node client.
 *
 * @author WangQuanzhou
 * @date 2018 /6/5 16:59
 */
//@FeignClient("${higgs.trust.prefix}")
public interface NodeClient {

    /**
     * Node join resp data.
     *
     * @param nodeName the node name
     * @param vo       the vo
     * @return resp data
     * @desc send node join request
     */
    @RequestMapping(value = "/node/join", method = RequestMethod.POST)
    RespData<String> nodeJoin(String nodeName, @RequestBody NodeOptVO vo);
}
