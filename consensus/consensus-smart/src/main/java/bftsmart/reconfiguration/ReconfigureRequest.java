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
package bftsmart.reconfiguration;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * The type Reconfigure request.
 *
 * @author eduardo
 */
public class ReconfigureRequest implements Externalizable {

    private int sender;
    private Hashtable<Integer, String> properties = new Hashtable<Integer, String>();
    private byte[] signature;
    private byte[] otherSignature;
    private int number;
    private String nodeName;

    /**
     * Instantiates a new Reconfigure request.
     */
    public ReconfigureRequest() {
    }

    /**
     * Gets node name.
     *
     * @return the node name
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets node name.
     *
     * @param nodeName the node name
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Get other signature byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getOtherSignature() {
        return otherSignature;
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Sets other signature.
     *
     * @param otherSignature the other signature
     */
    public void setOtherSignature(byte[] otherSignature) {
        this.otherSignature = otherSignature;
    }

    /**
     * Instantiates a new Reconfigure request.
     *
     * @param sender the sender
     */
    public ReconfigureRequest(int sender) {
        this.sender = sender;
    }

    /**
     * Sets signature.
     *
     * @param signature the signature
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Get signature byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Hashtable<Integer, String> getProperties() {
        return properties;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public int getSender() {
        return sender;
    }

    /**
     * Sets property.
     *
     * @param prop  the prop
     * @param value the value
     */
    public void setProperty(int prop, String value) {
        this.properties.put(prop, value);
    }

    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(sender);

        int num = properties.keySet().size();

        out.writeInt(num);

        Iterator<Integer> it = properties.keySet().iterator();

        while (it.hasNext()) {
            int key = it.next();
            String value = properties.get(key);

            out.writeInt(key);
            out.writeUTF(value);
        }

        out.writeInt(signature.length);
        out.write(signature);
        //TODO modify
        out.writeInt(otherSignature.length);
        out.write(otherSignature);
        out.writeInt(number);
        out.writeUTF(nodeName);
    }

    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        sender = in.readInt();

        int num = in.readInt();

        for (int i = 0; i < num; i++) {
            int key = in.readInt();
            String value = in.readUTF();
            properties.put(key, value);
        }

        this.signature = new byte[in.readInt()];
        in.read(this.signature);
        //TODO modify
        this.otherSignature = new byte[in.readInt()];
        in.read(this.otherSignature);
        this.number = in.readInt();
        this.nodeName = in.readUTF();
    }

    @Override public String toString() {
        String ret = "Sender :" + sender + ";";
        Iterator<Integer> it = properties.keySet().iterator();
        while (it.hasNext()) {
            int key = it.next();
            String value = properties.get(key);
            ret = ret + key + value;
        }
        return ret;
    }

}
