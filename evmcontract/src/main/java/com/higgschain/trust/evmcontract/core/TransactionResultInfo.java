package com.higgschain.trust.evmcontract.core;

import com.higgschain.trust.evmcontract.solidity.Abi;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.util.RLP;
import com.higgschain.trust.evmcontract.util.RLPElement;
import com.higgschain.trust.evmcontract.util.RLPList;
import com.higgschain.trust.evmcontract.vm.LogInfo;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Transaction result info.
 *
 * @author duhongming
 * @date 2018 /12/4
 */
public class TransactionResultInfo {
    private long blockHeight;
    private byte[] txHash;
    private int index;
    private Bloom bloomFilter;
    private List<LogInfo> logInfoList;
    private byte[] result = ByteUtil.EMPTY_BYTE_ARRAY;
    private String error = "";
    private byte[] createdAddress = ByteUtil.EMPTY_BYTE_ARRAY;
    private String invokeMethod = "";
    private byte[] rlpEncoded;

    /**
     * Instantiates a new Transaction result info.
     *
     * @param blockHeight the block height
     * @param txHash      the tx hash
     * @param index       the index
     * @param bloomFilter the bloom filter
     * @param logInfoList the log info list
     * @param result      the result
     */
    public TransactionResultInfo(long blockHeight, byte[] txHash, int index, Bloom bloomFilter,
                                 List<LogInfo> logInfoList, byte[] result) {
        this.blockHeight = blockHeight;
        this.txHash = txHash;
        this.index = index;
        this.bloomFilter = bloomFilter;
        this.logInfoList = logInfoList;
        this.result = result == null ? ByteUtil.EMPTY_BYTE_ARRAY : result;
    }

    /**
     * Instantiates a new Transaction result info.
     *
     * @param rlp the rlp
     */
    public TransactionResultInfo(final byte[] rlp) {
        if (rlp.length == 0) {
            return;
        }

        RLPList rlpList = (RLPList) RLP.decode2(rlp).get(0);
        BigInteger integer = RLP.decodeBigInteger(rlpList.get(0).getRLPData(), 0);
        blockHeight = integer.longValue();
        txHash = rlpList.get(1).getRLPData();
        BigInteger indexInteger = RLP.decodeBigInteger(rlpList.get(2).getRLPData(), 0);
        index = indexInteger.intValue();
        bloomFilter = new Bloom(rlpList.get(3).getRLPData());

        List<LogInfo> logInfos = new ArrayList<>();
        for (RLPElement logInfoEl : (RLPList) rlpList.get(4)) {
            LogInfo logInfo = new LogInfo(logInfoEl.getRLPData());
            logInfos.add(logInfo);
        }
        logInfoList = logInfos;

        result = rlpList.get(5).getRLPData();
        createdAddress = rlpList.get(6).getRLPData();
        error = decodeString(rlpList.get(7).getRLPData());
        invokeMethod = decodeString(rlpList.get(8).getRLPData());
        rlpEncoded = rlp;
    }

    private String decodeString(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return new String(data);
    }

    /**
     * Get encoded byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getEncoded() {
        if (rlpEncoded != null) {
            return rlpEncoded;
        }

        byte[] bloomFilterRLP = RLP.encodeElement(this.bloomFilter.getData());
        final byte[] logInfoListRLP;
        if (logInfoList != null) {
            byte[][] logInfoListE = new byte[logInfoList.size()][];
            int i = 0;
            for (LogInfo logInfo : logInfoList) {
                logInfoListE[i] = logInfo.getEncoded();
                ++i;
            }
            logInfoListRLP = RLP.encodeList(logInfoListE);
        } else {
            logInfoListRLP = RLP.encodeList();
        }


        rlpEncoded = RLP.encodeList(
                RLP.encodeElement(RLP.encodeBigInteger(BigInteger.valueOf(blockHeight))),
                RLP.encodeElement(txHash),
                RLP.encodeElement(RLP.encodeBigInteger(BigInteger.valueOf(index))),
                bloomFilterRLP,
                logInfoListRLP,
                RLP.encodeElement(result),
                RLP.encodeElement(createdAddress),
                RLP.encodeString(error == null ? "" : error),
                RLP.encodeString(invokeMethod == null ? "" : invokeMethod)
        );
        return rlpEncoded;
    }

    /**
     * Gets block height.
     *
     * @return the block height
     */
    public long getBlockHeight() {
        return blockHeight;
    }

    /**
     * Sets block height.
     *
     * @param blockHeight the block height
     */
    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    /**
     * Get tx hash byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getTxHash() {
        return txHash;
    }

    /**
     * Sets tx hash.
     *
     * @param txHash the tx hash
     */
    public void setTxHash(byte[] txHash) {
        this.txHash = txHash;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets bloom filter.
     *
     * @return the bloom filter
     */
    public Bloom getBloomFilter() {
        return bloomFilter;
    }

    /**
     * Sets bloom filter.
     *
     * @param bloomFilter the bloom filter
     */
    public void setBloomFilter(Bloom bloomFilter) {
        this.bloomFilter = bloomFilter;
    }

    /**
     * Gets log info list.
     *
     * @return the log info list
     */
    public List<LogInfo> getLogInfoList() {
        return logInfoList;
    }

    /**
     * Sets log info list.
     *
     * @param logInfoList the log info list
     */
    public void setLogInfoList(List<LogInfo> logInfoList) {
        this.logInfoList = logInfoList;
    }

    /**
     * Get result byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(byte[] result) {
        this.result = result;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Get created address byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCreatedAddress() {
        return createdAddress;
    }

    /**
     * Sets created address.
     *
     * @param createdAddress the created address
     */
    public void setCreatedAddress(byte[] createdAddress) {
        this.createdAddress = createdAddress;
    }

    /**
     * Gets invoke method.
     *
     * @return the invoke method
     */
    public String getInvokeMethod() {
        return invokeMethod;
    }

    /**
     * Sets invoke method.
     *
     * @param invokeMethod the invoke method
     */
    public void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    /**
     * To map map.
     *
     * @return the map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(9);
        map.put("txId", new String(txHash));
        map.put("blockHeight", blockHeight);
        map.put("success", StringUtils.isEmpty(error) ? true : false);
        map.put("error", error);
        map.put("logInfoList", logInfoList);
        if (result != null && result.length > 0) {
            if(StringUtils.isNotEmpty(invokeMethod)) {
                Abi.Function func = Abi.Function.of(invokeMethod);
                map.put("result", func.decodeResult(result));
            } else {
                map.put("result", Hex.toHexString(result));
            }
        }
        if (createdAddress != null && createdAddress.length > 0) {
            map.put("createdAddress", Hex.toHexString(createdAddress));
        }
        if (StringUtils.isNotEmpty(invokeMethod)) {
            map.put("invokeMethod", invokeMethod);
        }

        return map;
    }
}
