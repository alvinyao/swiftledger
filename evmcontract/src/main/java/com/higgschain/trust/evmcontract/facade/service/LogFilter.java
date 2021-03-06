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
package com.higgschain.trust.evmcontract.facade.service;

import com.higgschain.trust.evmcontract.core.Bloom;
import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.LogInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton Nashatyrev on 12.04.2016.
 */
public class LogFilter {
    //  [[addr1, addr2], null, [A, B], [C]]
    private List<byte[][]> topics = new ArrayList<>();
    private byte[][] contractAddresses = new byte[0][];
    private Bloom[][] filterBlooms;

    /**
     * With contract address log filter.
     *
     * @param orAddress the or address
     * @return the log filter
     */
    public LogFilter withContractAddress(byte[]... orAddress) {
        contractAddresses = orAddress;
        return this;
    }

    /**
     * With topic log filter.
     *
     * @param orTopic the or topic
     * @return the log filter
     */
    public LogFilter withTopic(byte[]... orTopic) {
        topics.add(orTopic);
        return this;
    }

    private void initBlooms() {
        if (filterBlooms != null) {
            return;
        }

        List<byte[][]> addrAndTopics = new ArrayList<>(topics);
        addrAndTopics.add(contractAddresses);

        filterBlooms = new Bloom[addrAndTopics.size()][];
        for (int i = 0; i < addrAndTopics.size(); i++) {
            byte[][] orTopics = addrAndTopics.get(i);
            if (orTopics == null || orTopics.length == 0) {
                // always matches
                filterBlooms[i] = new Bloom[]{new Bloom()};
            } else {
                filterBlooms[i] = new Bloom[orTopics.length];
                for (int j = 0; j < orTopics.length; j++) {
                    filterBlooms[i][j] = Bloom.create(HashUtil.sha3(orTopics[j]));
                }
            }
        }
    }

    /**
     * Match bloom boolean.
     *
     * @param blockBloom the block bloom
     * @return the boolean
     */
    public boolean matchBloom(Bloom blockBloom) {
        initBlooms();
        for (Bloom[] andBloom : filterBlooms) {
            boolean orMatches = false;
            for (Bloom orBloom : andBloom) {
                if (blockBloom.matches(orBloom)) {
                    orMatches = true;
                    break;
                }
            }
            if (!orMatches) {
                return false;
            }
        }
        return true;
    }

    /**
     * Matches contract address boolean.
     *
     * @param toAddr the to addr
     * @return the boolean
     */
    public boolean matchesContractAddress(byte[] toAddr) {
        initBlooms();
        for (byte[] address : contractAddresses) {
            if (Arrays.equals(address, toAddr)) {
                return true;
            }
        }
        return contractAddresses.length == 0;
    }

    /**
     * Matches exactly boolean.
     *
     * @param logInfo the log info
     * @return the boolean
     */
    public boolean matchesExactly(LogInfo logInfo) {
        initBlooms();
        if (!matchesContractAddress(logInfo.getAddress())) {
            return false;
        }
        List<DataWord> logTopics = logInfo.getTopics();
        for (int i = 0; i < this.topics.size(); i++) {
            if (i >= logTopics.size()) {
                return false;
            }
            byte[][] orTopics = topics.get(i);
            if (orTopics != null && orTopics.length > 0) {
                boolean orMatches = false;
                DataWord logTopic = logTopics.get(i);
                for (byte[] orTopic : orTopics) {
                    if (new DataWord(orTopic).equals(logTopic)) {
                        orMatches = true;
                        break;
                    }
                }
                if (!orMatches) {
                    return false;
                }
            }
        }
        return true;
    }
}
