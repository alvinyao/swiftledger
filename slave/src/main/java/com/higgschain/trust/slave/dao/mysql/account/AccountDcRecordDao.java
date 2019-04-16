package com.higgschain.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.account.AccountDcRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Account dc record dao.
 *
 * @author liuyu
 * @description account DC record DAO
 * @date 2018 -03-27
 */
@Mapper public interface AccountDcRecordDao extends BaseDao<AccountDcRecordPO> {
    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(@Param("list") List<AccountDcRecordPO> list);
}
