package com.higgschain.trust.slave.core.service.action.contract;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.common.utils.Profiler;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.core.repository.contract.ContractRepository;
import com.higgschain.trust.slave.core.service.contract.DbContractStateStoreImpl;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import com.higgschain.trust.slave.core.service.snapshot.agent.ContractSnapshotAgent;
import com.higgschain.trust.slave.core.service.snapshot.agent.ContractStateSnapshotAgent;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.bo.contract.Contract;
import com.higgschain.trust.slave.model.bo.contract.ContractCreationAction;
import com.higgschain.trust.slave.model.bo.contract.ContractInvokeAction;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

/**
 * The type Contract invoke handler interface test.
 *
 * @author duhongming
 * @date 2018 /5/7
 */
public class ContractInvokeHandlerInterfaceTest extends ContractBaseTest {

    @Autowired private SnapshotService snapshot;
    @Autowired private ContractInvokeHandler invokeHandler;
    @Autowired private ContractRepository contractRepository;
    @Autowired private DbContractStateStoreImpl dbContractStateStore;
    @Autowired private ContractStateSnapshotAgent stateSnapshotAgent;
    @Autowired private ContractSnapshotAgent contractSnapshotAgent;

    /**
     * Clear db.
     */
    @AfterClass
    public void clearDb() {
        executeDelete("TRUNCATE TABLE contract;TRUNCATE TABLE contract_state");
    }

    private String getHash(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            byte[] cipherByte = md.digest();
            return Hex.encodeHexString(cipherByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createContract(String filePath) {
        try {
            String code = IOUtils.toString(this.getClass().getResource(String.format("/%s%s", getProviderRootPath(), filePath)), "UTF-8");
            Contract contract = new Contract();
            contract.setBlockHeight(1L);
            contract.setTxId("0000000000000000000000" + System.currentTimeMillis());
            contract.setActionIndex(0);
            contract.setAddress(getHash(code + System.currentTimeMillis()));
            contract.setCode(code);
            contract.setLanguage("javascript");
            contract.setVersion("0.1");
            contract.setCreateTime(new Date());

//            contractRepository.deploy(contract);
            return contract.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ContractInvokeAction createContractInvokeAction(Map<?, ?> param) {
        ContractInvokeAction action = getBody(param, ContractInvokeAction.class);
        if (action == null) {
            String address = createContract(String.valueOf(param.get("codeFile")));
            action = new ContractInvokeAction();
            action.setAddress(address);
        }
        action.setIndex(0);
        action.setType(ActionTypeEnum.TRIGGER_CONTRACT);
        return action;
    }

    private PackContext createPackContext(Map<?, ?> param) {
        Action action = createContractInvokeAction(param);
        return createPackContext(action);
    }

    private PackContext createPackContext(Action action) {
        PackContext packContext = ActionDataMockBuilder.getBuilder()
                .createSignedTransaction(InitPolicyEnum.CONTRACT_ISSUE)
                .addAction(action)
                .setTxId("00000000000" + System.currentTimeMillis())
                .signature("", ActionDataMockBuilder.privateKey1)
                .signature("", ActionDataMockBuilder.privateKey2)
                .makeBlockHeader()
                .build();
        return packContext;
    }

    @Override
    public String getProviderRootPath() {
        return "java/com/higgs/trust/slave/core/service/contract/invoke/";
    }

    /**
     * Test validate.
     *
     * @param param the param
     */
    @Test(dataProvider = "defaultProvider",priority = 0)
    public void testValidate(Map<?, ?> param) {
        snapshot.startTransaction();
        PackContext packContext = createPackContext(param);
        doTestValidate(param, packContext, invokeHandler);
        snapshot.commit();
    }

    /**
     * Test persist.
     *
     * @param param the param
     */
    @Test(dataProvider = "defaultProvider",priority = 1)
    public void testPersist(Map<?, ?> param) {
        PackContext packContext = createPackContext(param);
        doTestPersist(param, packContext, invokeHandler);
    }

    /**
     * Test validate repeat execution.
     */
    @Test
    public void testValidate_Repeat_Execution() {
        snapshot.startTransaction();
        String address = createContract("../code/putState.js");
        ContractInvokeAction action = new ContractInvokeAction();
        action.setAddress(address);
        action.setIndex(0);
        action.setType(ActionTypeEnum.TRIGGER_CONTRACT);
        PackContext packContext = createPackContext(action);
        Profiler.start();
        for(int i = 0; i < 3; i++) {
            invokeHandler.process(packContext);
        }
        Profiler.logDump();
        Object stateManager = stateSnapshotAgent.get(address);
        snapshot.commit();
    }

    /**
     * Test persist repeat execution.
     */
    @Test
    public void testPersist_Repeat_Execution() {
        String address = createContract("../code/putState.js");
        ContractInvokeAction action = new ContractInvokeAction();
        action.setAddress(address);
        action.setIndex(0);
        action.setType(ActionTypeEnum.TRIGGER_CONTRACT);
        PackContext packContext = createPackContext(action);
        Profiler.start();
        for (int i = 0; i < 3; i++) {
            invokeHandler.process(packContext);
        }
        Profiler.logDump();
        Object stateManager = dbContractStateStore.get(address);
//        int actualRunCount = stateManager.getInt("runCount");
//        final int expectRunCount = 30;
//        Assert.assertEquals(actualRunCount, expectRunCount);
    }

    /**
     * Test validate action type not expect.
     */
    @Test
    public void testValidate_ActionType_Not_Expect() {
        try {
            Action action = new ContractCreationAction();
            action.setIndex(0);
            action.setType(ActionTypeEnum.REGISTER_CONTRACT);
            PackContext packContext = createPackContext(action);
            invokeHandler.process(packContext);
            Assert.fail();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.assertTrue(ex instanceof  IllegalArgumentException, "throws IllegalArgumentException");
        }
    }

    /**
     * Test persist action type not expect.
     */
    @Test
    public void testPersist_ActionType_Not_Expect() {
        try {
            Action action = new ContractCreationAction();
            action.setIndex(0);
            action.setType(ActionTypeEnum.REGISTER_CONTRACT);
            PackContext packContext = createPackContext(action);
            invokeHandler.process(packContext);
            Assert.fail();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.assertTrue(ex instanceof  IllegalArgumentException, "throws IllegalArgumentException");
        }
    }

    /**
     * Test invoke.
     */
    @Test
    public void testInvoke() {
        String address = createContract("../code/hello_contract.js");
        ContractInvokeAction action = new ContractInvokeAction();
        action.setAddress(address);
        action.setIndex(0);
        action.setType(ActionTypeEnum.TRIGGER_CONTRACT);
        PackContext packContext = createPackContext(action);
        Profiler.start();

        String bizArgsJson = "[\"add\", [1,2]]";
        Object[] bizArgs = JSON.parseArray(bizArgsJson).toArray();
        action.setArgs(bizArgs);
        invokeHandler.process(packContext);
        invokeHandler.process(packContext);
        invokeHandler.process(packContext);
        Profiler.logDump();
//        StateManager stateManager = dbContractStateStore.get(address);
//        int actualRunCount = stateManager.getInt("runCount");
//        final int expectRunCount = 30;
//        Assert.assertEquals(actualRunCount, expectRunCount);
    }
}
