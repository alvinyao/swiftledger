package com.higgs.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgs.trust.slave.dao.po.account.CurrencyInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyu
 * @description currency DAO
 * @date 2018-03-27
 */
@Mapper public interface CurrencyInfoDao extends BaseDao<CurrencyInfoPO> {
    /**
     * query by currency
     *
     * @param currency
     * @return
     */
    CurrencyInfoPO queryByCurrency(@Param("currency") String currency);

    /**
     * batch insert
     *
     * @param list
     * @return
     */
    int batchInsert(List<CurrencyInfoPO> list);
}
