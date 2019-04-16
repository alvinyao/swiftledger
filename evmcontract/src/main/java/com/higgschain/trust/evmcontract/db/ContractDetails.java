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
package com.higgschain.trust.evmcontract.db;

import com.higgschain.trust.evmcontract.vm.DataWord;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The interface Contract details.
 *
 * @author tank
 */
public interface ContractDetails {

    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    void put(DataWord key, DataWord value);

    /**
     * Get data word.
     *
     * @param key the key
     * @return the data word
     */
    DataWord get(DataWord key);

    /**
     * Get code byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getCode();

    /**
     * Get code byte [ ].
     *
     * @param codeHash the code hash
     * @return the byte [ ]
     */
    byte[] getCode(byte[] codeHash);

    /**
     * Sets code.
     *
     * @param code the code
     */
    void setCode(byte[] code);

    /**
     * Get storage hash byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getStorageHash();

    /**
     * Decode.
     *
     * @param rlpCode the rlp code
     */
    void decode(byte[] rlpCode);

    /**
     * Sets dirty.
     *
     * @param dirty the dirty
     */
    void setDirty(boolean dirty);

    /**
     * Sets deleted.
     *
     * @param deleted the deleted
     */
    void setDeleted(boolean deleted);

    /**
     * Is dirty boolean.
     *
     * @return the boolean
     */
    boolean isDirty();

    /**
     * Is deleted boolean.
     *
     * @return the boolean
     */
    boolean isDeleted();

    /**
     * Get encoded byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getEncoded();

    /**
     * Gets storage size.
     *
     * @return the storage size
     */
    int getStorageSize();

    /**
     * Gets storage keys.
     *
     * @return the storage keys
     */
    Set<DataWord> getStorageKeys();

    /**
     * Gets storage.
     *
     * @param keys the keys
     * @return the storage
     */
    Map<DataWord, DataWord> getStorage(Collection<DataWord> keys);

    /**
     * Gets storage.
     *
     * @return the storage
     */
    Map<DataWord, DataWord> getStorage();

    /**
     * Sets storage.
     *
     * @param storageKeys   the storage keys
     * @param storageValues the storage values
     */
    void setStorage(List<DataWord> storageKeys, List<DataWord> storageValues);

    /**
     * Sets storage.
     *
     * @param storage the storage
     */
    void setStorage(Map<DataWord, DataWord> storage);

    /**
     * Get address byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getAddress();

    /**
     * Sets address.
     *
     * @param address the address
     */
    void setAddress(byte[] address);

    ContractDetails clone();

    @Override
    String toString();

    /**
     * Sync storage.
     */
    void syncStorage();

    /**
     * Gets snapshot to.
     *
     * @param hash the hash
     * @return the snapshot to
     */
    ContractDetails getSnapshotTo(byte[] hash);
}
