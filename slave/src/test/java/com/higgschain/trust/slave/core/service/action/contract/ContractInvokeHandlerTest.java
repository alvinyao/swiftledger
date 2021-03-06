package com.higgschain.trust.slave.core.service.action.contract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.higgschain.trust.contract.JsonHelper;
import com.higgschain.trust.slave.IntegrateBaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.core.service.pack.PackageServiceImpl;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.bo.contract.ContractInvokeAction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Contract invoke handler test.
 */
public class ContractInvokeHandlerTest extends IntegrateBaseTest {

    @Autowired private ContractInvokeHandler invokeHandler;
    /**
     * The Snapshot.
     */
    @Autowired SnapshotService snapshot;
    /**
     * The Package service.
     */
    @Autowired PackageServiceImpl packageService;

    private ContractInvokeAction createContractInvokeAction() {
        ContractInvokeAction action = new ContractInvokeAction();
        action.setAddress("a7d0c2779d627cfc7e931c35060d1dcb6d5c63c13862323bedf2a4f3c352f956");
        //action.setMethod("main");
        action.setIndex(0);
        action.setType(ActionTypeEnum.TRIGGER_CONTRACT);
        return action;
    }

    /**
     * Test process.
     */
    @Test
    public void testProcess() {
        Action action = createContractInvokeAction();
        Action action2 = JsonHelper.clone(action, ContractInvokeAction.class);
        action2.setIndex(1);

        PackContext packContext = ActionDataMockBuilder.getBuilder()
                .createSignedTransaction(InitPolicyEnum.CONTRACT_ISSUE)
                .addAction(action)
                .addAction(action2)
                .setTxId(String.format("tx_id_invoke_contract_%s", System.currentTimeMillis()))
                .signature("", ActionDataMockBuilder.privateKey1)
                .signature("", ActionDataMockBuilder.privateKey2)
                .makeBlockHeader()
                .setBlockHeight(9)
                .build();

        packageService.process(packContext, true,false);

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //JSON不做循环引用检测
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        //JSON输出NULL属性
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
        //toJSONString的时候对一级key进行按照字母排序
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.SortField.getMask();
        //toJSONString的时候对嵌套结果进行按照字母排序
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.MapSortField.getMask();
        //toJSONString的时候记录Class的name
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteClassName.getMask();

        Action action = new ContractInvokeHandlerTest().createContractInvokeAction();
        JSONObject bizModel = new JSONObject();
        bizModel.put("data", action);
        PackContext packContext = ActionDataMockBuilder.getBuilder()
                .createSignedTransaction(InitPolicyEnum.REGISTER_POLICY)
                .addAction(action)
                .setBizModel(bizModel)
                .setTxId(String.format("tx_id_invoke_contract_%s", System.currentTimeMillis()))
//                .signature(ActionDataMockBuilder.privateKey1)
//                .signature(ActionDataMockBuilder.privateKey1)
//                .signature(ActionDataMockBuilder.privateKey1)
//                .signature(ActionDataMockBuilder.privateKey1)

                .signature("", ActionDataMockBuilder.privateKey1)
                .signature("", ActionDataMockBuilder.privateKey2)
                .makeBlockHeader()
                .build();

        System.out.println(JSON.toJSONString(packContext.getCurrentTransaction()));
    }
}