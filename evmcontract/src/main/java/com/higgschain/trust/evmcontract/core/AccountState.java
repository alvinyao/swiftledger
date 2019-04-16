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

import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.util.FastByteComparisons;
import com.higgschain.trust.evmcontract.util.RLP;
import com.higgschain.trust.evmcontract.util.RLPList;

import java.math.BigInteger;

import static com.higgschain.trust.evmcontract.util.ByteUtil.toHexString;

/**
 * The type Account state.
 */
public class AccountState {

    private byte[] rlpEncoded;

    /* A value equal to the number of transactions sent
     * from this address, or, in the case of contract accounts,
     * the number of contract-creations made by this account */
    private final BigInteger nonce;

    /* A scalar value equal to the number of Wei owned by this address */
    private final BigInteger balance;

    /* A 256-bit hash of the root node of a trie structure
     * that encodes the storage contents of the contract,
     * itself a simple mapping between byte arrays of size 32.
     * The hash is formally denoted σ[a] s .
     *
     * Since I typically wish to refer not to the trie’s root hash
     * but to the underlying set of key/value pairs stored within,
     * I define a convenient equivalence TRIE (σ[a] s ) ≡ σ[a] s .
     * It shall be understood that σ[a] s is not a ‘physical’ member
     * of the account and does not contribute to its later serialisation */
    private final byte[] stateRoot;

    /* The hash of the EVM code of this contract—this is the code
     * that gets executed should this address receive a message call;
     * it is immutable and thus, unlike all other fields, cannot be changed
     * after construction. All such code fragments are contained in
     * the state database under their corresponding hashes for later
     * retrieval */
    private final byte[] codeHash;

    /**
     * Instantiates a new Account state.
     *
     * @param nonce   the nonce
     * @param balance the balance
     */
    public AccountState(BigInteger nonce, BigInteger balance) {
        this(nonce, balance, HashUtil.EMPTY_TRIE_HASH, HashUtil.EMPTY_DATA_HASH);
    }

    /**
     * Instantiates a new Account state.
     *
     * @param nonce     the nonce
     * @param balance   the balance
     * @param stateRoot the state root
     * @param codeHash  the code hash
     */
    public AccountState(BigInteger nonce, BigInteger balance, byte[] stateRoot, byte[] codeHash) {
        this.nonce = nonce;
        this.balance = balance;
        this.stateRoot = stateRoot == HashUtil.EMPTY_TRIE_HASH || FastByteComparisons.equal(stateRoot, HashUtil.EMPTY_TRIE_HASH) ? HashUtil.EMPTY_TRIE_HASH : stateRoot;
        this.codeHash = codeHash == HashUtil.EMPTY_DATA_HASH || FastByteComparisons.equal(codeHash, HashUtil.EMPTY_DATA_HASH) ? HashUtil.EMPTY_DATA_HASH : codeHash;
    }

    /**
     * Instantiates a new Account state.
     *
     * @param rlpData the rlp data
     */
    public AccountState(byte[] rlpData) {
        this.rlpEncoded = rlpData;

        RLPList items = (RLPList) RLP.decode2(rlpEncoded).get(0);
        this.nonce = ByteUtil.bytesToBigInteger(items.get(0).getRLPData());
        this.balance = ByteUtil.bytesToBigInteger(items.get(1).getRLPData());
        this.stateRoot = items.get(2).getRLPData();
        this.codeHash = items.get(3).getRLPData();
    }

    /**
     * Gets nonce.
     *
     * @return the nonce
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * With nonce account state.
     *
     * @param nonce the nonce
     * @return the account state
     */
    public AccountState withNonce(BigInteger nonce) {
        return new AccountState(nonce, balance, stateRoot, codeHash);
    }

    /**
     * Get state root byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getStateRoot() {
        return stateRoot;
    }

    /**
     * With state root account state.
     *
     * @param stateRoot the state root
     * @return the account state
     */
    public AccountState withStateRoot(byte[] stateRoot) {
        return new AccountState(nonce, balance, stateRoot, codeHash);
    }

    /**
     * With incremented nonce account state.
     *
     * @return the account state
     */
    public AccountState withIncrementedNonce() {
        return new AccountState(nonce.add(BigInteger.ONE), balance, stateRoot, codeHash);
    }

    /**
     * Get code hash byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCodeHash() {
        return codeHash;
    }

    /**
     * With code hash account state.
     *
     * @param codeHash the code hash
     * @return the account state
     */
    public AccountState withCodeHash(byte[] codeHash) {
        return new AccountState(nonce, balance, stateRoot, codeHash);
    }

    /**
     * Gets balance.
     *
     * @return the balance
     */
    public BigInteger getBalance() {
        return balance;
    }

    /**
     * With balance increment account state.
     *
     * @param value the value
     * @return the account state
     */
    public AccountState withBalanceIncrement(BigInteger value) {
        return new AccountState(nonce, balance.add(value), stateRoot, codeHash);
    }

    /**
     * Get encoded byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getEncoded() {
        if (rlpEncoded == null) {
            byte[] nonce = RLP.encodeBigInteger(this.nonce);
            byte[] balance = RLP.encodeBigInteger(this.balance);
            byte[] stateRoot = RLP.encodeElement(this.stateRoot);
            byte[] codeHash = RLP.encodeElement(this.codeHash);
            this.rlpEncoded = RLP.encodeList(nonce, balance, stateRoot, codeHash);
        }
        return rlpEncoded;
    }
//TODO
//    public boolean isContractExist(BlockChainConfig blockchainConfig) {
//        return !FastByteComparisons.equal(codeHash, EMPTY_DATA_HASH) ||
//                !blockchainConfig.getConstants().getInitialNonce().equals(nonce);
//    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return FastByteComparisons.equal(codeHash, HashUtil.EMPTY_DATA_HASH) &&
                BigInteger.ZERO.equals(balance) &&
                BigInteger.ZERO.equals(nonce);
    }


    @Override
    public String toString() {
        String ret = "  Nonce: " + this.getNonce().toString() + "\n" +
                "  Balance: " + getBalance() + "\n" +
                "  State Root: " + toHexString(this.getStateRoot()) + "\n" +
                "  Code Hash: " + toHexString(this.getCodeHash());
        return ret;
    }
}
