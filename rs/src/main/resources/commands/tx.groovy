package commands


import com.higgschain.trust.rs.core.api.CoreTransactionService
import com.higgschain.trust.rs.core.vo.RsCoreTxVO
import com.higgschain.trust.slave.core.repository.PolicyRepository
import com.higgschain.trust.slave.model.bo.action.Action
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.time.DateFormatUtils
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Required
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory

/*
 * suimi
 */

@Slf4j
@Usage("about tx info operation")
class tx {

    @Usage('get the tx info')
    @Command
    def info(InvocationContext context, @Usage('the tx id') @Required @Argument String txId) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def coreTxService = beans.getBean(CoreTransactionService.class)
        def coreTx = coreTxService.queryCoreTx(txId)
        if (coreTx == null) {
            out.println("The tx id is invalid")
        } else {
            printTxDetail(context, coreTx)
        }
    }

    def printTxDetail(InvocationContext context, RsCoreTxVO ctx) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def policyRepository = beans.getBean(PolicyRepository.class)
        context.provide("TxInfo": "TxID", "": ctx.txId)
        context.provide("Name": "Version", "value": ctx.version)
        context.provide(["Name": "PolicyId", "Value": ctx.policyId])
        context.provide(["Name": "PolicyType", "Value": policyRepository.getPolicyType(ctx.getPolicyId())])
        context.provide(["Name": "TxType", "Value": ctx.txType])
        context.provide(["Name": "Sender", "Value": ctx.sender])
        context.provide(["Name": "SendTime", "Value": DateFormatUtils.format(ctx.sendTime, "yyyy-MM-dd HH:mm:ss.SSS")])
        if(ctx.bizModel!=null){
            context.provide(["Name": "BizModel", "Value": ctx.bizModel])
        }
        context.provide(["Name": "Status", "Value": ctx.status])
        context.provide(["Name": "Result", "Value": ctx.executeResult])
        context.provide(["Name": "ErrorCode", "Value": ctx.errorCode])
        context.provide(["Name": "ErrorMsg", "Value": ctx.errorMsg])
        out.println("")
        ctx.getActionList().forEach({ a -> printAction(context, a,) })
    }

    def printAction(InvocationContext context, Action action) {
        context.provide(["\tAction": "\tActionType", "": action.type])
        context.provide(["Name": "\tIndex", "Value": action.index])
        context.provide(["Name": "\tInfo", "Value": action.toString()])
        out.println("")
    }


}
