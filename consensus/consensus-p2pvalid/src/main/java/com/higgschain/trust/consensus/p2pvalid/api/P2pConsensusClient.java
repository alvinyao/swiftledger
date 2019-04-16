package com.higgschain.trust.consensus.p2pvalid.api;

import com.higgschain.trust.consensus.p2pvalid.core.ResponseCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommandWrap;
import com.higgschain.trust.consensus.p2pvalid.core.ValidResponseWrap;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The interface P 2 p consensus client.
 */
//@FeignClient(name = "${higgs.trust.prefix}")
public interface P2pConsensusClient {
    /**
     * Send valid response wrap.
     *
     * @param nodeName         the node name
     * @param validCommandWrap the valid command wrap
     * @return the valid response wrap
     */
    //    @RequestMapping(value = "/consensus/p2p/receive_command", method = RequestMethod.POST) @ResponseBody
    ValidResponseWrap<ResponseCommand> send(String nodeName, @RequestBody ValidCommandWrap validCommandWrap);

    /**
     * Sync send valid response wrap.
     *
     * @param nodeName         the node name
     * @param validCommandWrap the valid command wrap
     * @return the valid response wrap
     */
    //    @RequestMapping(value = "/consensus/p2p/receive_command_sync", method = RequestMethod.POST) @ResponseBody
    ValidResponseWrap<ResponseCommand> syncSend(String nodeName, @RequestBody ValidCommandWrap validCommandWrap);

    /**
     * Sync send feign valid response wrap.
     *
     * @param nodeNameReg      the node name reg
     * @param validCommandWrap the valid command wrap
     * @return the valid response wrap
     */
    //    @RequestMapping(value = "/consensus/p2p/receive_command_sync", method = RequestMethod.POST) @ResponseBody
    ValidResponseWrap<ResponseCommand> syncSendFeign(String nodeNameReg, @RequestBody ValidCommandWrap validCommandWrap);
}
