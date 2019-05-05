package com.higgschain.trust.rs.core.service;

import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.core.ConsensusStateMachine;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.network.Peer;
import com.higgschain.trust.rs.core.api.CaService;
import com.higgschain.trust.rs.core.api.CoreTransactionService;
import com.higgschain.trust.rs.core.api.SignService;
import com.higgschain.trust.rs.core.integration.NodeClient;
import com.higgschain.trust.rs.core.vo.NodeOptVO;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.RespCodeEnum;
import com.higgschain.trust.slave.api.enums.TxTypeEnum;
import com.higgschain.trust.slave.api.enums.VersionEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.core.repository.config.ConfigRepository;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignInfo;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.ca.Ca;
import com.higgschain.trust.slave.model.bo.config.Config;
import com.higgschain.trust.slave.model.bo.node.NodeAction;
import com.higgschain.trust.slave.model.enums.UsageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The type Node consensus service.
 *
 * @author WangQuanzhou
 * @desc node consensus service
 * @date 2018 /7/5 11:38
 */
@Service
@Slf4j
public class NodeConsensusService {

    @Autowired
    private NodeState nodeState;
    @Autowired
    private CoreTransactionService coreTransactionService;
    @Autowired
    private NodeClient nodeClient;
    @Autowired
    private ConsensusStateMachine consensusStateMachine;
    @Autowired
    private SignService signService;
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private CaService caService;

    private static final String SUCCESS = "sucess";
    private static final String FAIL = "fail";

    /**
     * Join request string.
     *
     * @return the string
     */
    public String joinRequest() {
        log.info("[joinRequest] send ca auth request");
        RespData<Ca> caRespData = caService.acquireCA(nodeState.getNodeName());
        if (caRespData == null || caRespData.getData() == null || !caRespData.getData().isValid()) {
            log.error("[joinRequest] current node's ca is not exist or invalid");
            return "current node's ca is not exist or invalid";
        }
        log.info("[joinRequest] end send ca auth request");

        log.info("[joinRequest] send join consensus request");
        String nodeName = nodeState.getNodeName();
        NodeOptVO vo = new NodeOptVO();
        vo.setNodeName(nodeName);
        //add pubKey
        Config config = configRepository.getConfig(nodeName, UsageEnum.CONSENSUS);
        String pubKey = config.getPubKey();
        String signValue = nodeName + "-" + pubKey;
        String sign = signService.sign(signValue, SignInfo.SignTypeEnum.CONSENSUS);
        vo.setPubKey(pubKey);
        vo.setSign(sign);
        vo.setSignValue(signValue);
        Optional<Peer> peerOptional = NetworkManage.getInstance().getAnyMasterPeerExclude(nodeName);
        if (!peerOptional.isPresent()) {
            log.error("[joinRequest] alive peer to send join request not found");
            return FAIL;
        }
        RespData respData = nodeClient.nodeJoin(peerOptional.get().getNodeName(), vo);
        if (!respData.isSuccess()) {
            log.error("resp = {}", respData);
            return FAIL;
        }
        log.info("[joinRequest] end send join consensus request");
        return SUCCESS;
    }

    /**
     * Join consensus string.
     *
     * @param
     * @return string
     * @desc join consensus layer
     */
    public String joinConsensus() {
        log.info("[joinConsensus] start to join consensus layer");

        try {
            consensusStateMachine.joinConsensus();
        } catch (Throwable e) {
            log.error("[joinConsensus] join consensus error", e);
            nodeState.changeState(nodeState.getState(), NodeStateEnum.Offline);
            return FAIL;
        }
        log.info("[joinConsensus] end join consensus layer");
        return SUCCESS;
    }

