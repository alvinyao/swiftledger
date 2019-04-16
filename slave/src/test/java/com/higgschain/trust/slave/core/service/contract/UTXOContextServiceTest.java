package com.higgschain.trust.slave.core.service.contract;

import com.google.common.collect.Lists;
import com.higgschain.trust.common.crypto.KeyPair;
import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.model.bo.utxo.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

/**
 * UTXOContextService test
 *
 * @author lingchao
 * @create 2018年09月03日14 :50
 */
public class UTXOContextServiceTest extends BaseTest {

    @Autowired
    private UTXOContextService utxoContextService;

    /**
     * Verify signature.
     */
    @Test
    public void verifySignature() {
        String masssage = "lingchao";
        List<Sign> signList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Sign sign = new Sign();
            KeyPair keyPair = CryptoUtil.getBizCrypto(null).generateKeyPair();
            sign.setPubKey(keyPair.getPubKey());
            sign.setSignature(CryptoUtil.getBizCrypto(null).sign(masssage, keyPair.getPriKey()));
            signList.add(sign);
        }

       // System.out.println("verify result: "+ utxoContextService.verifySignature(signList, masssage));

    }

}
