package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.core.bo.ContractCreateV2Request;
import com.higgschain.trust.rs.core.bo.ContractMigrationRequest;
import com.higgschain.trust.rs.core.bo.ContractQueryRequest;
import com.higgschain.trust.slave.api.vo.ContractVO;
import com.higgschain.trust.slave.api.vo.PageVO;
import com.higgschain.trust.slave.api.vo.QueryContractVO;
import com.higgschain.trust.common.vo.RespData;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Contract service.
 *
 * @author duhongming
 * @date 2018 /5/14
 */
public interface ContractService {
    /**
     * deploy contract
     *
     * @param txId     the tx id
     * @param code     the code
     * @param initArgs the init args
     * @return resp data
     */
    RespData deploy(String txId, String code, Object... initArgs);

    /**
     * deploy contract
     *
     * @param request the request
     * @return resp data
     */
    RespData deployV2(ContractCreateV2Request request);

    /**
     * query contract list
     *
     * @param height    the height
     * @param txId      the tx id
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return page vo
     */
    @Deprecated
    PageVO<ContractVO> queryList(Long height, String txId, Integer pageIndex, Integer pageSize);

    /**
     * query contract list
     *
     * @param queryContractVO the query contract vo
     * @return list
     */
    List<ContractVO> queryContractsByPage(QueryContractVO queryContractVO);

    /**
     * invoke contract
     *
     * @param txId    the tx id
     * @param address the address
     * @param args    the args
     * @return the resp data
     */
    RespData invoke(String txId, String address, Object... args);

    /**
     * invoke v2 contract
     *
     * @param txId            the tx id
     * @param from            the from
     * @param to              the to
     * @param value           the value
     * @param methodSignature the method signature
     * @param args            the args
     * @return resp data
     */
    RespData invokeV2(String txId, String from, String to, BigDecimal value, String methodSignature, Object... args);

    /**
     * migration contract state
     *
     * @param migrationRequest the migration request
     * @return resp data
     */
    RespData migration(ContractMigrationRequest migrationRequest);

    /**
     * query contract state
     *
     * @param request the request
     * @return object
     */
    Object query(ContractQueryRequest request);

    /**
     * query by txId and action index
     *
     * @param txId        the tx id
     * @param actionIndex the action index
     * @return contract vo
     */
    ContractVO queryByTxId(String txId,int actionIndex);
}
