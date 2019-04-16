package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.VoteRequestRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Vote request record dao.
 */
@Mapper public interface VoteRequestRecordDao extends BaseDao<VoteRequestRecordPO> {

    /**
     * query by transaction id
     *
     * @param txId the tx id
     * @return vote request record po
     */
    VoteRequestRecordPO queryByTxId(@Param("txId") String txId);

    /**
     * set vote result,from INIT to AGREE or DISAGREE
     *
     * @param txId       the tx id
     * @param sign       the sign
     * @param voteResult the vote result
     * @return vote result
     */
    int setVoteResult(@Param("txId") String txId,@Param("sign")String sign,@Param("voteResult") String voteResult);

    /**
     * query all init request
     *
     * @param row   the row
     * @param count the count
     * @return list
     */
    List<VoteRequestRecordPO> queryAllInitRequest(@Param("row") int row,@Param("count") int count);
}
