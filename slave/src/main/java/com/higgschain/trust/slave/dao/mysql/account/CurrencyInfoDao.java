package com.higgschain.trust.slave.dao.mysql.account;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.account.CurrencyInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Currency info dao.
 *
 * @author liuyu
 * @description currency DAO
 * @date 2018 -03-27
 */
@Mapper public interface CurrencyInfoDao extends BaseDao<CurrencyInfoPO> {
    /**
     * query by currency
     *
     * @param currency the currency
     * @return currency info po
     */
    CurrencyInfoPO queryByCurrency(@Param("currency") String currency);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(List<CurrencyInfoPO> list);
}
