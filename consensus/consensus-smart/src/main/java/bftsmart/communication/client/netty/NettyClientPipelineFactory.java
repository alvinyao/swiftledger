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

import bftsmart.reconfiguration.ClientViewController;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Netty client pipeline factory.
 */
public class NettyClientPipelineFactory {

    /**
     * The Ncs.
     */
    NettyClientServerCommunicationSystemClientSide ncs;
    /**
     * The Session table.
     */
    Map sessionTable;
    /**
     * The Mac length.
     */
    int macLength;
    /**
     * The Signature length.
     */
    int signatureLength;

    /**
     * The Controller.
     */
    //******* EDUARDO BEGIN **************//
    ClientViewController controller;
    //******* EDUARDO END **************//

    /**
     * The Rl.
     */
    ReentrantReadWriteLock rl;

    /**
     * Instantiates a new Netty client pipeline factory.
     *
     * @param ncs             the ncs
     * @param sessionTable    the session table
     * @param macLength       the mac length
     * @param controller      the controller
     * @param rl              the rl
     * @param signatureLength the signature length
     */
    public NettyClientPipelineFactory(NettyClientServerCommunicationSystemClientSide ncs, Map sessionTable,
        int macLength, ClientViewController controller, ReentrantReadWriteLock rl, int signatureLength) {
        this.ncs = ncs;
        this.sessionTable = sessionTable;
        this.macLength = macLength;
        this.signatureLength = signatureLength;
        this.rl = rl;
        this.controller = controller;
    }

    /**
     * Gets decoder.
     *
     * @return the decoder
     */
    public ByteToMessageDecoder getDecoder() {
        return new NettyTOMMessageDecoder(true, sessionTable, macLength, controller, rl, signatureLength,
            controller.getStaticConf().getUseMACs() == 1 ? true : false);
    }

    /**
     * Gets encoder.
     *
     * @return the encoder
     */
    public MessageToByteEncoder getEncoder() {
        return new NettyTOMMessageEncoder(true, sessionTable, macLength, rl, signatureLength,
            controller.getStaticConf().getUseMACs() == 1 ? true : false);
    }

    /**
     * Gets handler.
     *
     * @return the handler
     */
    public SimpleChannelInboundHandler getHandler() {
        return ncs;
    }

}
