package com.higgschain.trust.slave.core.service;

import com.higgschain.trust.slave.IntegrateBaseTest;
import com.higgschain.trust.slave.api.enums.account.FundDirectionEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.core.repository.TransactionRepository;
import com.higgschain.trust.slave.core.service.action.account.TestDataMaker;
import com.higgschain.trust.slave.core.service.block.BlockService;
import com.higgschain.trust.slave.core.service.block.hash.TxRootHashBuilder;
import com.higgschain.trust.slave.model.bo.*;
import com.higgschain.trust.slave.model.bo.action.Action;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Block service test.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-17
 */
public class BlockServiceTest extends IntegrateBaseTest {

    /**
     * The Block service.
     */
    @Autowired BlockService blockService;
    /**
     * The Transaction repository.
     */
    @Autowired TransactionRepository transactionRepository;
    /**
     * The Tx root hash builder.
     */
    @Autowired TxRootHashBuilder txRootHashBuilder;

    /**
     * Gets max height.
     */
    @Test public void getMaxHeight() {
        Long height = blockService.getMaxHeight();
        System.out.println("max.height:" + height);
    }

    /**
     * Persist block.
     *
     * @throws Exception the exception
     */
    @Test public void persistBlock() throws Exception {
        List<SignedTransaction> txs = new ArrayList<>();
        for (int k = 0; k < 2; k++) {
            List<Action> actions = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Action action =
                    TestDataMaker.makeOpenAccountAction("tx-" + k + "-account_no_" + i, FundDirectionEnum.CREDIT);
                action.setIndex(i);
                actions.add(action);
            }
            CoreTransaction coreTransaction = TestDataMaker.makeCoreTx(actions, k, InitPolicyEnum.REGISTER_POLICY);
            SignedTransaction tx = TestDataMaker.makeSignedTx(coreTransaction);
            txs.add(tx);
        }
        Block block = new Block();
        BlockHeader blockHeader = TestDataMaker.makeBlockHeader();
        block.setBlockHeader(blockHeader);
        block.setSignedTxList(txs);
        Map<String, TransactionReceipt> txReceiptMap = new HashMap<>();
        TransactionReceipt receipt = new TransactionReceipt();
        receipt.setTxId(txs.get(0).getCoreTx().getTxId());
        receipt.setResult(false);
        receipt.setErrorCode("xxxxxxxxx");
        txReceiptMap.put(receipt.getTxId(), receipt);
        blockService.persistBlock(block,txReceiptMap);
    }

    /**
     * Test query block.
     */
    @Test public void testQueryBlock() {
        Block block = blockService.queryBlock(1L);
        System.out.println(block);
        List<SignedTransaction> txs = block.getSignedTxList();
        for (SignedTransaction tx : txs) {
            CoreTransaction coreTransaction = tx.getCoreTx();
            List<Action> actions = coreTransaction.getActionList();
            for (Action action : actions) {
                System.out.println(action.getType());
            }
        }
    }

    /**
     * Test query transaction.
     */
    @Test public void testQueryTransaction() {
        List<SignedTransaction> txs = transactionRepository.queryTransactions(4L);
        String rootHash  = txRootHashBuilder.buildTxs(txs);
        System.out.println("rootHash--->" + rootHash);
    }

}
