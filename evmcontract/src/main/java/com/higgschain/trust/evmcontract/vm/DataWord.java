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
package com.higgschain.trust.evmcontract.vm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.higgschain.trust.evmcontract.db.ByteArrayWrapper;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.util.FastByteComparisons;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * DataWord is the 32-byte array representation of a 256-bit number
 * Calculations can be done on this word with other DataWords
 *
 * @author Roman Mandeleil
 * @since 01.06.2014
 */
public class DataWord implements Comparable<DataWord> {

    /**
     * Maximum value of the DataWord
     */
    public static final BigInteger _2_256 = BigInteger.valueOf(2).pow(256);

    /**
     * The constant MAX_VALUE.
     */
    public static final BigInteger MAX_VALUE = _2_256.subtract(BigInteger.ONE);

    /**
     * don't push it in to the stack
     */
    public static final DataWord ZERO = new DataWord(new byte[32]);

    /**
     * don't push it in to the stack
     */
    public static final DataWord ZERO_EMPTY_ARRAY = new DataWord(new byte[0]);

    /**
     * The constant MEM_SIZE.
     */
    public static final long MEM_SIZE = 32 + 16 + 16;
    /**
     * The constant ONE.
     */
    public static final DataWord ONE = DataWord.of((byte) 1);
    private byte[] data = new byte[32];

    /**
     * Instantiates a new Data word.
     */
    public DataWord() {
    }

    /**
     * Instantiates a new Data word.
     *
     * @param num the num
     */
    public DataWord(int num) {
        this(ByteBuffer.allocate(4).putInt(num));
    }

    /**
     * Instantiates a new Data word.
     *
     * @param num the num
     */
    public DataWord(long num) {
        this(ByteBuffer.allocate(8).putLong(num));
    }

    private DataWord(ByteBuffer buffer) {
        final ByteBuffer data = ByteBuffer.allocate(32);
        final byte[] array = buffer.array();
        System.arraycopy(array, 0, data.array(), 32 - array.length, array.length);
        this.data = data.array();
    }

    /**
     * Instantiates a new Data word.
     *
     * @param data the data
     */
    @JsonCreator
    public DataWord(String data) {
        this(Hex.decode(data));
    }

    /**
     * Instantiates a new Data word.
     *
     * @param wrappedData the wrapped data
     */
    public DataWord(ByteArrayWrapper wrappedData) {
        this(wrappedData.getData());
    }

    /**
     * Instantiates a new Data word.
     *
     * @param data the data
     */
    public DataWord(byte[] data) {
        if (data == null) {
            this.data = ByteUtil.EMPTY_BYTE_ARRAY;
        } else if (data.length == 32) {
            this.data = data;
        } else if (data.length <= 32) {
            System.arraycopy(data, 0, this.data, 32 - data.length, data.length);
        } else {
            throw new RuntimeException("Data word can't exceed 32 bytes: " + data);
        }
    }

    /**
     * Of data word.
     *
     * @param num the num
     * @return the data word
     */
    public static DataWord of(int num) {
        return of(intToBytes(num));
    }

