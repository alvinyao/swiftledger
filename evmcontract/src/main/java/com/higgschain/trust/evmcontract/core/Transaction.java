/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.higgschain.trust.evmcontract.core;


import com.higgschain.trust.evmcontract.crypto.ECKey;
import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * A transaction (formally, T) is a single cryptographically
 * signed instruction sent by an actor external to Ethereum.
 * An external actor can be a person (via a mobile device or desktop computer)
 * or could be from a piece of automated software running on a server.
 * There are two types of transactions: those which result in message calls
 * and those which result in the creation of new contracts.
 */
public class Transaction {

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private static final BigInteger DEFAULT_GAS_PRICE = new BigInteger("10000000000000");
    private static final BigInteger DEFAULT_BALANCE_GAS = new BigInteger("21000");

    /**
     * The constant HASH_LENGTH.
     */
    public static final int HASH_LENGTH = 32;
    /**
     * The constant ADDRESS_LENGTH.
     */
    public static final int ADDRESS_LENGTH = 20;

    /* SHA3 hash of the RLP encoded transaction */
    private byte[] hash;

    /* a counter used to make sure each transaction can only be processed once */
    private byte[] nonce;

    /* the amount of ether to transfer (calculated as wei) */
    private byte[] value;

    /* the address of the destination account
     * In creation transaction the receive address is - 0 */
    private byte[] receiveAddress;

    /* the amount of ether to pay as a transaction fee
     * to the miner for each unit of gas */
    private byte[] gasPrice;

    /* the amount of "gas" to allow for the computation.
     * Gas is the fuel of the computational engine;
     * every computational step taken and every byte added
     * to the state or transaction list consumes some gas. */
    private byte[] gasLimit;

    /* An unlimited size byte array specifying
     * input [data] of the message call or
     * Initialization code for a new contract */
    private byte[] data;

    /**
     * Since EIP-155, we could encode chainId in V
     */
    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;
    private Integer chainId = null;

    /* the elliptic curve signature
     * (including public key recovery bits) */
    private ECKey.ECDSASignature signature;

    /**
     * The Send address.
     */
    protected byte[] sendAddress;

    /* Tx in encoded form */
    /**
     * RLP(Recursive Length Prefix)，中文翻译过来叫递归长度前缀编码，它是以太坊序列化所采用的编码方式。RLP主要用于以太坊中数据的网络传输和持久化存储。
     */
    protected byte[] rlpEncoded;
    private byte[] rawHash;
    /**
     * The Parsed.
     */
    /* Indicates if this transaction has been parsed
     * from the RLP-encoded data */
    protected boolean parsed = false;

    /**
     * Instantiates a new Transaction.
     *
     * @param rawData the raw data
     */
    public Transaction(byte[] rawData) {
        this.rlpEncoded = rawData;
        parsed = false;
    }

