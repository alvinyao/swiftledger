package commands

import com.higgschain.trust.common.utils.LogLevelChanger
import com.higgschain.trust.config.view.ClusterViewManager
import com.higgschain.trust.consensus.config.NodeState
import com.higgschain.trust.consensus.config.NodeStateEnum
import com.higgschain.trust.consensus.core.ConsensusStateMachine
import com.higgschain.trust.slave.core.repository.PackageRepository
import com.higgschain.trust.slave.core.service.block.BlockService
import com.higgschain.trust.slave.core.service.consensus.cluster.IClusterService
import com.higgschain.trust.slave.core.service.consensus.view.ClusterViewService
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.time.DateFormatUtils
import org.crsh.cli.*
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory

/*
 * Copyright (c) 2013-2017, suimi
 */

/**
 *  The type Node.
 */
@Slf4j
@Usage("about node info")
class node {

    @Usage('show the node info')
    @Command
    def info(InvocationContext context, @Usage("show the info until the end") @Option(names = ["t"]) Boolean isTill) {
        if (isTill) {
            while (true) {
                printInfo(context)
                context.flush()
                Thread.sleep(1000)
            }
        } else {
            printInfo(context)
        }
    }

    def printInfo(InvocationContext context) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def nodeState = beans.getBean(NodeState.class)
        def blockService = beans.getBean(BlockService.class)
        def packageRepository = beans.getBean(PackageRepository.class)
        context.provide([Name: "Name", Value: nodeState.nodeName])
        context.provide([Name: "Master", Value: nodeState.masterName])
        context.provide([Name: "isMaster", Value: nodeState.master])
        context.provide([Name: "State", Value: nodeState.state])
        context.provide([Name: "Term", Value: nodeState.getCurrentTerm()])
        context.provide([Name: "Block Height", Value: blockService.getMaxHeight().toString()])
        context.provide([Name: "Package Height", Value: packageRepository.getMaxHeight().toString()])
        context.provide([Name: "Time", Value: DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")])
        out.println("")
    }

    @Usage('show the current state of node')
    @Command
    def state(InvocationContext context,@Option(names = ["a", "all"]) Boolean isAll) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        if(isAll){
            def clusterService = beans.getBean(IClusterService.class)
            def map = clusterService.getAllClusterState()
            map.entrySet().forEach({ entry -> context.provide([name: entry.key, value: entry.value]) })
        }else{
            def nodeState = beans.getBean(NodeState.class)
            out.println("Node is $nodeState.state")
        }
    }


    @Usage('show the current height of node')
    @Command
    def height(InvocationContext context,
               @Usage("will show the cluster height")
               @Option(names = ["c", "cluster"]) Boolean isCluster, @Option(names = ["a", "all"]) Boolean isAll) {

        BeanFactory beans = context.attributes['spring.beanfactory']
        if (isAll) {
            def clusterService = beans.getBean(IClusterService.class)
            def map = clusterService.getAllClusterHeight()
            map.entrySet().forEach({ entry -> context.provide([name: entry.key, value: entry.value]) })
            return
        }
        def height
        if (isCluster) {
            def clusterService = beans.getBean(IClusterService.class)
            height = clusterService.getClusterHeight(1)
            if (height == null) {
                out.println("Failed to get cluster height, please try again")
                return
            }
            out.println("The cluster block height is $height")
        } else {
            def blockService = beans.getBean(BlockService.class)
            height = blockService.getMaxHeight().toString()
            out.println("The block height is $height")
        }
    }

    @Usage('change the state of node')
    @Command
    def changeState(InvocationContext context, @Usage("from state")
    @Required @Argument NodeStateEnum from, @Usage("to state") @Required @Argument NodeStateEnum to) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def nodeState = beans.getBean(NodeState.class)
        nodeState.changeState(from, to)
        out.println("State changed to $nodeState.state")
    }


    @Usage('start consensus layer')
    @Command
    def startConsensus(InvocationContext context) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def consensusStateMachine = beans.getBean(ConsensusStateMachine.class)
        consensusStateMachine.start()
        out.println("start consensus successful")
    }

    @Usage('show the views of cluster')
    @Command
    def views(InvocationContext context) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def viewManager = beans.getBean(ClusterViewManager.class)
        viewManager.getViews().forEach({ v -> context.provide(View: v.id, FaultNum: v.faultNum, StartHeight: v.startHeight, EndHeight: v.endHeight, NodeNames: v.nodeNames) })
        out.println("")
    }

    @Usage('refresh the cluster view')
    @Command
    def refreshView(InvocationContext context,
                    @Usage("init from cluster") @Option(names = ["c"]) Boolean isCluster,@Usage("use db.height + 1") @Option(names = ["h"]) Boolean height) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def clusterInfoService = beans.getBean(ClusterViewService.class)
        if (isCluster) {
            clusterInfoService.initClusterViewFromCluster()
        } else {
            clusterInfoService.initClusterViewFromDB(height)
        }
        out.println("refresh cluster view successful")
    }

    @Usage('change log level, log <logName> <[OFF|ERROR|WARN|INFO|DEBUG|TRACE|ALL]>')
    @Command
    def log(InvocationContext context,
                       @Usage("log name") @Required @Argument String logName,
                       @Usage("level") @Required @Argument String level) {
        LogLevelChanger.change(logName, level);
    }

}
