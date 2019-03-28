package com.higgschain.trust.rs.tx.sender;

import com.higgschain.trust.rs.core.bo.ContractQueryRequestV2;
import com.higgschain.trust.slave.api.vo.RespData;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Chen Jiawei
 * @date 2019-01-03
 */
public interface ITransactionSender {
    @POST("/transaction/post")
    Call<RespData> post(@Body SignedTransaction signedTransaction);

    @POST("/contract/query2")
    Call<RespData> post(@Body ContractQueryRequestV2 contractQueryRequestV2);
}
