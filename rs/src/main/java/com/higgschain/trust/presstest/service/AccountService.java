package com.higgschain.trust.presstest.service;

import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.presstest.AppConst;
import com.higgschain.trust.presstest.vo.*;
import com.higgschain.trust.rs.common.config.RsConfig;
import com.higgschain.trust.rs.core.api.RsCoreFacade;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.VersionEnum;
import com.higgschain.trust.slave.api.enums.account.FundDirectionEnum;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.account.*;
import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import java.util.Date;
import java.util.Random;

/**
 * The type Account service.
 *
 * @author liuyu
 * @description
 * @date 2018 -08-30
 */
@Service @Slf4j public class AccountService {

    /**
     * The Rs core facade.
     */
    @Autowired RsCoreFacade rsCoreFacade;
    /**
     * The Rs config.
     */
    @Autowired
    RsConfig rsConfig;

    /**
     * 创建币种
     *
     * @param vo the vo
     * @return the resp data
     */
    public RespData createCurrency(CurrencyVO vo) {
        IssueCurrency action = new IssueCurrency();
        action.setIndex(0);
        action.setType(ActionTypeEnum.ISSUE_CURRENCY);
        action.setCurrencyName(vo.getCurrencyName());
        action.setRemark(vo.getRemark());

        CoreTransaction coreTransaction = makeTx(vo.getReqNo(), action, AppConst.CREATE_CURRENCY);
        rsCoreFacade.processTx(coreTransaction);
        return rsCoreFacade.syncWait(vo.getReqNo(), true);
    }

    /**
     * 开户
     *
     * @param vo the vo
     * @return the resp data
     */
    public RespData openAccount(OpenAccountVO vo) {
        OpenAccount action = new OpenAccount();
        action.setIndex(0);
        action.setType(ActionTypeEnum.OPEN_ACCOUNT);
        action.setCurrency(vo.getCurrencyName());
        action.setAccountNo(vo.getAccountNo());
        action.setFundDirection(vo.getFundDirection() == 0 ? FundDirectionEnum.DEBIT : FundDirectionEnum.CREDIT);
        action.setDataOwner(rsConfig.getRsName());
        action.setChainOwner(AppConst.CHAIN_OWNER);

        CoreTransaction coreTransaction = makeTx(vo.getReqNo(), action, AppConst.OPEN_MY_ACCOUNT);
        rsCoreFacade.processTx(coreTransaction);
        return rsCoreFacade.syncWait(vo.getReqNo(), true);
    }

    /**
     * 转账
     *
     * @param vo the vo
     * @return the resp data
     */
    public RespData accounting(AccountingVO vo) {
        AccountOperation action = new AccountOperation();
        action.setIndex(0);
        action.setType(ActionTypeEnum.ACCOUNTING);
        action.setBizFlowNo("biz_flow_no_" + System.currentTimeMillis() + "-" + new Random().nextInt(1000));
        action.setDebitTradeInfo(vo.getDebitTradeInfo());
        action.setCreditTradeInfo(vo.getCreditTradeInfo());
        action.setAccountDate(new Date());
        action.setRemark("accounting for test");

        CoreTransaction coreTransaction = makeTx(vo.getReqNo(), action, AppConst.ACCOUNTING);
        rsCoreFacade.processTx(coreTransaction);
        return rsCoreFacade.syncWait(vo.getReqNo(), true);
    }

    /**
     * 冻结
     *
     * @param vo the vo
     * @return the resp data
     */
    public RespData freeze(FreezeVO vo) {
        AccountFreeze action = new AccountFreeze();
        action.setIndex(0);
        action.setType(ActionTypeEnum.FREEZE);
        action.setBizFlowNo(vo.getBizFlowNo());
        action.setAccountNo(vo.getAccountNo());
        action.setAmount(vo.getAmount());
        action.setRemark("freeze for test");

        CoreTransaction coreTransaction = makeTx(vo.getReqNo(), action, AppConst.FREEZE);
        rsCoreFacade.processTx(coreTransaction);
        return rsCoreFacade.syncWait(vo.getReqNo(), true);
    }

    /**
     * 解冻
     *
     * @param vo the vo
     * @return the resp data
     */
    public RespData unfreeze(UnFreezeVO vo) {
        AccountUnFreeze action = new AccountUnFreeze();
        action.setIndex(0);
        action.setType(ActionTypeEnum.UNFREEZE);
        action.setBizFlowNo(vo.getBizFlowNo());
        action.setAccountNo(vo.getAccountNo());
        action.setAmount(vo.getAmount());
        action.setRemark("unfreeze for test");

        CoreTransaction coreTransaction = makeTx(vo.getReqNo(), action, AppConst.UNFREEZE);
        rsCoreFacade.processTx(coreTransaction);
        return rsCoreFacade.syncWait(vo.getReqNo(), true);
    }

    /**
     * 构造 CoreTransaction 对象
     *
     * @param txId
     * @param action
     * @param policyId
     * @return
     */
    private CoreTransaction makeTx(String txId, Action action, String policyId) {
        CoreTransaction coreTransaction = new CoreTransaction();
        coreTransaction.setTxId(txId);
        coreTransaction.setPolicyId(policyId);
        coreTransaction.setBizModel(new JSONObject());
        coreTransaction.setActionList(Lists.newArrayList(action));
        coreTransaction.setSender(rsConfig.getRsName());
        coreTransaction.setVersion(VersionEnum.V1.getCode());
        coreTransaction.setSendTime(new Date());
        return coreTransaction;
    }
}
