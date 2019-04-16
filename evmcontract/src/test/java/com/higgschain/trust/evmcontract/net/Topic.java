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
package com.higgschain.trust.evmcontract.net;


import com.higgschain.trust.evmcontract.util.RLP;

import java.util.Arrays;

import static com.higgschain.trust.evmcontract.crypto.HashUtil.sha3;
import static com.higgschain.trust.evmcontract.util.ByteUtil.toHexString;

/**
 * The type Topic.
 *
 * @author by Konstantin Shabalin
 */
public class Topic {
    private String originalTopic;
    private byte[] fullTopic;
    private byte[] abrigedTopic = new byte[4];

    /**
     * Instantiates a new Topic.
     *
     * @param data the data
     */
    public Topic(byte[] data) {
        this.abrigedTopic = data;
    }

    /**
     * Instantiates a new Topic.
     *
     * @param data the data
     */
    public Topic(String data) {
        originalTopic = data;
        fullTopic = sha3(RLP.encode(originalTopic));
        this.abrigedTopic = buildAbrigedTopic(fullTopic);
    }

    /**
     * Create topics topic [ ].
     *
     * @param topicsString the topics string
     * @return the topic [ ]
     */
    public static Topic[] createTopics(String... topicsString) {
        if (topicsString == null) return new Topic[0];
        Topic[] topics = new Topic[topicsString.length];
        for (int i = 0; i < topicsString.length; i++) {
            topics[i] = new Topic(topicsString[i]);
        }
        return topics;
    }

    /**
     * Get bytes byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBytes() {
        return abrigedTopic;
    }

    private byte[] buildAbrigedTopic(byte[] data) {
        byte[] hash = sha3(data);
        byte[] topic = new byte[4];
        System.arraycopy(hash, 0, topic, 0, 4);
        return topic;
    }

    /**
     * Get abriged topic byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAbrigedTopic() {
        return abrigedTopic;
    }

    /**
     * Get full topic byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getFullTopic() {
        return fullTopic;
    }

    /**
     * Gets original topic.
     *
     * @return the original topic
     */
    public String getOriginalTopic() {
        return originalTopic;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Topic)) return false;
        return Arrays.equals(this.abrigedTopic, ((Topic) obj).getBytes());
    }

    @Override
    public String toString() {
        return "#" + toHexString(abrigedTopic) + (originalTopic == null ? "" : "(" + originalTopic + ")");
    }
}
