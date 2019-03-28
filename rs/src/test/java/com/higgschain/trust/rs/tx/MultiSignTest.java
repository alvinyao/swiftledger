package com.higgschain.trust.rs.tx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.evmcontract.crypto.ECKey;
import com.higgschain.trust.rs.core.vo.CreateCurrencyVO;
import com.higgschain.trust.rs.core.vo.MultiSignHashVO;
import com.higgschain.trust.rs.core.vo.MultiSignRuleVO;
import com.higgschain.trust.rs.core.vo.MultiSignTxVO;
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
    private String multiContractAddr = "cdfbe82b2ebe76a80c8e3eeac5789bab801f93ff";
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
        vo.setRequestId("4df64fd98714472554201150b1d69a3b174d342d62e9f4517b320198f4a9fde9");
        String result = HttpUtils.postJson(CREATE_MULTI_CONTRACT, JSON.toJSON(vo));
        System.out.println(result);
    }

    @Test
    public void createTokenTest() {
        CreateCurrencyVO vo = new CreateCurrencyVO();
        vo.setAddress(owerAddress);
        vo.setAmount(new BigDecimal("1000000000000000000"));
        vo.setCurrency("AB");
        vo.setRequestId("4df64f198774492554101250b1d69a3b174d342d62e9f4517b320198f4a91de9");
        String result = HttpUtils.postJson(CREATE_TOKEN, JSON.toJSON(vo));
        System.out.println(result);
    }

    @Test
    public void transferTest() {
        MultiSignTxVO vo = new MultiSignTxVO();
        vo.setCurrency("AB");
        vo.setAmount(new BigDecimal("100000"));
        vo.setFromAddr(multiContractAddr);
        vo.setToAddr(owerAddress);
        vo.setMultiSign(true);
        vo.setRequestId("4df64f198774172551c21231b111913b174d142d62e1f4017b320198f4091de9");
        byte[] sourceHash = getSourceHash(vo.getFromAddr(), vo.getToAddr(), vo.getAmount(), null);
//        byte[] sourceHash = getSourceHash(vo.getFromAddr(), vo.getToAddr(), vo.getAmount(), vo.getCurrency());
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
            list.add(ECKey.fromPrivate(Hex.decode(pris[0])).sign(sourceHash).toHex());
//            if (i==0){
//               list.set(i, 2+list.get(i).substring(1));
//            }
        }
        return list;
    }
}
