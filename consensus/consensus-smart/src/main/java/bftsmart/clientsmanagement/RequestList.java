/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and the authors indicated in the @author tags
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bftsmart.clientsmanagement;

import bftsmart.tom.core.messages.TOMMessage;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Extended LinkedList used to store pending requests issued by a client.
 *
 * @author alysson
 */
public class RequestList extends LinkedList<TOMMessage> {

    private static final long serialVersionUID = -3639222602426147629L;

    private int maxSize = Integer.MAX_VALUE;

    /**
     * Instantiates a new Request list.
     */
    public RequestList() {
    }

    /**
     * Instantiates a new Request list.
     *
     * @param maxSize the max size
     */
    public RequestList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override public void addLast(TOMMessage msg) {
        super.addLast(msg);
        if (size() > maxSize) {
            super.removeFirst();
        }
    }

    /**
     * Remove tom message.
     *
     * @param serializedMessage the serialized message
     * @return the tom message
     */
    public TOMMessage remove(byte[] serializedMessage) {
        for (ListIterator<TOMMessage> li = listIterator(); li.hasNext(); ) {
            TOMMessage msg = li.next();
            if (Arrays.equals(serializedMessage, msg.serializedMessage)) {
                li.remove();
                return msg;
            }
        }
        return null;
    }

    /**
     * Remove by id tom message.
     *
     * @param id the id
     * @return the tom message
     */
    public TOMMessage removeById(int id) {
        for (ListIterator<TOMMessage> li = listIterator(); li.hasNext(); ) {
            TOMMessage msg = li.next();
            if (msg.getId() == id) {
                li.remove();
                return msg;
            }
        }
        return null;
    }

    /**
     * Get ids int [ ].
     *
     * @return the int [ ]
     */
    // I think this method can be removed in future versions of JBP
    public int[] getIds() {
        int ids[] = new int[size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = get(i).getId();
        }

        return ids;
    }

    /**
     * Get tom message.
     *
     * @param serializedMessage the serialized message
     * @return the tom message
     */
    public TOMMessage get(byte[] serializedMessage) {
        for (ListIterator<TOMMessage> li = listIterator(); li.hasNext(); ) {
            TOMMessage msg = li.next();
            if (Arrays.equals(serializedMessage, msg.serializedMessage)) {
                return msg;
            }
        }
        return null;
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    public TOMMessage getById(int id) {
        for (ListIterator<TOMMessage> li = listIterator(); li.hasNext(); ) {
            TOMMessage msg = li.next();
            if (msg.getId() == id) {
                return msg;
            }
        }
        return null;
    }

    /**
     * Gets by sequence.
     *
     * @param sequence the sequence
     * @return the by sequence
     */
    public TOMMessage getBySequence(int sequence) {
        for (ListIterator<TOMMessage> li = listIterator(); li.hasNext(); ) {
            TOMMessage msg = li.next();
            if (msg.getSequence() == sequence) {
                return msg;
            }
        }
        return null;
    }

    /**
     * Contains boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean contains(int id) {
        for (ListIterator<TOMMessage> li = listIterator(); li.hasNext(); ) {
            TOMMessage msg = li.next();
            if (msg.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