    /**
     * process join request
     *
     * @param vo the vo
     * @return resp data
     */
    public RespData joinConsensusTx(NodeOptVO vo) {
        //send and get callback result
        try {
            coreTransactionService.submitTx(constructJoinCoreTx(vo));
        } catch (Throwable e) {
            log.error("send node join transaction error", e);
            return new RespData(RespCodeEnum.SYS_FAIL.getRespCode(), RespCodeEnum.SYS_FAIL.getMsg());
        }
        log.info("[joinConsensusTx] submit joinConsensusTx to slave success");
        return new RespData();
    }

    /**
     * make core transaction
     *
     * @param vo
     * @return
     */
    private CoreTransaction constructJoinCoreTx(NodeOptVO vo) {
        CoreTransaction coreTx = new CoreTransaction();
        coreTx.setTxId(UUID.randomUUID().toString());
        coreTx.setSender(nodeState.getNodeName());
        coreTx.setVersion(VersionEnum.V1.getCode());
        coreTx.setPolicyId(InitPolicyEnum.NODE_JOIN.getPolicyId());
        coreTx.setActionList(buildJoinActionList(vo));
        //set transaction type
        coreTx.setTxType(TxTypeEnum.NODE.getCode());
        coreTx.setSendTime(new Date());
        return coreTx;
    }

    /**
     * make action
     *
     * @param vo
     * @return
     */
    private List<Action> buildJoinActionList(NodeOptVO vo) {
        List<Action> actions = new ArrayList<>();
        NodeAction nodeAction = new NodeAction();
        nodeAction.setType(ActionTypeEnum.NODE_JOIN);
        nodeAction.setIndex(0);
        nodeAction.setNodeName(vo.getNodeName());
        nodeAction.setSelfSign(vo.getSign());
        nodeAction.setSignValue(vo.getSignValue());
        nodeAction.setPubKey(vo.getPubKey());
        actions.add(nodeAction);
        return actions;
    }

    /**
     * Leave consensus string.
     *
     * @param
     * @return string
     * @desc process leave consensus layer
     */
    public String leaveConsensus() {

        //send and get callback result
        try {
            coreTransactionService.submitTx(constructLeaveCoreTx(nodeState.getNodeName()));
        } catch (Throwable e) {
            log.error("send node leave transaction error", e);
            return FAIL;
        }
        log.info("[leaveConsensus] submit leaveConsensusTx to slave success");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("[leaveConsensus] error occured while thread sleep", e);
            return FAIL;
        }
        log.info("[leaveConsensus] end leave consensus layer and transform node status");
        return SUCCESS;

    }

    /**
     * make core transaction
     *
     * @param nodeName
     * @return
     */
    private CoreTransaction constructLeaveCoreTx(String nodeName) {
        CoreTransaction coreTx = new CoreTransaction();
        coreTx.setTxId(UUID.randomUUID().toString());
        coreTx.setSender(nodeName);
        coreTx.setVersion(VersionEnum.V1.getCode());
        coreTx.setPolicyId(InitPolicyEnum.NODE_LEAVE.getPolicyId());
        coreTx.setActionList(buildLeaveActionList(nodeName));
        //set transaction type
        coreTx.setTxType(TxTypeEnum.NODE.getCode());
        coreTx.setSendTime(new Date());
        return coreTx;
    }

    /**
     * make action
     *
     * @param nodeName
     * @return
     */
    private List<Action> buildLeaveActionList(String nodeName) {
        List<Action> actions = new ArrayList<>();
        NodeAction nodeAction = new NodeAction();
        nodeAction.setType(ActionTypeEnum.NODE_LEAVE);
        nodeAction.setIndex(0);
        nodeAction.setNodeName(nodeName);
        //add pubKey
        Config config = configRepository.getConfig(nodeName, UsageEnum.CONSENSUS);
        String pubKey = config.getPubKey();
        String signValue = nodeName + "-" + pubKey;
        String sign = signService.sign(signValue, SignInfo.SignTypeEnum.CONSENSUS);
        nodeAction.setPubKey(pubKey);
        nodeAction.setSelfSign(sign);
        nodeAction.setSignValue(signValue);
        actions.add(nodeAction);
        return actions;
    }

}
