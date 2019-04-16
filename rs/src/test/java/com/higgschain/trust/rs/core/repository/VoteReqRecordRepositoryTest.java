package com.higgschain.trust.rs.core.repository;

import com.higgschain.trust.IntegrateBaseTest;
import com.higgschain.trust.common.dao.RocksUtils;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.rs.core.api.enums.VoteResultEnum;
import com.higgschain.trust.rs.core.bo.VoteRequestRecord;
import org.rocksdb.Transaction;
import org.rocksdb.WriteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Vote req record repository test.
 */
public class VoteReqRecordRepositoryTest extends IntegrateBaseTest {
    @Autowired
    private VoteReqRecordRepository voteReqRecordRepository;

    /**
     * Test add.
     *
     * @throws Exception the exception
     */
    @Test public void testAdd() throws Exception {
        VoteRequestRecord voteRequestRecord = new VoteRequestRecord();
        voteRequestRecord.setTxId("test-tx-id-1");
        voteRequestRecord.setSender("TRUST-TEST0");
        voteRequestRecord.setTxData("test-data");
        voteRequestRecord.setId(1L);
        voteRequestRecord.setSign("test-signature");
        Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
        ThreadLocalUtils.putRocksTx(tx);
        voteReqRecordRepository.add(voteRequestRecord);
        RocksUtils.txCommit(tx);
        ThreadLocalUtils.clearRocksTx();
    }

    /**
     * Test query by tx id.
     *
     * @throws Exception the exception
     */
    @Test public void testQueryByTxId() throws Exception {
        VoteRequestRecord requestRecord = voteReqRecordRepository.queryByTxId("test-tx-id-1");
        System.out.println(requestRecord);
    }

    /**
     * Test set vote result.
     *
     * @throws Exception the exception
     */
    @Test public void testSetVoteResult() throws Exception {

        Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
        ThreadLocalUtils.putRocksTx(tx);
        voteReqRecordRepository.setVoteResult("test-tx-id-1", "new test signature", VoteResultEnum.AGREE);
        RocksUtils.txCommit(tx);
        ThreadLocalUtils.clearRocksTx();

        VoteRequestRecord requestRecord = voteReqRecordRepository.queryByTxId("test-tx-id-1");
        System.out.println(requestRecord);
    }
}