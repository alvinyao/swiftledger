package com.higgschain.trust.slave.dao.mysql.contract;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.contract.ContractStatePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * The interface Contract state dao.
 */
@Mapper
public interface ContractStateDao extends BaseDao<ContractStatePO> {

    /**
     * Batch insert int.
     *
     * @param list the list
     * @return the int
     */
    int batchInsert(Collection<ContractStatePO> list);

    /**
     * Batch update int.
     *
     * @param list the list
     * @return the int
     */
    int batchUpdate(Collection<ContractStatePO> list);

    /**
     * Save int.
     *
     * @param state the state
     * @return the int
     */
    int save(ContractStatePO state);

    /**
     * Delete by address int.
     *
     * @param address the address
     * @return the int
     */
    int deleteByAddress(@Param("address") String address);

    /**
     * Query by address contract state po.
     *
     * @param address the address
     * @return the contract state po
     */
    ContractStatePO queryByAddress(@Param("address") String address);
}
