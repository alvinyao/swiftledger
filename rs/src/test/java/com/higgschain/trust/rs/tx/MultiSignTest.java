package com.higgschain.trust.rs.tx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.common.crypto.ecc.CryptoUtils;
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
    private String multiContractAddr = "24a4b09676068bd6a9bc14bf80b5eca2ac66a7d8";
    private BigDecimal amount = new BigDecimal("100");
    private List<String> allAddress = new ArrayList<>(3);
    private List<String> mustAddress = new ArrayList<>(2);

    {
        allAddress.add("e53a3fd600be283181f7396c35da469050651be7");
        allAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
        allAddress.add("010f774849c57fcb9f73d159daa85c20e197f0d2");

//        mustAddress.add("e53a3fd600be283181f7396c35da469050651be7");
//        mustAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
    }

    @Test
    public void createMultiSignContractTest() {
        MultiSignRuleVO vo = new MultiSignRuleVO();
        vo.setAddrs(allAddress);
        vo.setMustAddrs(mustAddress);
        vo.setVerifyNum(2);
        vo.setRequestId(generate64TxId(vo.toString()));
        String result = HttpUtils.postJson(CREATE_MULTI_CONTRACT, JSON.toJSON(vo));
        System.out.println(result);
    }

    @Test
    public void createTokenTest() {
        CreateCurrencyVO vo = new CreateCurrencyVO();
        vo.setAddress(owerAddress);
        vo.setAmount(new BigDecimal("1000000000000000000"));
        vo.setCurrency("AB");
        vo.setRequestId(generate64TxId(vo.toString()));
        String result = HttpUtils.postJson(CREATE_TOKEN, JSON.toJSON(vo));
        System.out.println(result);
    }

    @Test
    public void transferTest() {
        MultiSignTxVO vo = new MultiSignTxVO();
        vo.setCurrency("AB");
        vo.setAmount(new BigDecimal("10"));
        vo.setFromAddr(multiContractAddr);
        vo.setToAddr(owerAddress);
        vo.setMultiSign(true);
        vo.setRequestId(generate64TxId(vo.toString()));
        byte[] sourceHash = getSourceHash(vo.getFromAddr(), vo.getToAddr(), vo.getAmount(), null);
//        byte[] sourceHash = getSourceHash(vo.getFromAddr(), vo.getToAddr(), vo.getAmount(), vo.getCurrency());
        System.out.println(Hex.toHexString(sourceHash));
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
//            if (i==1){
//                continue;
//            }
            list.add(ECKey.fromPrivate(Hex.decode(pris[0])).sign(sourceHash).toHex());
        }
        return list;
    }

    private String generate64TxId(String originalTxId) {
        originalTxId = originalTxId + System.currentTimeMillis();
        byte[] txIdBytes = originalTxId.getBytes();
        byte[] txIdHash256 = CryptoUtils.sha256hashTwice256(txIdBytes);
        return CryptoUtils.HEX.encode(txIdHash256);
    }
}