    /**
     * Instantiates a new Transaction.
     *
     * @param nonce          the nonce
     * @param gasPrice       the gas price
     * @param gasLimit       the gas limit
     * @param receiveAddress the receive address
     * @param value          the value
     * @param data           the data
     * @param chainId        the chain id
     */
    public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value, byte[] data,
                       Integer chainId) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.receiveAddress = receiveAddress;
        if (ByteUtil.isSingleZero(value)) {
            this.value = ByteUtil.EMPTY_BYTE_ARRAY;
        } else {
            this.value = value;
        }
        this.data = data;
        this.chainId = chainId;

        if (receiveAddress == null) {
            this.receiveAddress = ByteUtil.EMPTY_BYTE_ARRAY;
        }

        parsed = true;
    }

    /**
     * Warning: this transaction would not be protected by replay-attack protection mechanism
     * Use {@link Transaction#Transaction(byte[], byte[], byte[], byte[], byte[], byte[], Integer)} constructor instead
     * and specify the desired chainID
     *
     * @param nonce          the nonce
     * @param gasPrice       the gas price
     * @param gasLimit       the gas limit
     * @param receiveAddress the receive address
     * @param value          the value
     * @param data           the data
     */
    public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value, byte[] data) {
        this(nonce, gasPrice, gasLimit, receiveAddress, value, data, null);
    }

    /**
     * Instantiates a new Transaction.
     *
     * @param nonce          the nonce
     * @param gasPrice       the gas price
     * @param gasLimit       the gas limit
     * @param receiveAddress the receive address
     * @param value          the value
     * @param data           the data
     * @param r              the r
     * @param s              the s
     * @param v              the v
     * @param chainId        the chain id
     */
    public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value, byte[] data,
                       byte[] r, byte[] s, byte v, Integer chainId) {
        this(nonce, gasPrice, gasLimit, receiveAddress, value, data, chainId);
        this.signature = ECKey.ECDSASignature.fromComponents(r, s, v);
    }

    /**
     * Warning: this transaction would not be protected by replay-attack protection mechanism
     * Use {@link Transaction#Transaction(byte[], byte[], byte[], byte[], byte[], byte[], byte[], byte[], byte, Integer)}
     * constructor instead and specify the desired chainID
     *
     * @param nonce          the nonce
     * @param gasPrice       the gas price
     * @param gasLimit       the gas limit
     * @param receiveAddress the receive address
     * @param value          the value
     * @param data           the data
     * @param r              the r
     * @param s              the s
     * @param v              the v
     */
    public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value, byte[] data,
                       byte[] r, byte[] s, byte v) {
        this(nonce, gasPrice, gasLimit, receiveAddress, value, data, r, s, v, null);
    }


    private Integer extractChainIdFromRawSignature(BigInteger bv, byte[] r, byte[] s) {
        if (r == null && s == null) {
            // EIP 86
            return bv.intValue();
        }
        if (bv.bitLength() > 31) {
            // chainId is limited to 31 bits, longer are not valid for now
            return Integer.MAX_VALUE;
        }
        long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return null;
        }
        return (int) ((v - CHAIN_ID_INC) / 2);
    }

    private byte getRealV(BigInteger bv) {
        if (bv.bitLength() > 31) {
            // chainId is limited to 31 bits, longer are not valid for now
            return 0;
        }
        long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return (byte) v;
        }
        byte realV = LOWER_REAL_V;
        int inc = 0;
        if ((int) v % 2 == 0) {
            inc = 1;
        }
        return (byte) (realV + inc);
    }

    /**
     * Verify.
     */
    public synchronized void verify() {
        rlpParse();
        validate();
    }

    /**
     * Rlp parse.
     */
    public synchronized void rlpParse() {
        if (parsed) {
            return;
        }
        try {
            RLPList decodedTxList = RLP.decode2(rlpEncoded);
            RLPList transaction = (RLPList) decodedTxList.get(0);

            // Basic verification
            if (transaction.size() > 9) {
                throw new RuntimeException("Too many RLP elements");
            }
            for (RLPElement rlpElement : transaction) {
                if (!(rlpElement instanceof RLPItem)) {
                    throw new RuntimeException("Transaction RLP elements shouldn't be lists");
                }
            }

            this.nonce = transaction.get(0).getRLPData();
            this.gasPrice = transaction.get(1).getRLPData();
            this.gasLimit = transaction.get(2).getRLPData();
            this.receiveAddress = transaction.get(3).getRLPData();
            this.value = transaction.get(4).getRLPData();
            this.data = transaction.get(5).getRLPData();
            // only parse signature in case tx is signed
            if (transaction.get(6).getRLPData() != null) {
                byte[] vData = transaction.get(6).getRLPData();
                BigInteger v = ByteUtil.bytesToBigInteger(vData);
                byte[] r = transaction.get(7).getRLPData();
                byte[] s = transaction.get(8).getRLPData();
                this.chainId = extractChainIdFromRawSignature(v, r, s);
                if (r != null && s != null) {
                    this.signature = ECKey.ECDSASignature.fromComponents(r, s, getRealV(v));
                }
            } else {
                logger.debug("RLP encoded tx is not signed!");
            }
            this.hash = HashUtil.sha3(rlpEncoded);
            this.parsed = true;
        } catch (Exception e) {
            throw new RuntimeException("Error on parsing RLP", e);
        }
    }

    private void validate() {
        if (getNonce().length > HASH_LENGTH) {
            throw new RuntimeException("Nonce is not valid");
        }
        if (receiveAddress != null && receiveAddress.length != 0 && receiveAddress.length != ADDRESS_LENGTH) {
            throw new RuntimeException("Receive address is not valid");
        }
        if (gasLimit.length > HASH_LENGTH) {
            throw new RuntimeException("Gas Limit is not valid");
        }
        if (gasPrice != null && gasPrice.length > HASH_LENGTH) {
            throw new RuntimeException("Gas Price is not valid");
        }
        if (value != null && value.length > HASH_LENGTH) {
            throw new RuntimeException("Value is not valid");
        }
        if (getSignature() != null) {
            if (BigIntegers.asUnsignedByteArray(signature.r).length > HASH_LENGTH) {
                throw new RuntimeException("Signature R is not valid");
            }
            if (BigIntegers.asUnsignedByteArray(signature.s).length > HASH_LENGTH) {
                throw new RuntimeException("Signature S is not valid");
            }
            if (getSender() != null && getSender().length != ADDRESS_LENGTH) {
                throw new RuntimeException("Sender is not valid");
            }
        }
    }

    /**
     * Is parsed boolean.
     *
     * @return the boolean
     */
    public boolean isParsed() {
        return parsed;
    }

    /**
     * Sets hash.
     *
     * @param hash the hash
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    /**
     * Get hash byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHash() {
        if (!isEmpty(hash)) {
            return hash;
        }
        rlpParse();
        getEncoded();
        return hash;
    }

    /**
     * Get raw hash byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getRawHash() {
        rlpParse();
        if (rawHash != null) {
            return rawHash;
        }
        byte[] plainMsg = this.getEncodedRaw();
        return rawHash = HashUtil.sha3(plainMsg);
    }

    /**
     * Get nonce byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getNonce() {
        rlpParse();

        return nonce == null ? ByteUtil.ZERO_BYTE_ARRAY : nonce;
    }

    /**
     * Sets nonce.
     *
     * @param nonce the nonce
     */
    protected void setNonce(byte[] nonce) {
        this.nonce = nonce;
        parsed = true;
    }

    /**
     * Is value tx boolean.
     *
     * @return the boolean
     */
    public boolean isValueTx() {
        rlpParse();
        return value != null;
    }

    /**
     * Get value byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getValue() {
        rlpParse();
        return value == null ? ByteUtil.ZERO_BYTE_ARRAY : value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    protected void setValue(byte[] value) {
        this.value = value;
        parsed = true;
    }

    /**
     * Get receive address byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getReceiveAddress() {
        rlpParse();
        return receiveAddress;
    }

    /**
     * Sets receive address.
     *
     * @param receiveAddress the receive address
     */
    protected void setReceiveAddress(byte[] receiveAddress) {
        this.receiveAddress = receiveAddress;
        parsed = true;
    }

    /**
     * Get gas price byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getGasPrice() {
        rlpParse();
        return gasPrice == null ? ByteUtil.ZERO_BYTE_ARRAY : gasPrice;
    }

    /**
     * Sets gas price.
     *
     * @param gasPrice the gas price
     */
    protected void setGasPrice(byte[] gasPrice) {
        this.gasPrice = gasPrice;
        parsed = true;
    }

    /**
     * Get gas limit byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getGasLimit() {
        rlpParse();
        return gasLimit == null ? ByteUtil.ZERO_BYTE_ARRAY : gasLimit;
    }

    /**
     * Sets gas limit.
     *
     * @param gasLimit the gas limit
     */
    protected void setGasLimit(byte[] gasLimit) {
        this.gasLimit = gasLimit;
        parsed = true;
    }

    /**
     * Non zero data bytes long.
     *
     * @return the long
     */
    public long nonZeroDataBytes() {
        if (data == null) {
            return 0;
        }
        int counter = 0;
        for (final byte aData : data) {
            if (aData != 0) {
                ++counter;
            }
        }
        return counter;
    }

    /**
     * Zero data bytes long.
     *
     * @return the long
     */
    public long zeroDataBytes() {
        if (data == null) {
            return 0;
        }
        int counter = 0;
        for (final byte aData : data) {
            if (aData == 0) {
                ++counter;
            }
        }
        return counter;
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        rlpParse();
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    protected void setData(byte[] data) {
        this.data = data;
        parsed = true;
    }

    /**
     * Gets signature.
     *
     * @return the signature
     */
    public ECKey.ECDSASignature getSignature() {
        rlpParse();
        return signature;
    }

    /**
     * Get contract address byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getContractAddress() {
        if (!isContractCreation()) {
            return null;
        }
        return HashUtil.calcNewAddr(this.getSender(), this.getNonce());
    }

    /**
     * Is contract creation boolean.
     *
     * @return the boolean
     */
    public boolean isContractCreation() {
        rlpParse();
        return this.receiveAddress == null || Arrays.equals(this.receiveAddress, ByteUtil.EMPTY_BYTE_ARRAY);
    }

    /*
     * Crypto
     */

    /**
     * Gets key.
     *
     * @return the key
     */
    public ECKey getKey() {
        byte[] hash = getRawHash();
        return ECKey.recoverFromSignature(signature.v, signature, hash);
    }

    /**
     * Get sender byte [ ].
     *
     * @return the byte [ ]
     */
    public synchronized byte[] getSender() {
        try {
            if (sendAddress == null && getSignature() != null) {
                sendAddress = ECKey.signatureToAddress(getRawHash(), getSignature());
            }
            return sendAddress;
        } catch (SignatureException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Gets chain id.
     *
     * @return the chain id
     */
    public Integer getChainId() {
        rlpParse();
        return chainId == null ? null : (int) chainId;
    }

    /**
     * Sign.
     *
     * @param privKeyBytes the priv key bytes
     * @throws MissingPrivateKeyException the missing private key exception
     * @deprecated should prefer #sign(ECKey) over this method
     */
    public void sign(byte[] privKeyBytes) throws ECKey.MissingPrivateKeyException {
        sign(ECKey.fromPrivate(privKeyBytes));
    }

    /**
     * Sign.
     *
     * @param key the key
     * @throws MissingPrivateKeyException the missing private key exception
     */
    public void sign(ECKey key) throws ECKey.MissingPrivateKeyException {
        this.signature = key.sign(this.getRawHash());
        this.rlpEncoded = null;
    }

    @Override
    public String toString() {
        return toString(Integer.MAX_VALUE);
    }

    /**
     * To string string.
     *
     * @param maxDataSize the max data size
     * @return the string
     */
    public String toString(int maxDataSize) {
        rlpParse();
        String dataS;
        if (data == null) {
            dataS = "";
        } else if (data.length < maxDataSize) {
            dataS = ByteUtil.toHexString(data);
        } else {
            dataS = ByteUtil.toHexString(Arrays.copyOfRange(data, 0, maxDataSize)) +
                    "... (" + data.length + " bytes)";
        }
        return "TransactionData [" + "hash=" + ByteUtil.toHexString(hash) +
                "  nonce=" + ByteUtil.toHexString(nonce) +
                ", gasPrice=" + ByteUtil.toHexString(gasPrice) +
                ", gas=" + ByteUtil.toHexString(gasLimit) +
                ", receiveAddress=" + ByteUtil.toHexString(receiveAddress) +
                ", sendAddress=" + ByteUtil.toHexString(getSender()) +
                ", value=" + ByteUtil.toHexString(value) +
                ", data=" + dataS +
                ", signatureV=" + (signature == null ? "" : signature.v) +
                ", signatureR=" + (signature == null ? "" : ByteUtil.toHexString(BigIntegers.asUnsignedByteArray(signature.r))) +
                ", signatureS=" + (signature == null ? "" : ByteUtil.toHexString(BigIntegers.asUnsignedByteArray(signature.s))) +
                "]";
    }

    /**
     * For signatures you have to keep also
     * RLP of the transaction without any signature data
     *
     * @return the byte [ ]
     */
    public byte[] getEncodedRaw() {

        rlpParse();
        byte[] rlpRaw;

        // parse null as 0 for nonce
        byte[] nonce = null;
        if (this.nonce == null || this.nonce.length == 1 && this.nonce[0] == 0) {
            nonce = RLP.encodeElement(null);
        } else {
            nonce = RLP.encodeElement(this.nonce);
        }
        byte[] gasPrice = RLP.encodeElement(this.gasPrice);
        byte[] gasLimit = RLP.encodeElement(this.gasLimit);
        byte[] receiveAddress = RLP.encodeElement(this.receiveAddress);
        byte[] value = RLP.encodeElement(this.value);
        byte[] data = RLP.encodeElement(this.data);

        // Since EIP-155 use chainId for v
        if (chainId == null) {
            rlpRaw = RLP.encodeList(nonce, gasPrice, gasLimit, receiveAddress,
                    value, data);
        } else {
            byte[] v, r, s;
            v = RLP.encodeInt(chainId);
            r = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
            s = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
            rlpRaw = RLP.encodeList(nonce, gasPrice, gasLimit, receiveAddress,
                    value, data, v, r, s);
        }
        return rlpRaw;
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

        // parse null as 0 for nonce
        byte[] nonce = null;
        if (this.nonce == null || this.nonce.length == 1 && this.nonce[0] == 0) {
            nonce = RLP.encodeElement(null);
        } else {
            nonce = RLP.encodeElement(this.nonce);
        }
        byte[] gasPrice = RLP.encodeElement(this.gasPrice);
        byte[] gasLimit = RLP.encodeElement(this.gasLimit);
        byte[] receiveAddress = RLP.encodeElement(this.receiveAddress);
        byte[] value = RLP.encodeElement(this.value);
        byte[] data = RLP.encodeElement(this.data);

        byte[] v, r, s;

        if (signature != null) {
            int encodeV;
            if (chainId == null) {
                encodeV = signature.v;
            } else {
                encodeV = signature.v - LOWER_REAL_V;
                encodeV += chainId * 2 + CHAIN_ID_INC;
            }
            v = RLP.encodeInt(encodeV);
            r = RLP.encodeElement(BigIntegers.asUnsignedByteArray(signature.r));
            s = RLP.encodeElement(BigIntegers.asUnsignedByteArray(signature.s));
        } else {
            // Since EIP-155 use chainId for v
            v = chainId == null ? RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY) : RLP.encodeInt(chainId);
            r = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
            s = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
        }

        this.rlpEncoded = RLP.encodeList(nonce, gasPrice, gasLimit,
                receiveAddress, value, data, v, r, s);

        this.hash = HashUtil.sha3(rlpEncoded);

        return rlpEncoded;
    }

    @Override
    public int hashCode() {

        byte[] hash = this.getHash();
        int hashCode = 0;

        for (int i = 0; i < hash.length; ++i) {
            hashCode += hash[i] * i;
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Transaction)) {
            return false;
        }
        Transaction tx = (Transaction) obj;

        return tx.hashCode() == this.hashCode();
    }

    /**
     * Create default transaction.
     *
     * @param to     the to
     * @param amount the amount
     * @param nonce  the nonce
     * @return the transaction
     * @deprecated Use {@link Transaction#createDefault(String, BigInteger, BigInteger, Integer)} instead
     */
    public static Transaction createDefault(String to, BigInteger amount, BigInteger nonce) {
        return create(to, amount, nonce, DEFAULT_GAS_PRICE, DEFAULT_BALANCE_GAS);
    }

    /**
     * Create default transaction.
     *
     * @param to      the to
     * @param amount  the amount
     * @param nonce   the nonce
     * @param chainId the chain id
     * @return the transaction
     */
    public static Transaction createDefault(String to, BigInteger amount, BigInteger nonce, Integer chainId) {
        return create(to, amount, nonce, DEFAULT_GAS_PRICE, DEFAULT_BALANCE_GAS, chainId);
    }

    /**
     * Create transaction.
     *
     * @param to       the to
     * @param amount   the amount
     * @param nonce    the nonce
     * @param gasPrice the gas price
     * @param gasLimit the gas limit
     * @return the transaction
     * @deprecated use {@link Transaction#create(String, BigInteger, BigInteger, BigInteger, BigInteger, Integer)} instead
     */
    public static Transaction create(String to, BigInteger amount, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transaction(BigIntegers.asUnsignedByteArray(nonce),
                BigIntegers.asUnsignedByteArray(gasPrice),
                BigIntegers.asUnsignedByteArray(gasLimit),
                Hex.decode(to),
                BigIntegers.asUnsignedByteArray(amount),
                null);
    }

    /**
     * Create transaction.
     *
     * @param to       the to
     * @param amount   the amount
     * @param nonce    the nonce
     * @param gasPrice the gas price
     * @param gasLimit the gas limit
     * @param chainId  the chain id
     * @return the transaction
     */
    public static Transaction create(String to, BigInteger amount, BigInteger nonce, BigInteger gasPrice,
                                     BigInteger gasLimit, Integer chainId) {
        return new Transaction(BigIntegers.asUnsignedByteArray(nonce),
                BigIntegers.asUnsignedByteArray(gasPrice),
                BigIntegers.asUnsignedByteArray(gasLimit),
                Hex.decode(to),
                BigIntegers.asUnsignedByteArray(amount),
                null,
                chainId);
    }

}
