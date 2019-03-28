package com.higgschain.trust.slave.core.repository;

import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.DecisionTypeEnum;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.core.repository.config.SystemPropertyRepository;
import com.higgschain.trust.slave.dao.po.block.BlockPO;
import com.higgschain.trust.slave.dao.rocks.block.BlockRocksDao;
import com.higgschain.trust.slave.dao.rocks.block.BlockTestRocksDao;
import com.higgschain.trust.slave.dao.rocks.transaction.TxRocksDao;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignInfo;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.manage.RegisterPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author liuyu
 * @description
 * @date 2018-04-18
 */
public class BlockRepositoryTest  extends BaseTest {
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SystemPropertyRepository systemPropertyRepository;
    @Autowired
    private InitConfig initConfig;

    @Autowired
    private BlockRocksDao blockRocksDao;

    @Autowired
    private BlockTestRocksDao blockTestRocksDao;

    @Autowired
    private TxRocksDao txRocksDao;

    @Test public void testGetMaxHeight() throws Exception {
        if (!initConfig.isUseMySQL()) {
            systemPropertyRepository.add(Constant.MAX_BLOCK_HEIGHT, "100", "max block height");
        }
        Long height = blockRepository.getMaxHeight();
        Assert.assertEquals(height.longValue(), 100L);
    }

    @Test public void testGetLimitHeight() throws Exception {
        blockRepository.getLimitHeight(10);
    }

    @Test public void testGetBlock() throws Exception {
    }

    @Test public void testListBlocks() throws Exception {
        System.out.println(blockRepository.listBlocks(3, 2));
    }

    @Test public void testListBlockHeaders() throws Exception {
    }

    @Test public void testGetBlockHeader() throws Exception {
    }

    @Test public void testSaveBlock() throws Exception {
//        BlockPO blockPO = new BlockPO();
//        blockPO.setBlockTime(new Date());
//        blockPO.setHeight(10000L);
//        blockPO.setTxReceiptRootHash("txReceiptHash");
//        blockPO.setTxRootHash("txRootHash");
//        blockPO.setPolicyRootHash("policyRootHash");
//        blockPO.setRsRootHash("rsRootHash");
//        blockPO.setContractRootHash("contractRootHash");
//        blockPO.setAccountRootHash("accountRootHash");
//        blockPO.setCaRootHash("caRootHash");
//        blockPO.setPreviousHash("previousHash");
//        blockPO.setVersion("1.0.1");
//        blockPO.setBlockHash("blockHash");
//        blockPO.setTotalTxNum(200L);
//
//        blockPO.setTxNum(200);
//        blockPO.setTotalBlockSize(new BigDecimal("123"));
//
//        List<TransactionPO> pos = new ArrayList<>();
//        for (int i = 0; i < 200; i++) {
//            TransactionPO transactionPO = new TransactionPO();
//            transactionPO.setSendTime(new Date());
//            transactionPO.setActionDatas("test-actionDatas");
//            transactionPO.setBizModel("test-bizModel");
////            transactionPO.set
//        }
//
//        List<SignedTransaction> signedTxList = new ArrayList<>();
//        for (int i = 0; i < 200; i++) {
//            signedTxList.add(buildSignedTx());
//        }
//
//        blockPO.setTxPOs(pos);

        for (int i = 0; i < 100; i++) {
            BlockPO po = blockRocksDao.get(String.valueOf(i + 2));

//            List<TransactionPO> pos = new ArrayList<>();
//            for (TransactionPO txPo : po.getTxPOs()) {
//                txPo.setTxId("test-tx-id" + System.currentTimeMillis());
//                pos.add(txPo);
//            }
//
//            po.setHeight(Long.valueOf(i + 285));
////            po.setTxPOs(pos);
//            ThreadLocalUtils.putRocksTx(tx);
//            long begin = System.currentTimeMillis();
//            blockRocksDao.save(po);
//            RocksUtils.txCommit(tx);
//            System.out.println("save block with transactionPOs: " + (System.currentTimeMillis() - begin) + "ms");
//            ThreadLocalUtils.clearRocksTx();
//
//            ThreadLocalUtils.putRocksTx(tx);
//            long begin2 = System.currentTimeMillis();
//            blockTestRocksDao.save(po);
//            txRocksDao.batchInsert(pos);
//            RocksUtils.txCommit(tx);
//            System.out.println("save block without transactionPOs: " + (System.currentTimeMillis() - begin2) + "ms");
//            ThreadLocalUtils.clearRocksTx();
        }



    }

    @Test public void testQueryBlocksWithCondition() throws Exception {
    }

    @Test public void testCountBlocksWithCondition() throws Exception {
    }

    @Test public void testQueryBlockByHeight() throws Exception {
    }

    private SignedTransaction buildSignedTx() {
        Random r = new Random();

        List<Action> actionList = new ArrayList<>();
        RegisterPolicy action = new RegisterPolicy();
        action.setIndex(0);
        action.setType(ActionTypeEnum.REGISTER_POLICY);
        action.setPolicyName("OPEN_ACCOUNT");
        action.setPolicyId("OPEN_ACCOUNT");
        action.setDecisionType(DecisionTypeEnum.FULL_VOTE);
        action.setContractAddr(null);

        List<String> rsIds = new ArrayList<>();
        rsIds.add("TRUST-TEST0");
        action.setRsIds(rsIds);
        actionList.add(action);

        CoreTransaction coreTx = new CoreTransaction();

        coreTx.setTxId("test-tx-id-" + r.nextLong());
        coreTx.setPolicyId("000001");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", actionList);
        coreTx.setBizModel(jsonObject);
        coreTx.setLockTime(new Date());
        coreTx.setSender("TRUST-TEST0");
        coreTx.setVersion("1.0.0");
        coreTx.setSendTime(new Date());
        coreTx.setActionList(actionList);

        SignedTransaction signedTx = new SignedTransaction();
        signedTx.setCoreTx(coreTx);

        List<SignInfo> signInfos = new ArrayList<>();
        SignInfo signInfo = new SignInfo();
        signInfo.setOwner("TRUST-TEST0");
        signInfo.setSign("sgcgsdc");
        signInfos.add(signInfo);

        signedTx.setSignatureList(signInfos);
        return signedTx;
    }
}
