package commands

import com.higgschain.trust.consensus.config.NodeState
import com.higgschain.trust.consensus.config.NodeStateEnum
import com.higgschain.trust.management.failover.service.StandbyService
import lombok.extern.slf4j.Slf4j
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory

/**
 * standby groovy
 *
 * @author lingchao
 * @create 2018年10月09日10:24
 */
/**
 *  The type Standby.
 */
@Slf4j
@Usage("set standby")
class standby {
    @Usage('set Running')
    @Command
    def setRunning(InvocationContext context) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def nodeState = beans.getBean(NodeState.class)
        def standbyService = beans.getBean(StandbyService.class)
        if (nodeState.state != NodeStateEnum.Standby) {
            out.println("Node state is not Standby but $nodeState.state, can't set standby")
            return
        }
        standbyService.startOrResume()
        nodeState.changeState(NodeStateEnum.Standby, NodeStateEnum.Offline)
        nodeState.changeState(NodeStateEnum.Offline, NodeStateEnum.Initialize)
        nodeState.changeState(NodeStateEnum.Initialize, NodeStateEnum.StartingConsensus)
        nodeState.changeState(NodeStateEnum.StartingConsensus, NodeStateEnum.SelfChecking)
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.AutoSync)
        nodeState.changeState(NodeStateEnum.AutoSync, NodeStateEnum.Running)

        //restart network
        standbyService.restartNetwork()
        out.println("Standby set finish, now the state is : $nodeState.state")
    }
}
