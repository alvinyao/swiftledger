package com.higgschain.trust.slave.core.service.action.manage;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.block.BlockService;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.bo.manage.RegisterPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * The type Register policy handler test.
 */
/*
 *
 * @desc
 * @author shenaingyan
 * @date 2018/4/2
 *
 */
public class RegisterPolicyHandlerTest extends BaseTest {

    /**
     * The Register policy handler.
     */
    @Autowired
    RegisterPolicyHandler registerPolicyHandler;

    /**
     * The Block service.
     */
    @Autowired BlockService blockService;

    /**
     * Test validate 1.
     *
     * @throws Exception the exception
     */
    //policy已存在
    @Test public void testValidate1() throws Exception {

        Package pack = new Package();
        pack.setHeight(1L);
        Block block = blockService.buildDummyBlock(1L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        List<String> rsIds = new ArrayList<>();
        rsIds.add("test1");
        rsIds.add("test2");
        rsIds.add("test3");
        RegisterPolicy registerPolicyAction = new RegisterPolicy();
        registerPolicyAction.setIndex(1);
        registerPolicyAction.setType(ActionTypeEnum.REGISTER_POLICY);
        registerPolicyAction.setPolicyId("policy-1hsdh6310-23hhs");
        registerPolicyAction.setPolicyName("test-policy");
        registerPolicyAction.setRsIds(rsIds);

        packContext.setCurrentAction(registerPolicyAction);

        try {
            registerPolicyHandler.process(packContext);
        } catch (SlaveException e) {
            Assert.assertEquals(e.getCode().toString(), "SLAVE_POLICY_EXISTS_ERROR");
        }
    }

    /**
     * Test validate 2.
     *
     * @throws Exception the exception
     */
    //policy参数为空
    @Test public void testValidate2() throws Exception {
        Package pack = new Package();
        pack.setHeight(1L);
        Block block = blockService.buildDummyBlock(1L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        RegisterPolicy registerPolicyAction = new RegisterPolicy();
        registerPolicyAction = null;

        packContext.setCurrentAction(registerPolicyAction);
        try {
            registerPolicyHandler.process(packContext);
        } catch (SlaveException e) {
            Assert.assertEquals(e.getCode().toString(), "SLAVE_PARAM_VALIDATE_ERROR");
        }

    }

    /**
     * Test validate 3.
     *
     * @throws Exception the exception
     */
    //    policy参数校验不通过
    @Test public void testValidate3() throws Exception {
        Package pack = new Package();
        pack.setHeight(1L);
        Block block = blockService.buildDummyBlock(1L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        List<String> rsIds = new ArrayList<>();
        rsIds.add("test1");
        rsIds.add("test2");
        rsIds.add("test3");
        RegisterPolicy registerPolicyAction = new RegisterPolicy();
        registerPolicyAction.setIndex(1);
        registerPolicyAction.setType(ActionTypeEnum.REGISTER_POLICY);
        registerPolicyAction.setPolicyName("test-policy");
        registerPolicyAction.setRsIds(rsIds);

        packContext.setCurrentAction(registerPolicyAction);
        try {
            registerPolicyHandler.process(packContext);
        } catch (SlaveException e) {
            Assert.assertEquals(e.getCode().toString(), "SLAVE_PARAM_VALIDATE_ERROR");
        }
    }

    /**
     * Test validate 4.
     *
     * @throws Exception the exception
     */
    //rsIdList重复
    @Test public void testValidate4() throws Exception {
        Package pack = new Package();
        pack.setHeight(1L);
        Block block = blockService.buildDummyBlock(1L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        List<String> rsIds = new ArrayList<>();
        rsIds.add("test1");
        rsIds.add("test2");
        rsIds.add("test2");
        RegisterPolicy registerPolicyAction = new RegisterPolicy();
        registerPolicyAction.setIndex(1);
        registerPolicyAction.setType(ActionTypeEnum.REGISTER_POLICY);
        registerPolicyAction.setPolicyId("policy-1hsdh6310-23hhs");
        registerPolicyAction.setPolicyName("test-policy");
        registerPolicyAction.setRsIds(rsIds);

        packContext.setCurrentAction(registerPolicyAction);

        try {
            registerPolicyHandler.process(packContext);
        } catch (SlaveException e) {
            Assert.assertEquals(e.getCode().toString(), "SLAVE_PARAM_VALIDATE_ERROR");
        }
    }

    /**
     * Test validate 5.
     *
     * @throws Exception the exception
     */
    //校验通过
    @Test public void testValidate5() throws Exception {
        Package pack = new Package();
        pack.setHeight(1L);
        Block block = blockService.buildDummyBlock(1L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        List<String> rsIds = new ArrayList<>();
        rsIds.add("test1");
        rsIds.add("test2");
        rsIds.add("test3");
//        rsIds.add("test3");
        RegisterPolicy registerPolicyAction = new RegisterPolicy();
        registerPolicyAction.setIndex(1);
        registerPolicyAction.setType(ActionTypeEnum.REGISTER_POLICY);
        Random random = new Random();
        String pid = String.valueOf(random.nextInt());
        registerPolicyAction.setPolicyId(pid);
        registerPolicyAction.setPolicyName("test-policy");
        registerPolicyAction.setRsIds(rsIds);

        packContext.setCurrentAction(registerPolicyAction);

        registerPolicyHandler.process(packContext);
    }

    /**
     * Test persist.
     *
     * @throws Exception the exception
     */
    //成功增加policy
    @Test public void testPersist() throws Exception {
        Package pack = new Package();
        pack.setHeight(1L);
        Block block = blockService.buildDummyBlock(1L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        List<String> rsIds = new ArrayList<>();
        rsIds.add("test1");
        rsIds.add("test2");
        rsIds.add("test3");
        RegisterPolicy registerPolicyAction = new RegisterPolicy();
        registerPolicyAction.setIndex(1);
        registerPolicyAction.setType(ActionTypeEnum.REGISTER_POLICY);
        Random random = new Random();
        String pid = String.valueOf(random.nextInt());
        registerPolicyAction.setPolicyId(pid);
        registerPolicyAction.setPolicyName("test-policy");
        registerPolicyAction.setRsIds(rsIds);

        packContext.setCurrentAction(registerPolicyAction);

        registerPolicyHandler.process(packContext);
    }

}