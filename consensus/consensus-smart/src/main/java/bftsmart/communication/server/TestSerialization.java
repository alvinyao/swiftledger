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
package bftsmart.communication.server;

import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;
import bftsmart.tom.util.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The type Test serialization.
 */
public class TestSerialization {

    /**
     * The entry point of application.
     *
     * @param args the command line arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        TOMMessage tm = new TOMMessage(0, 0, 0, 0, new String("abc").getBytes(), 0, TOMMessageType.ORDERED_REQUEST);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
        DataOutputStream oos = new DataOutputStream(baos);

        tm.wExternal(oos);
        oos.flush();
        //oos.writeObject(tm);

        byte[] message = baos.toByteArray();
        Logger.println("message length" + message.length);

        ByteArrayInputStream bais = new ByteArrayInputStream(message);
        DataInputStream ois = new DataInputStream(bais);

        //TOMMessage tm2 = (TOMMessage) ois.readObject();
        TOMMessage tm2 = new TOMMessage();
        tm2.rExternal(ois);

        //        Logger.println((new String(tm2.getContent()));
    }

}
