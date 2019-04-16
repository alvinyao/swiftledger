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

/**
 * The type Rlp item.
 *
 * @author Roman Mandeleil
 * @since 21.04.14
 */
public class RLPItem implements RLPElement {

    private final byte[] rlpData;

    /**
     * Instantiates a new Rlp item.
     *
     * @param rlpData the rlp data
     */
    public RLPItem(byte[] rlpData) {
        this.rlpData = rlpData;
    }

    @Override
    public byte[] getRLPData() {
        if (rlpData.length == 0) {
            return null;
        }
        return rlpData;
    }
}
