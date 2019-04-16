package com.higgschain.trust.rs.core.integration;

import com.higgschain.trust.slave.api.vo.CaVO;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.ca.Ca;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * The interface Ca client.
 *
 * @author WangQuanzhou
 * @date 2018 /6/5 16:59
 */
//@FeignClient("${higgs.trust.prefix}")
public interface CaClient {

    /**
     * send ca auth request
     *
     * @param nodeNameReg the node name reg
     * @param list        the list
     * @return resp data
     */
    @RequestMapping(value = "/ca/auth", method = RequestMethod.POST) RespData<String> caAuth(String nodeNameReg, @RequestBody List<CaVO> list);

    /**
     * Acquire ca resp data.
     *
     * @param nodeNameReg the node name reg
     * @param user        the user
     * @return resp data
     * @desc send acqurie ca  request
     */
    @RequestMapping(value = "/ca/get", method = RequestMethod.POST)
    RespData<Ca> acquireCA(String nodeNameReg, @RequestParam("user") String user);

    /**
     * Sync cluster resp data.
     *
     * @param nodeNameReg the node name reg
     * @return resp data
     * @desc send acqurie ca  request
     */
    @RequestMapping(value = "/ca/sync", method = RequestMethod.POST)
    RespData<Map> syncCluster(String nodeNameReg);
}
