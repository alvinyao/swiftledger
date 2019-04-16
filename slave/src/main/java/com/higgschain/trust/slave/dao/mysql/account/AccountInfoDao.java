package com.higgschain.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.account.AccountInfoPO;
import com.higgschain.trust.slave.dao.po.account.AccountInfoWithOwnerPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Account info dao.
 *
 * @author liuyu
 * @description account info DAO
 * @date 2018 -03-27
 */
@Mapper public interface AccountInfoDao extends BaseDao<AccountInfoPO> {
    /**
     * query by accountNo,support FOR UPDATE
     *
     * @param accountNo the account no
     * @param forUpdate the for update
     * @return account info po
     */
    AccountInfoPO queryByAccountNo(@Param("accountNo") String accountNo, @Param("forUpdate") boolean forUpdate);

    /**
     * batch query the account info
     *
     * @param accountNos the account nos
     * @return list
     */
    List<AccountInfoPO> queryByAccountNos(@Param("accountNos") List<String> accountNos);

    /**
     * increase balance for account
     *
     * @param accountNo the account no
     * @param amount    the amount
     * @return int
     */
    int increaseBalance(@Param("accountNo") String accountNo, @Param("amount") BigDecimal amount);

    /**
     * decrease balance for account
     *
     * @param accountNo the account no
     * @param amount    the amount
     * @return int
     */
    int decreaseBalance(@Param("accountNo") String accountNo, @Param("amount") BigDecimal amount);

    /**
     * freeze account balance
     * only increase freeze_amount
     *
     * @param accountNo the account no
     * @param amount    the amount
     * @return int
     */
    int freeze(@Param("accountNo") String accountNo, @Param("amount") BigDecimal amount);

    /**
     * unfreeze account balance
     * only decrease freeze_amount
     *
     * @param accountNo the account no
     * @param amount    the amount
     * @return int
     */
    int unfreeze(@Param("accountNo") String accountNo, @Param("amount") BigDecimal amount);

    /**
     * query account with data owner
     *
     * @param accountNo the account no
     * @param dataOwner the data owner
     * @param start     the start
     * @param end       the end
     * @return list
     */
    List<AccountInfoWithOwnerPO> queryAccountInfoWithOwner(@Param("accountNo") String accountNo,
                                                           @Param("dataOwner") String dataOwner, @Param("start") Integer start, @Param("end") Integer end);

    /**
     * count
     *
     * @param accountNo the account no
     * @param dataOwner the data owner
     * @return long
     */
    long countAccountInfoWithOwner(@Param("accountNo") String accountNo, @Param("dataOwner") String dataOwner);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(@Param("list")List<AccountInfoPO> list);

    /**
     * batch update
     *
     * @param list the list
     * @return int
     */
    int batchUpdate(@Param("list")List<AccountInfoPO> list);
}
