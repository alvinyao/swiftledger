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
package com.higgschain.trust.evmcontract.util;

import com.higgschain.trust.evmcontract.crypto.HashUtil;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.higgschain.trust.evmcontract.util.ByteUtil.toHexString;

/**
 * Class to encapsulate an object and provide utilities for conversion
 */
public class Value {

    private Object value;
    private byte[] rlp;
    private byte[] sha3;

    private boolean decoded = false;

    /**
     * From rlp encoded value.
     *
     * @param data the data
     * @return the value
     */
    public static Value fromRlpEncoded(byte[] data) {

        if (data != null && data.length != 0) {
            Value v = new Value();
            v.init(data);
            return v;
        }
        return null;
    }

    /**
     * Instantiates a new Value.
     */
    public Value() {
    }

    /**
     * Init.
     *
     * @param rlp the rlp
     */
    public void init(byte[] rlp) {
        this.rlp = rlp;
    }

    /**
     * Instantiates a new Value.
     *
     * @param obj the obj
     */
    public Value(Object obj) {

        this.decoded = true;
        if (obj == null) {
            return;
        }

        if (obj instanceof Value) {
            this.value = ((Value) obj).asObj();
        } else {
            this.value = obj;
        }
    }

    /**
     * With hash value.
     *
     * @param hash the hash
     * @return the value
     */
    public Value withHash(byte[] hash) {
        sha3 = hash;
        return this;
    }

    /* *****************
     *      Convert
     * *****************/

    /**
     * As obj object.
     *
     * @return the object
     */
    public Object asObj() {
        decode();
        return value;
    }

    /**
     * As list list.
     *
     * @return the list
     */
    public List<Object> asList() {
        decode();
        Object[] valueArray = (Object[]) value;
        return Arrays.asList(valueArray);
    }

    /**
     * As int int.
     *
     * @return the int
     */
    public int asInt() {
        decode();
        if (isInt()) {
            return (Integer) value;
        } else if (isBytes()) {
            return new BigInteger(1, asBytes()).intValue();
        }
        return 0;
    }

    /**
     * As long long.
     *
     * @return the long
     */
    public long asLong() {
        decode();
        if (isLong()) {
            return (Long) value;
        } else if (isBytes()) {
            return new BigInteger(1, asBytes()).longValue();
        }
        return 0;
    }

    /**
     * As big int big integer.
     *
     * @return the big integer
     */
    public BigInteger asBigInt() {
        decode();
        return (BigInteger) value;
    }

    /**
     * As string string.
     *
     * @return the string
     */
    public String asString() {
        decode();
        if (isBytes()) {
            return new String((byte[]) value);
        } else if (isString()) {
            return (String) value;
        }
        return "";
    }

    /**
     * As bytes byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] asBytes() {
        decode();
        if (isBytes()) {
            return (byte[]) value;
        } else if (isString()) {
            return asString().getBytes();
        }
        return ByteUtil.EMPTY_BYTE_ARRAY;
    }

    /**
     * Gets hex.
     *
     * @return the hex
     */
    public String getHex() {
        return Hex.toHexString(this.encode());
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return this.encode();
    }

    /**
     * As slice int [ ].
     *
     * @return the int [ ]
     */
    public int[] asSlice() {
        return (int[]) value;
    }

    /**
     * Get value.
     *
     * @param index the index
     * @return the value
     */
    public Value get(int index) {
        if (isList()) {
            // Guard for OutOfBounds
            if (asList().size() <= index) {
                return new Value(null);
            }
            if (index < 0) {
                throw new RuntimeException("Negative index not allowed");
            }
            return new Value(asList().get(index));
        }
        // If this wasn't a slice you probably shouldn't be using this function
        return new Value(null);
    }

    /* *****************
     *      Utility
     * *****************/

    /**
     * Decode.
     */
    public void decode() {
        if (!this.decoded) {
            this.value = RLP.decode(rlp, 0).getDecoded();
            this.decoded = true;
        }
    }

    /**
     * Encode byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] encode() {
        if (rlp == null) {
            rlp = RLP.encode(value);
        }
        return rlp;
    }

    /**
     * Hash byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] hash() {
        if (sha3 == null) {
            sha3 = HashUtil.sha3(encode());
        }
        return sha3;
    }

    /**
     * Is list boolean.
     *
     * @return the boolean
     */
    public boolean isList() {
        decode();
        return value != null && value.getClass().isArray() && !value.getClass().getComponentType().isPrimitive();
    }

    /**
     * Is string boolean.
     *
     * @return the boolean
     */
    public boolean isString() {
        decode();
        return value instanceof String;
    }

