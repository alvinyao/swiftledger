package com.higgschain.trust.zkproof;

import java.io.*;

/**
 * The type Serializer util.
 */
class SerializerUtil {

    /**
     * Serialize string.
     *
     * @param o the o
     * @return the string
     * @throws Exception the exception
     */
    static String serialize(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        byte[] buf = baos.toByteArray();
        oos.flush();
        return Base58.encode(buf);
    }

    /**
     * Deserialize object.
     *
     * @param bytes the bytes
     * @return the object
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    static Object deserialize(String bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(Base58.decode(bytes));
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }


}

