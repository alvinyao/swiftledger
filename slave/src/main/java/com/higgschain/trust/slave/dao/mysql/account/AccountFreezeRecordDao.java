package com.higgschain.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.account.AccountFreezeRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Account freeze record dao.
 *
 * @author liuyu
 * @descrition
 * @date 2018 -03-30
 */
@Mapper public interface AccountFreezeRecordDao extends BaseDao<AccountFreezeRecordPO> {

    /**
     * query by flowNo and accountNo,return entity
     *
     * @param bizFlowNo the biz flow no
     * @param accountNo the account no
     * @return account freeze record po
     */
    AccountFreezeRecordPO queryByFlowNoAndAccountNo(@Param("bizFlowNo") String bizFlowNo,
        @Param("accountNo") String accountNo);

    /**
     * decrease amount by data id
     *
     * @param id     the id
     * @param amount the amount
     * @return int
     */
    int decreaseAmount(@Param("id") Long id, @Param("amount") BigDecimal amount);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(@Param("list") List<AccountFreezeRecordPO> list);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchUpdate(@Param("list") List<AccountFreezeRecordPO> list);
}
