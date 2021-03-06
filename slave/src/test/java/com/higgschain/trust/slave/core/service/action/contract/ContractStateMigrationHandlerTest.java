package com.higgschain.trust.slave.core.service.action.contract;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.core.service.pack.PackageServiceImpl;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.bo.contract.ContractStateMigrationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Contract state migration handler test.
 *
 * @author duhongming
 * @date 2018 /6/21
 */
public class ContractStateMigrationHandlerTest extends BaseTest {
    @Autowired
    private ContractStateMigrationHandler stateMigrationHandler;
    /**
     * The Snapshot.
     */
    @Autowired
    SnapshotService snapshot;
    /**
     * The Package service.
     */
    @Autowired
    PackageServiceImpl packageService;

    private ContractStateMigrationAction createAction() {
        ContractStateMigrationAction action = new ContractStateMigrationAction();
        action.setFormInstanceAddress("a5a28246eb54a9529c7c129075e760e1451a21d3f76f8aac52a3cf0f7d5d15f3");
        action.setToInstanceAddress("8a2ad8b7392a32ff072f12a1e0e9f11233fa4fe431330c16bb51861a50e8f12b");
        action.setIndex(0);
        action.setType(ActionTypeEnum.CONTRACT_STATE_MIGRATION);
        return action;
    }

    /**
     * Test process.
     */
    @Test
    public void testProcess() {
        Action action = createAction();
        PackContext packContext = ActionDataMockBuilder.getBuilder()
                .createSignedTransaction(InitPolicyEnum.CONTRACT_ISSUE)
                .addAction(action)
                .setTxId(String.format("0x0000MIGRATIONSTATE_%s", System.currentTimeMillis()))
                .signature("", ActionDataMockBuilder.privateKey1)
                .signature("", ActionDataMockBuilder.privateKey2)
                .makeBlockHeader()
                .setBlockHeight()
                .build();

        packageService.process(packContext, true, false);

    }
}
