package com.higgschain.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.account.AccountDetailFreezePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Account detail freeze dao.
 *
 * @author liuyu
 * @description freeze detail DAO
 * @date 2018 -03-27
 */
@Mapper public interface AccountDetailFreezeDao extends BaseDao<AccountDetailFreezePO> {
    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(@Param("list") List<AccountDetailFreezePO> list);
}
