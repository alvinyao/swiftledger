package com.higgschain.trust.slave.core.service.action.contract;

import com.higgschain.trust.slave.IntegrateBaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import com.higgschain.trust.slave.core.service.snapshot.agent.AccountContractBindingSnapshotAgent;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.bo.contract.AccountContractBindingAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Account contract binding handler test.
 */
public class AccountContractBindingHandlerTest extends IntegrateBaseTest {

    /**
     * The Snapshot.
     */
    @Autowired SnapshotService snapshot;
    @Autowired private AccountContractBindingHandler actionHandler;
    @Autowired private AccountContractBindingSnapshotAgent agent;


    private AccountContractBindingAction createAction() {
        AccountContractBindingAction action = new AccountContractBindingAction();
        action.setAccountNo("zhangs");
        action.setContractAddress("895321051547e82e2018a204abe510e1b0e9a0843fd1ad4483a307d48bfe9754");
        action.setArgs("");
        action.setIndex(1);
        action.setType(ActionTypeEnum.BIND_CONTRACT);
        return action;
    }

    /**
     * Test validate.
     */
    @org.junit.Test
    public void testValidate() {
        Action action = createAction();
        PackContext packContext = ActionDataMockBuilder.getBuilder()
                .createSignedTransaction(InitPolicyEnum.REGISTER_POLICY)
                .addAction(action)
                .setTxId("00000000002")
                .signature("", "0x0000000000000000000000000")
                .makeBlockHeader()
                .build();


        snapshot.startTransaction();
        actionHandler.process(packContext);
        actionHandler.process(packContext);
        snapshot.commit();
    }

    /**
     * Test persist.
     */
    @org.junit.Test
    public void testPersist() {
        Action action = createAction();
        PackContext packContext = ActionDataMockBuilder.getBuilder()
                .createSignedTransaction(InitPolicyEnum.REGISTER_POLICY)
                .addAction(action)
                .setTxId("0000000000")
                .signature("", "0x0000000000000000000000000")
                .makeBlockHeader()
                .build();

        actionHandler.process(packContext);
    }
}