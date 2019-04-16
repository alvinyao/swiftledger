package bftsmart.reconfiguration;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * The type Rc message.
 *
 * @author: zhouyafeng
 * @create: 2018 /05/30 14:52
 * @description:
 */
public class RCMessage implements Serializable {
    private int sender;
    private String operation;
    private int num;
    private String ip;
    private int port;
    private Hashtable<Integer, String> properties = new Hashtable<Integer, String>();
    private byte[] signature;
    private String nodeName;

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
     * Gets sender.
     *
     * @return the sender
     */
    public int getSender() {
        return sender;
    }

    /**
     * Sets sender.
     *
     * @param sender the sender
     */
    public void setSender(int sender) {
        this.sender = sender;
    }

    /**
     * Gets operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets operation.
     *
     * @param operation the operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Gets num.
     *
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * Sets num.
     *
     * @param num the num
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Gets ip.
     *
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets ip.
     *
     * @param ip the ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
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
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(Hashtable<Integer, String> properties) {
        this.properties = properties;
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
     * Sets signature.
     *
     * @param signature the signature
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
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