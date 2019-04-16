package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.RequestPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Request dao.
 */
@Mapper
public interface RequestDao extends BaseDao<RequestPO> {
    /**
     * 根据requestId 查询请求数据
     *
     * @param requestId the request id
     * @return request po
     */
    RequestPO queryByRequestId(@Param("requestId") String requestId);

    /**
     * 更改请求状态
     *
     * @param requestId  the request id
     * @param fromStatus the from status
     * @param toStatus   the to status
     * @param respCode   the resp code
     * @param respMsg    the resp msg
     * @return int
     */
    int updateStatusByRequestId(@Param("requestId") String requestId, @Param("fromStatus")String fromStatus, @Param("toStatus")String toStatus, @Param("respCode") String respCode, @Param("respMsg") String respMsg);

    /**
     * batch insert
     *
     * @param requestPOList the request po list
     * @return int
     */
    int batchInsert(List<RequestPO> requestPOList);
}
