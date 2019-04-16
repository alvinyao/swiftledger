package com.higgschain.trust.rs.tx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.common.crypto.ecc.CryptoUtils;
import com.higgschain.trust.evmcontract.crypto.ECKey;
import com.higgschain.trust.rs.core.bo.ContractQueryRequestV2;
import com.higgschain.trust.rs.core.vo.CreateCurrencyVO;
import com.higgschain.trust.rs.core.vo.MultiSignHashVO;
import com.higgschain.trust.rs.core.vo.MultiSignRuleVO;
import com.higgschain.trust.rs.core.vo.MultiSignTxVO;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The type Multi sign test.
 *
 * @author Su Jiulong
 * @date 2019 /3/27
 */
public class MultiSignTest {
    private static final String CREATE_MULTI_CONTRACT = "http://localhost:7073/multiSign/create";
    private static final String GET_SOURCE_HASH = "http://localhost:7073/multiSign/getSignHash";
    private static final String CREATE_TOKEN = "http://localhost:7073/multiSign/createCurrency";
    private static final String TRANSFER = "http://localhost:7073/multiSign/transfer";
    private static final String QUERY_BALANCE = "http://localhost:7073/contract/query2";
    private static final String CURRENCY_ADDRESS = "f642d7729efc98cea252f7967fd6ccca733935dc";

    private String priKey = "87385e10d018f971f66cf2c065663d4aa427286f259f85bb8b2438130f4f1ee7";
    private String owerAddress = "e53a3fd600be283181f7396c35da469050651be7";
    private String multiContractAddr = "53bff8cda24f15d5077b9b28420428b8b7c2bfbf";
    private BigDecimal amount = new BigDecimal("1000000000");
    private static final String CURRENCY = "AB";
    private List<String> allAddress = new ArrayList<>(3);
    private List<String> mustAddress = new ArrayList<>(2);

    {
        allAddress.add("e53a3fd600be283181f7396c35da469050651be7");
        allAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
        allAddress.add("010f774849c57fcb9f73d159daa85c20e197f0d2");

        mustAddress.add("e53a3fd600be283181f7396c35da469050651be7");
        mustAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
    }

    /**
     * Create multi sign contract test.
     */
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

    /**
     * Create token test.
     */
    @Test
    public void createTokenTest() {
        CreateCurrencyVO vo = new CreateCurrencyVO();
        vo.setAddress(owerAddress);
        vo.setAmount(amount);
        vo.setCurrency(CURRENCY);
        vo.setRequestId(generate64TxId(vo.toString()));
        String result = HttpUtils.postJson(CREATE_TOKEN, JSON.toJSON(vo));
        System.out.println(result);
    }

    /**
     * Transfer test.
     */
    @Test
    public void transferTest() {
        MultiSignTxVO vo = new MultiSignTxVO();
        vo.setCurrency(CURRENCY);
        vo.setAmount(new BigDecimal("10"));
        vo.setFromAddr(multiContractAddr);
        vo.setToAddr(owerAddress);
        vo.setMultiSign(true);
//        vo.setMultiSign(false);
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
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queryBalance(vo.getFromAddr());
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
        byte[] signBytes = null;
        for (int i = 0; i < pris.length; i++) {
//            if (i == 0) {
//                continue;
//            }
            signBytes = ECKey.fromPrivate(Hex.decode(pris[i])).sign(sourceHash).toByteArray();
//            if (i>=0){
//                signBytes[7] = signBytes[9];
//            }
            list.add(Hex.toHexString(signBytes));
        }
        return list;
    }

    private String generate64TxId(String originalTxId) {
        originalTxId = originalTxId + System.currentTimeMillis();
        byte[] txIdBytes = originalTxId.getBytes();
        byte[] txIdHash256 = CryptoUtils.sha256hashTwice256(txIdBytes);
        return CryptoUtils.HEX.encode(txIdHash256);
    }

    private void queryBalance(String adddress) {
        ContractQueryRequestV2 queryRequestV2 = new ContractQueryRequestV2();
        queryRequestV2.setAddress(CURRENCY_ADDRESS);
        queryRequestV2.setMethodSignature("(uint,uint) balanceOf(address)");
        queryRequestV2.setParameters(new Object[]{adddress});
        String result = HttpUtils.postJson(QUERY_BALANCE, JSON.toJSON(queryRequestV2));
        JSONObject jsonObject = JSON.parseObject(result);
        BigDecimal data = new BigDecimal(((List) jsonObject.get("data")).get(0).toString()).scaleByPowerOfTen(-8);
        data = "0E-8".equals(data.toString()) ? BigDecimal.ZERO : data;
        System.out.println(data);
    }

    /**
     * Gets balance test.
     */
    @Test
    public void getBalanceTest() {
        queryBalance(multiContractAddr);
    }
}
