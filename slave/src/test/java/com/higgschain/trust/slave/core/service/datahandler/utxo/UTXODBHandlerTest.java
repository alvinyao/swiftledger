package com.higgschain.trust.slave.core.service.datahandler.utxo;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.model.bo.utxo.TxIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Utxodb handler test.
 */
public class UTXODBHandlerTest extends BaseTest{
    @Autowired
    private UTXODBHandler utxodbHandler;

    /**
     * Test query tx out list.
     *
     * @throws Exception the exception
     */
    @Test
    public void testQueryTxOutList() throws Exception {
        List<TxIn> inputList = new ArrayList<>();
        TxIn txIn = new TxIn();
        txIn.setTxId("123123");
        txIn.setIndex(0);
        txIn.setActionIndex(0);
        TxIn txIn2 = new TxIn();
        txIn2.setTxId("123123");
        txIn2.setIndex(1);
        txIn2.setActionIndex(0);
        inputList.add(txIn);
        inputList.add(txIn2);
        System.out.println(utxodbHandler.queryUTXOList(inputList));


    }

}