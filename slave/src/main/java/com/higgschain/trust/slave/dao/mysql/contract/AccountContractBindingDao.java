package com.higgschain.trust.slave.dao.mysql.contract;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.contract.AccountContractBindingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * The interface Account contract binding dao.
 *
 * @author duhongming
 * @date 2018 /4/26
 */
@Mapper public interface AccountContractBindingDao extends BaseDao<AccountContractBindingPO> {

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(Collection<AccountContractBindingPO> list);

    /**
     * query AccountContractBinding list by accountNo
     *
     * @param accountNo the account no
     * @return list
     */
    List<AccountContractBindingPO> queryListByAccountNo(@Param("accountNo") String accountNo);

    /**
     * query one AccountContractBinding by hash
     *
     * @param hash the hash
     * @return account contract binding po
     */
    AccountContractBindingPO queryByHash(@Param("hash") String hash);
}
