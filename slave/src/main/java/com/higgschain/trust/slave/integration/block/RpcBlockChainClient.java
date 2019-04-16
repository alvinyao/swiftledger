package com.higgschain.trust.slave.integration.block;

import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.network.HttpClient;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.slave.api.BlockChainService;
import com.higgschain.trust.slave.api.rpc.request.BlockRequest;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.api.vo.TransactionVO;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The type Rpc block chain client.
 *
 * @author duhongming
 * @date 2018 /9/18
 */
@ConditionalOnProperty(name = "network.rpc", havingValue = "netty", matchIfMissing = true)
@Component
@Slf4j
public class RpcBlockChainClient implements BlockChainClient {

    private static final String ACTION_TYPE_BLOCK_HEADER_GET = "block/header/get";
    private static final String ACTION_TYPE_BLOCK_GET = "block/get";


    @Autowired
    private IClusterViewManager viewManager;

    @Autowired
    private NetworkManage networkManage;

    @Autowired
    private BlockChainService blockChainService;

    private static Type respDataListType;

    static {
        try {
            respDataListType = (new Object() {
                public RespData<List<TransactionVO>> respDataList;
            }).getClass().getField("respDataList").getGenericType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiates a new Rpc block chain client.
     */
    public RpcBlockChainClient() {
        log.info("Use RpcBlockChainClient");
    }

    @Override
    public List<BlockHeader> getBlockHeaders(String nodeNameReg, long startHeight, int size) {
        BlockRequest request = new BlockRequest(startHeight, size);
        List<String> names = viewManager.getCurrentView().getNodeNames();
        if (names.size() == 1) {
            return blockChainService.listBlockHeaders(startHeight, size);
        }
        return networkManage.rpcClient().randomSendAndReceive(names, ACTION_TYPE_BLOCK_HEADER_GET, request);
    }

    @Override
    public List<BlockHeader> getBlockHeadersFromNode(String nodeName, long startHeight, int size) {
        BlockRequest request = new BlockRequest(startHeight, size);
        return networkManage.rpcClient().sendAndReceive(nodeName, ACTION_TYPE_BLOCK_HEADER_GET, request);
    }

    @Override
    public List<Block> getBlocks(String nodeNameReg, long startHeight, int size) {
        BlockRequest request = new BlockRequest(startHeight, size);
        List<String> names = viewManager.getCurrentView().getNodeNames();
        if (names.size() == 1) {
            return blockChainService.listBlocks(startHeight, size);
        }
        return networkManage.rpcClient().randomSendAndReceive(names, ACTION_TYPE_BLOCK_GET, request);
    }

    @Override
    public List<Block> getBlocksFromNode(String nodeName, long startHeight, int size) {
        BlockRequest request = new BlockRequest(startHeight, size);
        return networkManage.rpcClient().sendAndReceive(nodeName, ACTION_TYPE_BLOCK_GET, request);
    }

    @Override
    public RespData<List<TransactionVO>> submitToMaster(String nodeName, List<SignedTransaction> transactions) {
        String url = "/transaction/master/submit";
        HttpClient httpClient = NetworkManage.getInstance().httpClient();
        return httpClient.postJson(nodeName, url, transactions, respDataListType);
    }

}