    /**
     * Is int boolean.
     *
     * @return the boolean
     */
    public boolean isInt() {
        decode();
        return value instanceof Integer;
    }

    /**
     * Is long boolean.
     *
     * @return the boolean
     */
    public boolean isLong() {
        decode();
        return value instanceof Long;
    }

    /**
     * Is big int boolean.
     *
     * @return the boolean
     */
    public boolean isBigInt() {
        decode();
        return value instanceof BigInteger;
    }

    /**
     * Is bytes boolean.
     *
     * @return the boolean
     */
    public boolean isBytes() {
        decode();
        return value instanceof byte[];
    }

    /**
     * Is readable string boolean.
     *
     * @return the boolean
     */
    // it's only if the isBytes() = true;
    public boolean isReadableString() {

        decode();
        int readableChars = 0;
        byte[] data = (byte[]) value;

        if (data.length == 1 && data[0] > 31 && data[0] < 126) {
            return true;
        }

        for (byte aData : data) {
            if (aData > 32 && aData < 126) {
                ++readableChars;
            }
        }

        return (double) readableChars / (double) data.length > 0.55;
    }

    /**
     * Is hex string boolean.
     *
     * @return the boolean
     */
    // it's only if the isBytes() = true;
    public boolean isHexString() {

        decode();
        int hexChars = 0;
        byte[] data = (byte[]) value;

        for (byte aData : data) {

            if ((aData >= 48 && aData <= 57)
                    || (aData >= 97 && aData <= 102)) {
                ++hexChars;
            }
        }

        return (double) hexChars / (double) data.length > 0.9;
    }

    /**
     * Is hash code boolean.
     *
     * @return the boolean
     */
    public boolean isHashCode() {
        decode();
        return this.asBytes().length == 32;
    }

    /**
     * Is null boolean.
     *
     * @return the boolean
     */
    public boolean isNull() {
        decode();
        return value == null;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        decode();
        if (isNull()) {
            return true;
        }
        if (isBytes() && asBytes().length == 0) {
            return true;
        }
        if (isList() && asList().isEmpty()) {
            return true;
        }
        return isString() && asString().isEmpty();

    }

    /**
     * Length int.
     *
     * @return the int
     */
    public int length() {
        decode();
        if (isList()) {
            return asList().size();
        } else if (isBytes()) {
            return asBytes().length;
        } else if (isString()) {
            return asString().length();
        }
        return 0;
    }

    @Override
    public String toString() {

        decode();
        StringBuilder stringBuilder = new StringBuilder();

        if (isList()) {

            Object[] list = (Object[]) value;

            // special case - key/value node
            if (list.length == 2) {

                stringBuilder.append("[ ");

                Value key = new Value(list[0]);

                byte[] keyNibbles = CompactEncoder.binToNibblesNoTerminator(key.asBytes());
                String keyString = ByteUtil.nibblesToPrettyString(keyNibbles);
                stringBuilder.append(keyString);

                stringBuilder.append(",");

                Value val = new Value(list[1]);
                stringBuilder.append(val.toString());

                stringBuilder.append(" ]");
                return stringBuilder.toString();
            }
            stringBuilder.append(" [");

            for (int i = 0; i < list.length; ++i) {
                Value val = new Value(list[i]);
                if (val.isString() || val.isEmpty()) {
                    stringBuilder.append("'").append(val.toString()).append("'");
                } else {
                    stringBuilder.append(val.toString());
                }
                if (i < list.length - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("] ");

            return stringBuilder.toString();
        } else if (isEmpty()) {
            return "";
        } else if (isBytes()) {

            StringBuilder output = new StringBuilder();
            if (isHashCode()) {
                output.append(toHexString(asBytes()));
            } else if (isReadableString()) {
                output.append("'");
                for (byte oneByte : asBytes()) {
                    if (oneByte < 16) {
                        output.append("\\x").append(ByteUtil.oneByteToHexString(oneByte));
                    } else {
                        output.append(Character.valueOf((char) oneByte));
                    }
                }
                output.append("'");
                return output.toString();
            }
            return toHexString(this.asBytes());
        } else if (isString()) {
            return asString();
        }
        return "Unexpected type";
    }

    /**
     * Count branch nodes int.
     *
     * @return the int
     */
    public int countBranchNodes() {
        decode();
        if (this.isList()) {
            List<Object> objList = this.asList();
            int i = 0;
            for (Object obj : objList) {
                i += (new Value(obj)).countBranchNodes();
            }
            return i;
        } else if (this.isBytes()) {
            this.asBytes();
        }
        return 0;
    }
}
