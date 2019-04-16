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
package com.higgschain.trust.evmcontract.datasource;

/**
 * Converter from one type to another and vice versa
 * <p>
 * Created by Anton Nashatyrev on 17.03.2016.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
public interface Serializer<T, S> {
    /**
     * Converts T ==> S
     * Should correctly handle null parameter
     *
     * @param object the object
     * @return the s
     */
    S serialize(T object);

    /**
     * Converts S ==> T
     * Should correctly handle null parameter
     *
     * @param stream the stream
     * @return the t
     */
    T deserialize(S stream);
}
