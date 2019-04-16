package com.higgschain.trust.slave.dao.mysql.manage;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.manage.RsNodePO;
import com.higgschain.trust.slave.model.bo.manage.RsPubKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Rs node dao.
 *
 * @author tangfashuang
 * @date 2018 /03/28
 * @desc rs node dao
 */
@Mapper public interface RsNodeDao extends BaseDao<RsNodePO> {
    /**
     * query rs_node by rsId
     *
     * @param rsId the rs id
     * @return rs node po
     */
    RsNodePO queryByRsId(String rsId);

    /**
     * query all rs_node
     *
     * @return list
     */
    List<RsNodePO> queryAll();

    /**
     * query all rs and public key when status is 'COMMON'
     *
     * @return list
     */
    List<RsPubKey> queryRsAndPubKey();

    /**
     * batch insert
     *
     * @param rsNodePOList the rs node po list
     * @return int
     */
    int batchInsert(List<RsNodePO> rsNodePOList);

    /**
     * batch update
     *
     * @param rsNodePOList the rs node po list
     * @return int
     */
    int batchUpdate(List<RsNodePO> rsNodePOList);

}
