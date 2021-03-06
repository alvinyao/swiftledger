package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.IntegrateBaseTest;
import com.higgschain.trust.rs.core.api.enums.CoreTxStatusEnum;
import com.higgschain.trust.rs.core.dao.po.CoreTransactionProcessPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.List;

/**
 * CoreTxProcessJDBCDao test
 *
 * @author lingchao
 * @create 2018年08月25日17 :07
 */
public class CoreTxProcessJDBCDaoTest extends IntegrateBaseTest {
    @Autowired
    private CoreTxProcessJDBCDao coreTxProcessJDBCDao;

    /**
     * Batch insert tx.
     */
    @Test
    public void  batchInsertTx(){
        List<CoreTransactionProcessPO> list = Lists.newArrayList();
        for(int i =0; i<100; i++){
            CoreTransactionProcessPO coreTransactionProcessPO = new CoreTransactionProcessPO();
            coreTransactionProcessPO.setTxId(i+"");
            coreTransactionProcessPO.setStatus(CoreTxStatusEnum.WAIT.getCode());
            list.add(coreTransactionProcessPO);
        }
        System.out.println("batchInsertTx:"+coreTxProcessJDBCDao.batchInsert(list));
    }

    /**
     * Batch update status.
     */
    @Test
    public void  batchUpdateStatus(){
        List<CoreTransactionProcessPO> list = Lists.newArrayList();
        for(int i =0; i<100; i++){
            CoreTransactionProcessPO coreTransactionProcessPO = new CoreTransactionProcessPO();
            coreTransactionProcessPO.setTxId(i+"");
            coreTransactionProcessPO.setStatus(CoreTxStatusEnum.INIT.getCode());
            list.add(coreTransactionProcessPO);
        }
        System.out.println("batchInsertTx:"+coreTxProcessJDBCDao.batchUpdate(list, CoreTxStatusEnum.INIT,CoreTxStatusEnum.WAIT));
    }
}
