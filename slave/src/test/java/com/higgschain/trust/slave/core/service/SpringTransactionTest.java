package com.higgschain.trust.slave.core.service;

import com.higgschain.trust.common.utils.BeanConvertor;
import com.higgschain.trust.slave.IntegrateBaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.account.FundDirectionEnum;
import com.higgschain.trust.slave.core.repository.account.AccountRepository;
import com.higgschain.trust.slave.dao.mysql.account.AccountInfoDao;
import com.higgschain.trust.slave.dao.po.account.AccountInfoPO;
import com.higgschain.trust.slave.model.bo.account.AccountInfo;
import com.higgschain.trust.slave.model.bo.account.OpenAccount;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;

/**
 * @author liuyu
 * @description
 * @date 2018-04-20
 */
public class SpringTransactionTest extends IntegrateBaseTest {

    @Autowired TransactionTemplate txNested;
    @Autowired TransactionTemplate txRequired;
    @Autowired TransactionTemplate txRequiresNew;

    @Autowired AccountRepository accountRepository;
    @Autowired AccountInfoDao accountInfoDao;

    @Test
    public void testSpringTransaction() throws IOException {
        OpenAccount openAccount = new OpenAccount();
        openAccount.setType(ActionTypeEnum.OPEN_ACCOUNT);
        openAccount.setIndex(1);
        openAccount.setAccountNo("account_no_0003");
        openAccount.setChainOwner("BUC_CHAIN");
        openAccount.setDataOwner("ALL");
        openAccount.setCurrency("CNY");
        openAccount.setFundDirection(FundDirectionEnum.CREDIT);


        AccountInfo accountInfo = accountRepository.buildAccountInfo(openAccount);
        accountInfo.setAccountNo("account_no_0004");

        AccountInfoPO accountInfoPO = BeanConvertor.convertBean(accountInfo,AccountInfoPO.class);

        txNested.execute(new TransactionCallbackWithoutResult() {
            @Override protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
//                    process(openAccount);
                }catch (Throwable e){
                    System.out.println("has error:" + e);
                }
                //
                accountInfoDao.add(accountInfoPO);
//               throw new RuntimeException("TEST EXCEPTION_1");
            }
        });
        System.out.println("------->exe.end");
    }

    private void process(OpenAccount openAccount){
        txNested.execute(new TransactionCallbackWithoutResult() {
            @Override protected void doInTransactionWithoutResult(TransactionStatus status) {
//               accountRepository.openAccount(openAccount);
//               throw new RuntimeException("TEST EXCEPTION_0");
            }
        });
    }
}
