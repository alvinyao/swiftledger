package com.higgschain.trust.consensus.sofajraft.example;

import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.impl.cli.BoltCliClientService;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

public class ExampleClient {
    public static void main(String[] args) throws TimeoutException, InterruptedException, RemotingException {
        String confStr = "127.0.0.1:9000,127.0.0.1:9001,127.0.0.1:9002";
        final Configuration conf = new Configuration();
        if (!conf.parse(confStr)) {
            throw new IllegalArgumentException("Fail to parse conf:" + confStr);
        }

        String groupId = "trust";

        RouteTable.getInstance().updateConfiguration(groupId, conf);

        final BoltCliClientService cliClientService = new BoltCliClientService();
        cliClientService.init(new CliOptions());

        if (!RouteTable.getInstance().refreshLeader(cliClientService, groupId, 1000).isOk()) {
            throw new IllegalStateException("Refresh leader failed");
        }

        final PeerId leader = RouteTable.getInstance().selectLeader(groupId);
        System.out.println("Leader is " + leader);

        final ExampleCommand command = new ExampleCommand("test", 10);
        command.setTraceId(12345L);
        cliClientService.getRpcClient().invokeWithCallback(leader.getEndpoint().toString(), command,
                new InvokeCallback() {

                    @Override
                    public void onResponse(Object result) {
                        System.out.println("incrementAndGet result:" + result);
                    }

                    @Override
                    public void onException(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public Executor getExecutor() {
                        return null;
                    }
                }, 5000);
    }
}
