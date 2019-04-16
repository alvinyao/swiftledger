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
package com.higgschain.trust.evmcontract.db.index;

import java.util.Collection;

/**
 * The interface Index.
 *
 * @author Mikhail Kalinin
 * @since 28.01.2016
 */
public interface Index extends Iterable<Long> {

    /**
     * Add all.
     *
     * @param nums the nums
     */
    void addAll(Collection<Long> nums);

    /**
     * Add.
     *
     * @param num the num
     */
    void add(Long num);

    /**
     * Peek long.
     *
     * @return the long
     */
    Long peek();

    /**
     * Poll long.
     *
     * @return the long
     */
    Long poll();

    /**
     * Contains boolean.
     *
     * @param num the num
     * @return the boolean
     */
    boolean contains(Long num);

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Size int.
     *
     * @return the int
     */
    int size();

    /**
     * Clear.
     */
    void clear();

    /**
     * Remove all.
     *
     * @param indexes the indexes
     */
    void removeAll(Collection<Long> indexes);

    /**
     * Peek last long.
     *
     * @return the long
     */
    Long peekLast();

    /**
     * Remove.
     *
     * @param num the num
     */
    void remove(Long num);
}