    /**
     * Int to bytes byte [ ].
     *
     * @param val the val
     * @return the byte [ ]
     */
    public static byte[] intToBytes(int val) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(val).array();
    }

    /**
     * Of data word.
     *
     * @param data the data
     * @return the data word
     */
    public static DataWord of(byte[] data) {
        if (data == null || data.length == 0) {
            return DataWord.ZERO;
        }

        int leadingZeroBits = ByteUtil.numberOfLeadingZeros(data);
        int valueBits = 8 * data.length - leadingZeroBits;
        if (valueBits <= 8) {
            if (data[data.length - 1] == 0) {
                return DataWord.ZERO;
            }
            if (data[data.length - 1] == 1) {
                //hotfix dataWord one is not init
                byte[] bytes = new byte[8 * data.length];
                bytes[bytes.length - 1] = 1;
                return new DataWord(bytes);
            }
        }

        if (data.length == 32) {
            return new DataWord(java.util.Arrays.copyOf(data, data.length));
        } else if (data.length <= 32) {
            byte[] bytes = new byte[32];
            System.arraycopy(data, 0, bytes, 32 - data.length, data.length);
            return new DataWord(bytes);
        } else {
            throw new RuntimeException(String.format("Data word can't exceed 32 bytes: 0x%s", ByteUtil.toHexString(data)));
        }
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Get no lead zeroes data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getNoLeadZeroesData() {
        return ByteUtil.stripLeadingZeroes(data);
    }

    /**
     * Get last 20 bytes byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getLast20Bytes() {
        return Arrays.copyOfRange(data, 12, data.length);
    }

    /**
     * Value big integer.
     *
     * @return the big integer
     */
    public BigInteger value() {
        return new BigInteger(1, data);
    }

    /**
     * Converts this DataWord to an int, checking for lost information.
     * If this DataWord is out of the possible range for an int result
     * then an ArithmeticException is thrown.
     *
     * @return this DataWord converted to an int.
     * @throws ArithmeticException - if this will not fit in an int.
     */
    public int intValue() {
        int intVal = 0;

        for (byte aData : data) {
            intVal = (intVal << 8) + (aData & 0xff);
        }

        return intVal;
    }

    /**
     * In case of int overflow returns Integer.MAX_VALUE
     * otherwise works as #intValue()
     *
     * @return the int
     */
    public int intValueSafe() {
        int bytesOccupied = bytesOccupied();
        int intValue = intValue();
        if (bytesOccupied > 4 || intValue < 0) {
            return Integer.MAX_VALUE;
        }
        return intValue;
    }

    /**
     * Converts this DataWord to a long, checking for lost information.
     * If this DataWord is out of the possible range for a long result
     * then an ArithmeticException is thrown.
     *
     * @return this DataWord converted to a long.
     * @throws ArithmeticException - if this will not fit in a long.
     */
    public long longValue() {

        long longVal = 0;
        for (byte aData : data) {
            longVal = (longVal << 8) + (aData & 0xff);
        }

        return longVal;
    }

    /**
     * In case of long overflow returns Long.MAX_VALUE
     * otherwise works as #longValue()
     *
     * @return the long
     */
    public long longValueSafe() {
        int bytesOccupied = bytesOccupied();
        long longValue = longValue();
        if (bytesOccupied > 8 || longValue < 0) {
            return Long.MAX_VALUE;
        }
        return longValue;
    }

    /**
     * S value big integer.
     *
     * @return the big integer
     */
    public BigInteger sValue() {
        return new BigInteger(data);
    }

    /**
     * Big int value string.
     *
     * @return the string
     */
    public String bigIntValue() {
        return new BigInteger(data).toString();
    }

    /**
     * Is zero boolean.
     *
     * @return the boolean
     */
    public boolean isZero() {
        for (byte tmp : data) {
            if (tmp != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is negative boolean.
     *
     * @return the boolean
     */
    // only in case of signed operation
    // when the number is explicit defined
    // as negative
    public boolean isNegative() {
        int result = data[0] & 0x80;
        return result == 0x80;
    }

    /**
     * And data word.
     *
     * @param w2 the w 2
     * @return the data word
     */
    public DataWord and(DataWord w2) {

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] &= w2.data[i];
        }
        return this;
    }

    /**
     * Or data word.
     *
     * @param w2 the w 2
     * @return the data word
     */
    public DataWord or(DataWord w2) {

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] |= w2.data[i];
        }
        return this;
    }

    /**
     * Xor data word.
     *
     * @param w2 the w 2
     * @return the data word
     */
    public DataWord xor(DataWord w2) {

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] ^= w2.data[i];
        }
        return this;
    }

    /**
     * Negate.
     */
    public void negate() {

        if (this.isZero()) {
            return;
        }

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] = (byte) ~this.data[i];
        }

        for (int i = this.data.length - 1; i >= 0; --i) {
            this.data[i] = (byte) (1 + this.data[i] & 0xFF);
            if (this.data[i] != 0) {
                break;
            }
        }
    }

    /**
     * Bnot.
     */
    public void bnot() {
        if (this.isZero()) {
            this.data = ByteUtil.copyToArray(MAX_VALUE);
            return;
        }
        this.data = ByteUtil.copyToArray(MAX_VALUE.subtract(this.value()));
    }

    /**
     * From : http://stackoverflow.com/a/24023466/459349
     *
     * @param word the word
     */
    public void add(DataWord word) {
        byte[] result = new byte[32];
        for (int i = 31, overflow = 0; i >= 0; i--) {
            int v = (this.data[i] & 0xff) + (word.data[i] & 0xff) + overflow;
            result[i] = (byte) v;
            overflow = v >>> 8;
        }
        this.data = result;
    }

    /**
     * Add 2.
     *
     * @param word the word
     */
    // old add-method with BigInteger quick hack
    public void add2(DataWord word) {
        BigInteger result = value().add(word.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * mul can be done in more efficient way
     * with shift left shift right trick
     * without BigInteger quick hack
     *
     * @param word the word
     */
    public void mul(DataWord word) {
        BigInteger result = value().multiply(word.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * Div.
     *
     * @param word the word
     */
    // TODO: improve with no BigInteger
    public void div(DataWord word) {

        if (word.isZero()) {
            this.and(ZERO);
            return;
        }

        BigInteger result = value().divide(word.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * S div.
     *
     * @param word the word
     */
    // TODO: improve with no BigInteger
    public void sDiv(DataWord word) {

        if (word.isZero()) {
            this.and(ZERO);
            return;
        }

        BigInteger result = sValue().divide(word.sValue());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * Sub.
     *
     * @param word the word
     */
    // TODO: improve with no BigInteger
    public void sub(DataWord word) {
        BigInteger result = value().subtract(word.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * Exp.
     *
     * @param word the word
     */
    // TODO: improve with no BigInteger
    public void exp(DataWord word) {
        BigInteger result = value().modPow(word.value(), _2_256);
        this.data = ByteUtil.copyToArray(result);
    }

    /**
     * Mod.
     *
     * @param word the word
     */
    // TODO: improve with no BigInteger
    public void mod(DataWord word) {

        if (word.isZero()) {
            this.and(ZERO);
            return;
        }

        BigInteger result = value().mod(word.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * S mod.
     *
     * @param word the word
     */
    public void sMod(DataWord word) {

        if (word.isZero()) {
            this.and(ZERO);
            return;
        }

        BigInteger result = sValue().abs().mod(word.sValue().abs());
        result = (sValue().signum() == -1) ? result.negate() : result;

        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * Addmod.
     *
     * @param word1 the word 1
     * @param word2 the word 2
     */
    public void addmod(DataWord word1, DataWord word2) {
        if (word2.isZero()) {
            this.data = new byte[32];
            return;
        }

        BigInteger result = value().add(word1.value()).mod(word2.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    /**
     * Mulmod.
     *
     * @param word1 the word 1
     * @param word2 the word 2
     */
    public void mulmod(DataWord word1, DataWord word2) {

        if (this.isZero() || word1.isZero() || word2.isZero()) {
            this.data = new byte[32];
            return;
        }

        BigInteger result = value().multiply(word1.value()).mod(word2.value());
        this.data = ByteUtil.copyToArray(result.and(MAX_VALUE));
    }

    @JsonValue
    @Override
    public String toString() {
        return ByteUtil.toHexString(data);
    }

    /**
     * To prefix string string.
     *
     * @return the string
     */
    public String toPrefixString() {

        byte[] pref = getNoLeadZeroesData();
        if (pref.length == 0) {
            return "";
        }

        if (pref.length < 7) {
            return Hex.toHexString(pref);
        }

        return Hex.toHexString(pref).substring(0, 6);
    }

    /**
     * Short hex string.
     *
     * @return the string
     */
    public String shortHex() {
        String hexValue = Hex.toHexString(getNoLeadZeroesData()).toUpperCase();
        return "0x" + hexValue.replaceFirst("^0+(?!$)", "");
    }

    @Override
    public DataWord clone() {
        return new DataWord(Arrays.clone(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataWord dataWord = (DataWord) o;

        return java.util.Arrays.equals(data, dataWord.data);

    }

    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(data);
    }

    @Override
    public int compareTo(DataWord o) {
        if (o == null || o.getData() == null) {
            return -1;
        }
        int result = FastByteComparisons.compareTo(
                data, 0, data.length,
                o.getData(), 0, o.getData().length);
        // Convert result into -1, 0 or 1 as is the convention
        return (int) Math.signum(result);
    }

    /**
     * Sign extend.
     *
     * @param k the k
     */
    public void signExtend(byte k) {
        if (0 > k || k > 31) {
            throw new IndexOutOfBoundsException();
        }
        byte mask = this.sValue().testBit((k * 8) + 7) ? (byte) 0xff : 0;
        for (int i = 31; i > k; i--) {
            this.data[31 - i] = mask;
        }
    }

    /**
     * Bytes occupied int.
     *
     * @return the int
     */
    public int bytesOccupied() {
        int firstNonZero = ByteUtil.firstNonZeroByte(data);
        if (firstNonZero == -1) {
            return 0;
        }
        return 31 - firstNonZero + 1;
    }

    /**
     * Is hex boolean.
     *
     * @param hex the hex
     * @return the boolean
     */
    public boolean isHex(String hex) {
        return Hex.toHexString(data).equals(hex);
    }

    /**
     * As string string.
     *
     * @return the string
     */
    public String asString() {
        return new String(getNoLeadZeroesData());
    }
}
