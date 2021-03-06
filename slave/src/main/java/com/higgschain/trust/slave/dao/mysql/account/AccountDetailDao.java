package com.higgschain.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.account.AccountDetailPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Account detail dao.
 *
 * @author liuyu
 * @description detail of account DAO
 * @date 2018 -03-27
 */
@Mapper public interface AccountDetailDao extends BaseDao<AccountDetailPO> {
    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(@Param("list") List<AccountDetailPO> list);
}
