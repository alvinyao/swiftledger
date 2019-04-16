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

import com.higgschain.trust.evmcontract.core.Bloom;
import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.util.RLP;
import com.higgschain.trust.evmcontract.util.RLPElement;
import com.higgschain.trust.evmcontract.util.RLPItem;
import com.higgschain.trust.evmcontract.util.RLPList;
import com.higgschain.trust.evmcontract.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Log info.
 *
 * @author tangkun
 * @date 2018 -12-05
 */
public class LogInfo {

    /**
     * The Address.
     */
    byte[] address = new byte[]{};
    /**
     * The Topics.
     */
    List<DataWord> topics = new ArrayList<>();
    /**
     * The Data.
     */
    byte[] data = new byte[]{};

    /**
     * Instantiates a new Log info.
     *
     * @param rlp the rlp
     */
    public LogInfo(byte[] rlp) {

        RLPList params = RLP.decode2(rlp);
        RLPList logInfo = (RLPList) params.get(0);

        RLPItem address = (RLPItem) logInfo.get(0);
        RLPList topics = (RLPList) logInfo.get(1);
        RLPItem data = (RLPItem) logInfo.get(2);

        this.address = address.getRLPData() != null ? address.getRLPData() : new byte[]{};
        this.data = data.getRLPData() != null ? data.getRLPData() : new byte[]{};

        for (RLPElement topic1 : topics) {
            byte[] topic = topic1.getRLPData();
            this.topics.add(new DataWord(topic));
        }
    }

    /**
     * Instantiates a new Log info.
     *
     * @param address the address
     * @param topics  the topics
     * @param data    the data
     */
    public LogInfo(byte[] address, List<DataWord> topics, byte[] data) {
        this.address = (address != null) ? address : new byte[]{};
        this.topics = (topics != null) ? topics : new ArrayList<DataWord>();
        this.data = (data != null) ? data : new byte[]{};
    }

    /**
     * Get address byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAddress() {
        return address;
    }

    /**
     * Gets topics.
     *
     * @return the topics
     */
    public List<DataWord> getTopics() {
        return topics;
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
     * [address, [topic, topic ...] data]
     *
     * @return the byte [ ]
     */
    public byte[] getEncoded() {

        byte[] addressEncoded = RLP.encodeElement(this.address);

        byte[][] topicsEncoded = null;
        if (topics != null) {
            topicsEncoded = new byte[topics.size()][];
            int i = 0;
            for (DataWord topic : topics) {
                byte[] topicData = topic.getData();
                topicsEncoded[i] = RLP.encodeElement(topicData);
                ++i;
            }
        }

        byte[] dataEncoded = RLP.encodeElement(data);
        return RLP.encodeList(addressEncoded, RLP.encodeList(topicsEncoded), dataEncoded);
    }

    /**
     * Gets bloom.
     *
     * @return the bloom
     */
    public Bloom getBloom() {
        Bloom ret = Bloom.create(HashUtil.sha3(address));
        for (DataWord topic : topics) {
            byte[] topicData = topic.getData();
            ret.or(Bloom.create(HashUtil.sha3(topicData)));
        }
        return ret;
    }

    @Override
    public String toString() {

        StringBuilder topicsStr = new StringBuilder();
        topicsStr.append("[");

        for (DataWord topic : topics) {
            String topicStr = ByteUtil.toHexString(topic.getData());
            topicsStr.append(topicStr).append(" ");
        }
        topicsStr.append("]");


        return "LogInfo{" +
                "address=" + ByteUtil.toHexString(address) +
                ", topics=" + topicsStr +
                ", data=" + ByteUtil.toHexString(data) +
                '}';
    }
}
