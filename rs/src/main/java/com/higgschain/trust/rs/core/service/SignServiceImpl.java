package com.higgschain.trust.rs.core.service;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.rs.common.config.RsConfig;
import com.higgschain.trust.rs.core.api.SignService;
import com.higgschain.trust.slave.api.enums.TxTypeEnum;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Sign service.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
@Service
@Slf4j
public class SignServiceImpl implements SignService {
    @Autowired
    private RsConfig rsConfig;
    @Autowired
    private NodeState nodeState;

    @Override
    public SignInfo signTx(CoreTransaction coreTx) {
        String coreTxJSON = JSON.toJSONString(coreTx);
        if (log.isDebugEnabled()) {
            log.debug("[signTx]txId:{},coreTxJSON:{}", coreTx.getTxId(), coreTxJSON);
        }
        SignInfo signInfo = new SignInfo();
        signInfo.setOwner(rsConfig.getRsName());
        //check tx type for consensus
        if (TxTypeEnum.isTargetType(coreTx.getTxType(), TxTypeEnum.NODE)) {
            signInfo.setSignType(SignInfo.SignTypeEnum.CONSENSUS);
        } else {
            signInfo.setSignType(SignInfo.SignTypeEnum.BIZ);
        }
        signInfo.setSign(sign(coreTxJSON, signInfo.getSignType()));
        return signInfo;
    }

    @Override
    public String sign(String signValue, SignInfo.SignTypeEnum signTypeEnum) {
        if (signTypeEnum == SignInfo.SignTypeEnum.CONSENSUS) {
            return CryptoUtil.getProtocolCrypto().sign(signValue, nodeState.getConsensusPrivateKey());
        } else {
            return CryptoUtil.getBizCrypto(null).sign(signValue, nodeState.getPrivateKey());
        }
    }
}
