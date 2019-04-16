package com.higgschain.trust.rs.tx.sender;

import com.higgschain.trust.rs.core.bo.ContractQueryRequestV2;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * The interface Transaction sender.
 *
 * @author Chen Jiawei
 * @date 2019 -01-03
 */
public interface ITransactionSender {
    /**
     * Post call.
     *
     * @param signedTransaction the signed transaction
     * @return the call
     */
    @POST("/transaction/post")
    Call<RespData> post(@Body SignedTransaction signedTransaction);

    /**
     * Post call.
     *
     * @param contractQueryRequestV2 the contract query request v 2
     * @return the call
     */
    @POST("/contract/query2")
    Call<RespData> post(@Body ContractQueryRequestV2 contractQueryRequestV2);
}
