package com.higgschain.trust.slave._interface;

import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.core.service.action.utxo.UTXOActionHandler;
import com.higgschain.trust.slave.model.bo.action.UTXOAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * UTXO test
 *
 * @author lingchao
 * @create 2018年05月10日10 :41
 */
@Slf4j
public class UTXOTest extends InterfaceCommonTest {
    private static String PROVIDER_ROOT_PATH = "java/com/higgs/trust/slave/core/service/action/utxo/";
    @Override
    protected String getProviderRootPath() {
        return PROVIDER_ROOT_PATH;
    }

    @Autowired private UTXOActionHandler utxoActionHandler;

    /**
     * Param validate.
     *
     * @param param the param
     */
    @Test(dataProvider = "defaultProvider", priority = 1)
    public void paramValidate(Map<?, ?> param) {
        log.info("[paramValidate]{}", param.get("comment"));
        UTXOAction action = getAction(param, UTXOAction.class, ActionTypeEnum.UTXO);
        executeActionHandler(param, utxoActionHandler, action);
    }

    /**
     * Test regular.
     *
     * @param param the param
     */
    @Test(dataProvider = "defaultProvider", priority = 2)
    public void testRegular(Map<?, ?> param) {
        log.info("[testRegular]{}", param.get("comment"));


        executeBeforeSql(param);

        UTXOAction action = getAction(param, UTXOAction.class, ActionTypeEnum.UTXO);
        executeActionHandler(param, utxoActionHandler, action);

        checkResults(param);

        executeAfterSql(param);
    }

    /**
     * Test exception.
     *
     * @param param the param
     */
    @Test(dataProvider = "defaultProvider", priority = 3)
    public void testException(Map<?, ?> param) {
        log.info("[testException]{}", param.get("comment"));
        executeBeforeSql(param);

        UTXOAction action = getAction(param, UTXOAction.class, ActionTypeEnum.UTXO);
        executeActionHandler(param, utxoActionHandler, action);

        executeAfterSql(param);
    }

    /**
     * Test.
     *
     * @param param the param
     */
    @Test(dataProvider = "defaultProvider", priority = 1)
    public void test(Map<?, ?> param) {
        log.info("[paramValidate]{}", param.get("comment"));
        UTXOAction action = getAction(param, UTXOAction.class, ActionTypeEnum.UTXO);
        executeActionHandler(param, utxoActionHandler, action);
    }

}
