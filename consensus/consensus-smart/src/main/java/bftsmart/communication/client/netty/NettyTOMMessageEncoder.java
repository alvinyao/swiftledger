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
package bftsmart.communication.client.netty;

import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Mac;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Netty tom message encoder.
 */
public class NettyTOMMessageEncoder extends MessageToByteEncoder<TOMMessage> {

    private boolean isClient;
    private Map sessionTable;
    private int macLength;
    private int signatureLength;
    private ReentrantReadWriteLock rl;
    private boolean useMAC;

    /**
     * Instantiates a new Netty tom message encoder.
     *
     * @param isClient        the is client
     * @param sessionTable    the session table
     * @param macLength       the mac length
     * @param rl              the rl
     * @param signatureLength the signature length
     * @param useMAC          the use mac
     */
    public NettyTOMMessageEncoder(boolean isClient, Map sessionTable, int macLength, ReentrantReadWriteLock rl,
        int signatureLength, boolean useMAC) {
        this.isClient = isClient;
        this.sessionTable = sessionTable;
        this.macLength = macLength;
        this.rl = rl;
        this.signatureLength = signatureLength;
        this.useMAC = useMAC;
    }

    @Override protected void encode(ChannelHandlerContext context, TOMMessage sm, ByteBuf buffer) throws Exception {
        byte[] msgData;
        byte[] macData = null;
        byte[] signatureData = null;

        msgData = sm.serializedMessage;
        if (sm.signed) {
            //signature was already produced before            
            signatureData = sm.serializedMessageSignature;
            if (signatureData.length != signatureLength)
                Logger.println("WARNING: message signature has size " + signatureData.length + " and should have "
                    + signatureLength);
        }

        if (useMAC) {
            macData = produceMAC(sm.destination, msgData, sm.getSender());
            if (macData == null) {
                Logger.println("uses MAC and the MAC returned is null. Won't write to channel");
                return;
            }
        }

        int dataLength = 1 + msgData.length + (macData == null ? 0 : macData.length) + (signatureData == null ? 0 :
            signatureData.length);

        //Logger.println("Sending message with "+dataLength+" bytes.");
        /* msg size */
        buffer.writeInt(dataLength);
        /* control byte indicating if the message is signed or not */
        buffer.writeByte(sm.signed == true ? (byte)1 : (byte)0);
        /* data to be sent */
        buffer.writeBytes(msgData);
        /* MAC */
        if (useMAC)
            buffer.writeBytes(macData);
        /* signature */
        if (signatureData != null)
            buffer.writeBytes(signatureData);

        context.flush();
    }

    /**
     * Produce mac byte [ ].
     *
     * @param id   the id
     * @param data the data
     * @param me   the me
     * @return the byte [ ]
     */
    byte[] produceMAC(int id, byte[] data, int me) {
        NettyClientServerSession session = (NettyClientServerSession)sessionTable.get(id);
        if (session == null) {
            Logger.println("NettyTOMMessageEncoder.produceMAC(). session for client " + id + " is null");
            return null;
        }
        Mac macSend = session.getMacSend();
        return macSend.doFinal(data);
    }

}
