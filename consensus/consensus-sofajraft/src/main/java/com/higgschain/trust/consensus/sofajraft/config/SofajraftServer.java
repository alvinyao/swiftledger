package com.higgschain.trust.consensus.sofajraft.config;

import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.jraft.CliService;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.StateMachine;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.impl.cli.BoltCliClientService;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.core.ConsensusClient;
import com.higgschain.trust.consensus.core.ConsensusStateMachine;
import com.higgschain.trust.consensus.listener.StateChangeListener;
import com.higgschain.trust.consensus.listener.StateListener;
import com.higgschain.trust.consensus.sofajraft.rpc.ConsensusCommandProcessor;
import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@StateListener
public class SofajraftServer implements ConsensusClient, ConsensusStateMachine {

    @Autowired
    private SofajraftProperties properties;

    @Autowired
    private StateMachine stateMachine;

    @Autowired
    private BoltCliClientService cliClientService;

    @Autowired
    private CliService cliService;

    @Autowired
    private PeerId serverId;

    @Autowired
    private Configuration initConf;

    private RaftGroupService raftGroupService;

    @Override
    public <T> CompletableFuture<?> submit(AbstractConsensusCommand<T> command) {
        PeerId peerId = raftGroupService.getRaftNode().getLeaderId();
        CompletableFuture future = new CompletableFuture();
        try {
            cliClientService.getRpcClient().invokeWithCallback(peerId.getEndpoint().toString(), command, new InvokeCallback() {

                @Override
                public void onResponse(Object result) {
                    future.complete(null);
                }

                @Override
                public void onException(Throwable e) {
                    future.completeExceptionally(e);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            }, 1000);
        } catch (RemotingException e) {
            throw new RuntimeException("submit to sofajraft failed", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("submit to sofajraft failed", e);
        }
        return future;
    }

    @StateChangeListener(NodeStateEnum.StartingConsensus)
    @Order
    @Override
    public void start() {
        if (raftGroupService == null) {

            // 初始化路径
            try {
                FileUtils.forceMkdir(new File(properties.getDataPath()));
            } catch (IOException e) {
                throw new RuntimeException("start sofa jraft server, but make data path directory failed!", e);
            }

            final NodeOptions nodeOptions = new NodeOptions();
            nodeOptions.setElectionTimeoutMs(properties.getElectionTimeoutMs());
            nodeOptions.setSnapshotIntervalSecs(properties.getSnapshotIntervalSecs());
            // 设置初始集群配置
            nodeOptions.setInitialConf(initConf);

            // 这里让 raft RPC 和业务 RPC 使用同一个 RPC server, 通常也可以分开
            final RpcServer rpcServer = new RpcServer(serverId.getPort());
            RaftRpcServerFactory.addRaftRequestProcessors(rpcServer);
            // 注册业务处理器
            registProcessors(rpcServer);
            // 设置状态机到启动参数
            nodeOptions.setFsm(stateMachine);
            // 设置存储路径
            // 日志, 必须
            nodeOptions.setLogUri(properties.getDataPath() + File.separator + "log");
            // 元信息, 必须
            nodeOptions.setRaftMetaUri(properties.getDataPath() + File.separator + "raft_meta");
            // snapshot, 可选, 一般都推荐
            nodeOptions.setSnapshotUri(properties.getDataPath() + File.separator + "snapshot");
            // 初始化 raft group 服务框架
            this.raftGroupService = new RaftGroupService(properties.getGroupId(), serverId, nodeOptions, rpcServer);
            // 启动
            this.raftGroupService.start();
        }
    }

    private void registProcessors(RpcServer rpcServer) {
        Reflections reflections = new Reflections("com.higgschain.trust");
        ConsensusCommandProcessor processor = new ConsensusCommandProcessor(this);
        Set<Class<? extends AbstractConsensusCommand>> subTypes = reflections.getSubTypesOf(AbstractConsensusCommand.class);
        for (Class clazz : subTypes) {
            processor.setInterest(clazz.getName());
            rpcServer.registerUserProcessor(processor);
        }
    }

    @Override
    public void leaveConsensus() {
        cliService.removePeer(properties.getGroupId(), initConf, serverId);
    }

    @Override
    public void joinConsensus() {
        cliService.addPeer(properties.getGroupId(), initConf, serverId);
    }

    public RaftGroupService getRaftGroupService() {
        return raftGroupService;
    }
}
