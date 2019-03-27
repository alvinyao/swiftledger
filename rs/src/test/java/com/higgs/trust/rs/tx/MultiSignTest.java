package com.higgs.trust.rs.tx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higgs.trust.evmcontract.crypto.ECKey;
import com.higgs.trust.rs.core.vo.CreateCurrencyVO;
import com.higgs.trust.rs.core.vo.MultiSignHashVO;
import com.higgs.trust.rs.core.vo.MultiSignRuleVO;
import com.higgs.trust.rs.core.vo.MultiSignTxVO;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Su Jiulong
 * @date 2019/3/27
 */
public class MultiSignTest {
    private static final String CREATE_MULTI_CONTRACT = "http://localhost:7073/multiSign/create";
    private static final String GET_SOURCE_HASH = "http://localhost:7073/multiSign/getSignHash";
    private static final String CREATE_TOKEN = "http://localhost:7073/multiSign/createCurrency";
    private static final String TRANSFER = "http://localhost:7073/multiSign/transfer";
    private String priKey = "87385e10d018f971f66cf2c065663d4aa427286f259f85bb8b2438130f4f1ee7";
    private String owerAddress = "e53a3fd600be283181f7396c35da469050651be7";
    private String currency = "BTC";
    private String multiContractAddr = "8974c029d47571184439fab74d75e3bf6b22388e";
    private BigDecimal amount = new BigDecimal("100");
    private List<String> allAddress = new ArrayList<>(3);
    private List<String> mustAddress = new ArrayList<>(2);

    {
        allAddress.add("e53a3fd600be283181f7396c35da469050651be7");
        allAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
        allAddress.add("010f774849c57fcb9f73d159daa85c20e197f0d2");

        mustAddress.add("e53a3fd600be283181f7396c35da469050651be7");
        mustAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
    }

    @Test
    public void createMultiSignContractTest() {
        MultiSignRuleVO vo = new MultiSignRuleVO();


        vo.setAddrs(allAddress);
        vo.setMustAddrs(mustAddress);
        vo.setVerifyNum(2);
        vo.setRequestId("4df64fd98774472554c01150b1d69a3b174d342d62e9f4517b320198f4a9fde9");
        String result = HttpUtils.postJson(CREATE_MULTI_CONTRACT, JSON.toJSON(vo));
        System.out.println(result);
    }

    @Test
    public void createTokenTest() {
        CreateCurrencyVO vo = new CreateCurrencyVO();
        vo.setAddress(owerAddress);
        vo.setAmount(new BigDecimal("1000000000000000000"));
        vo.setCurrency("aaa");
        vo.setRequestId("4df64fd98774492554101150b1d69a3b174d342d62e9f4517b320198f4a91de9");
        String result = HttpUtils.postJson(CREATE_TOKEN, JSON.toJSON(vo));
        System.out.println(result);
    }

    @Test
    public void transferTest() {
        MultiSignTxVO vo = new MultiSignTxVO();
        vo.setCurrency("aaa");
        vo.setAmount(amount);
        vo.setFromAddr(owerAddress);
        vo.setToAddr(multiContractAddr);
        vo.setMultiSign(false);
        vo.setRequestId("4df64f198774472551c21231b116913b174d342d61e1f4517b320198f4091de9");
        byte[] sourceHash = getSourceHash(owerAddress, multiContractAddr, amount, currency);
        if (vo.isMultiSign()) {
            vo.setSigns(getMultiSignList(sourceHash));
        } else {
            String sign = ECKey.fromPrivate(Hex.decode(priKey)).sign(sourceHash).toHex();
            ArrayList<String> list = new ArrayList<>(1);
            list.add(sign);
            vo.setSigns(list);
        }
        System.out.println(JSON.toJSON(vo));
        String result = HttpUtils.postJson(TRANSFER, JSON.toJSON(vo));
        System.out.println(result);
    }

    private byte[] getSourceHash(String from, String to, BigDecimal amount, String currency) {
        MultiSignHashVO vo = new MultiSignHashVO();
        vo.setFromAddr(from);
        vo.setToAddr(to);
        vo.setAmount(amount);
        vo.setCurrency(currency);
        String result = HttpUtils.postJson(GET_SOURCE_HASH, JSON.toJSON(vo));
        JSONObject jsonObject = JSON.parseObject(result);
        String sourceHash = (String) jsonObject.get("data");
        if (sourceHash == null) {
            throw new RuntimeException("get source hash error");
        }
        System.out.println(sourceHash);
        return Hex.decode(sourceHash);
    }

    private List<String> getMultiSignList(byte[] sourceHash) {
        String[] pris = {
                "87385e10d018f971f66cf2c065663d4aa427286f259f85bb8b2438130f4f1ee7",
                "3e465c393aebac5b5d4e0c770179723db57a41b7a3e5e75dd98cbc009174135e",
                "3da393045ba96c4ca49479663c327278634b603497b850af716a7bcbaad1a098"
        };
        List<String> list = new ArrayList<>(allAddress.size());
        for (int i = 0; i < pris.length; i++) {
            list.add(ECKey.fromPrivate(Hex.decode(pris[i])).sign(sourceHash).toHex());
        }
        return list;
    }
}
