package com.higgschain.trust.slave.core.service.action.account;

import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.slave._interface.InterfaceCommonTest;
import com.higgschain.trust.slave.core.repository.PolicyRepository;
import com.higgschain.trust.slave.core.service.snapshot.agent.ManageSnapshotAgent;
import com.higgschain.trust.slave.model.bo.account.AccountFreeze;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * The type Account freeze handler test.
 *
 * @author hanson
 * @Date 2018 /4/28
 * @Description:
 */
public class AccountFreezeHandlerTest extends InterfaceCommonTest {

    private final static String rootPath = "java/com/higgs/trust/slave/core/service/accounting/freezeAccount/";

    /**
     * The Account freeze handler.
     */
    @Autowired
    AccountFreezeHandler accountFreezeHandler;

    /**
     * The Open account handler.
     */
    @Autowired
    OpenAccountHandler openAccountHandler;

    /**
     * The Account operation handler.
     */
    @Autowired
    AccountOperationHandler accountOperationHandler;
    /**
     * The Manage snapshot agent.
     */
    @Autowired
    ManageSnapshotAgent manageSnapshotAgent;

    /**
     * The Policy repository.
     */
    @Autowired
    PolicyRepository policyRepository;

    @Override
    protected String getProviderRootPath() {
        return rootPath;
    }

    /**
     * Param validate.
     *
     * @param param the param
     * @throws Exception the exception
     */
    @Test(dataProvider = "defaultProvider", priority = 0)
    public void paramValidate(Map<?, ?> param) throws Exception {
        AccountFreeze freeze = getBodyData(param, AccountFreeze.class);
        executeActionHandler(param, accountFreezeHandler, freeze);
    }

    /**
     * Test exception.
     *
     * @param param the param
     * @throws Exception the exception
     */
    @Test(dataProvider = "defaultProvider", priority = 1)
    public void testException(Map<?, ?> param) throws Exception {
        executeBeforeSql(param);
        AccountFreeze freeze = getBodyData(param, AccountFreeze.class);
        executeActionHandler(param, accountFreezeHandler, freeze);
        executeAfterSql(param);

    }

    /**
     * Test regular.
     *
     * @param param the param
     * @throws Exception the exception
     */
    @Test(dataProvider = "defaultProvider", priority = 2)
    public void testRegular(Map<?, ?> param) throws Exception {
        JSONObject object = (JSONObject)param.get("body");

        AccountFreeze freeze = getObject(object.get("freeze").toString(), AccountFreeze.class);
//        OpenAccount creditAccount = getObject(object.get("credit").toString(), OpenAccount.class);
//        OpenAccount debitAccount = getObject(object.get("debit").toString(), OpenAccount.class);
//        AccountOperation accountOperation = getObject(object.get("accounting").toString(), AccountOperation.class);
//        String policyId = object.getString("policyId");

        executeBeforeSql(param);

//        openAccountHandler.validate(makePackContext(creditAccount, 1L));
//        openAccountHandler.persist(makePackContext(creditAccount, 1L));
//        openAccountHandler.validate(makePackContext(debitAccount, 1L));
//        openAccountHandler.persist(makePackContext(debitAccount, 1L));
//
//        PackContext packContext = makePackContext(accountOperation, 1L);
//        packContext.getCurrentTransaction().getCoreTx().setPolicyId(policyId);
//
//        accountOperationHandler.validate(packContext);
//        accountOperationHandler.persist(packContext);
        PackContext packContext = makePackContext(freeze, 1L,param);

      //  accountFreezeHandler.validate(packContext);
      //  accountFreezeHandler.persist(packContext);

//        accountFreezeHandler.validate(makePackContext(freeze, 1L));
//        accountFreezeHandler.persist(makePackContext(freeze, 1L));
        executeAfterSql(param);

    }

}