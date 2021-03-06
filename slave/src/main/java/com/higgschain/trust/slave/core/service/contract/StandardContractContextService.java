package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.contract.ContractApiService;
import com.higgschain.trust.contract.ExecuteContext;
import com.higgschain.trust.slave.api.BlockChainService;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.core.service.action.account.AccountUnFreezeHandler;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.account.AccountUnFreeze;
import com.higgschain.trust.slave.model.bo.context.ActionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * contract context service
 *
 * @author duhongming
 * @date 2018 -04-17
 */
@Slf4j @Service public class StandardContractContextService extends ContractApiService {
    /**
     * The Account un freeze handler.
     */
    @Autowired AccountUnFreezeHandler accountUnFreezeHandler;
    /**
     * The Block chain service.
     */
    @Autowired BlockChainService blockChainService;

    @Override
    public ExecuteContext getContext() {
        return super.getContext();
    }

    /**
     * execute unfreeze by js
     *
     * @param bizFlowNo the biz flow no
     * @param accountNo the account no
     * @param amount    the amount
     * @param remark    the remark
     */
    public void unFreeze(String bizFlowNo,String accountNo,BigDecimal amount,String remark){
        AccountUnFreeze bo = new AccountUnFreeze();
        bo.setType(ActionTypeEnum.UNFREEZE);
        bo.setIndex(1);
        bo.setBizFlowNo(bizFlowNo);
        bo.setAccountNo(accountNo);
        bo.setAmount(amount);
        bo.setRemark(remark);

        ActionData actionData = getContextData(StandardExecuteContextData.class).getAction();

        accountUnFreezeHandler.unFreeze(bo,actionData.getCurrentBlock().getBlockHeader().getHeight());
    }

    /**
     * get max block header
     *
     * @return block header
     */
    public BlockHeader getMaxBlockHeader(){
        return blockChainService.getMaxBlockHeader();
    }

    /**
     * get max block height
     *
     * @return long
     */
    public Long getMaxBlockHeight(){
        return blockChainService.getMaxBlockHeight();
    }

    /**
     * get current package time
     *
     * @return long
     */
    public Long getPackageTime(){
        ActionData actionData = getContextData(StandardExecuteContextData.class).getAction();
        return actionData.getCurrentPackage().getPackageTime();
    }

    /**
     * get current package height
     *
     * @return long
     */
    public Long getPackageHeight(){
        ActionData actionData = getContextData(StandardExecuteContextData.class).getAction();
        return actionData.getCurrentPackage().getHeight();
    }
}
