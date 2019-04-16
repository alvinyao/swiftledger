package com.higgschain.trust.slave.core.service.action.manage;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.core.service.block.BlockService;
import com.higgschain.trust.slave.model.bo.Block;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.context.PackContext;
import com.higgschain.trust.slave.model.bo.manage.RegisterRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * The type Register rs handler test.
 */
public class RegisterRsHandlerTest extends BaseTest {
    /**
     * The Block service.
     */
    @Autowired BlockService blockService;

    /**
     * The Register rs handler.
     */
    @Autowired
    RegisterRsHandler registerRsHandler;

    /**
     * Test process.
     *
     * @throws Exception the exception
     */
    @Test public void testProcess() throws Exception {
        Package pack = new Package();
        pack.setHeight(2L);
        Block block = blockService.buildDummyBlock(2L, new Date().getTime());
        PackContext packContext = new PackContext(pack, block);

        RegisterRS registerRS = new RegisterRS();
        registerRS.setRsId("test-register-rs1");
        registerRS.setDesc("测试register");
        registerRS.setIndex(0);
        registerRS.setType(ActionTypeEnum.REGISTER_RS);

        packContext.setCurrentAction(registerRS);



        registerRsHandler.process(packContext);
    }
}