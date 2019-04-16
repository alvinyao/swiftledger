package com.higgschain.trust.slave.core.service.action.account;

import com.higgschain.trust.slave._interface.InterfaceCommonTest;
import com.higgschain.trust.slave.model.bo.account.OpenAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * The type Open account handler test.
 *
 * @author hanson
 * @Date 2018 /4/26
 * @Description:
 */
public class OpenAccountHandlerTest extends InterfaceCommonTest {

    private final static String rootPath = "java/com/higgs/trust/slave/core/service/accounting/openAccount/";

    /**
     * The Open account handler.
     */
    @Autowired
    OpenAccountHandler openAccountHandler;

    @BeforeMethod()
    public void before() {
        super.before();

    }

    @AfterMethod
    public void after() {
        super.after();

    }

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
    // TODO 搞明白开户的逻辑
    @Test(dataProvider = "defaultProvider", priority = 0)
    public void paramValidate(Map<?, ?> param) throws Exception {

        OpenAccount openAccount = getBodyData(param, OpenAccount.class);
        executeActionHandler(param,openAccountHandler,openAccount);

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

        OpenAccount openAccount = getBodyData(param, OpenAccount.class);
        executeActionHandler(param,openAccountHandler,openAccount);

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
//        if (param.get("beforeSql") != null) {
//            String[] sql = param.get("beforeSql").toString().split(";");
//            DataBaseManager dataBaseManager = new DataBaseManager();
//            for(String s:sql)  dataBaseManager.executeSingleDelete(s, DB_URL);
//        }
        executeBeforeSql(param);

        OpenAccount openAccount = getBodyData(param, OpenAccount.class);
        executeActionHandler(param,openAccountHandler,openAccount);

        executeAfterSql(param);
//        if (param.get("afterSql") != null) {
//            String[] sql = param.get("beforeSql").toString().split(";");
//            DataBaseManager dataBaseManager = new DataBaseManager();
//            for(String s:sql)
//                dataBaseManager.executeSingleDelete(s, DB_URL);
//        }

    }

}