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
package bftsmart.tom.server;

import bftsmart.tom.MessageContext;

/**
 * Executables that implement this interface will get requests
 * delivered in FIFO order.
 *
 * @author Marcel Santos
 */
public interface FIFOExecutable extends SingleExecutable {

    /**
     * Execute ordered fifo byte [ ].
     *
     * @param command     the command
     * @param msgCtx      the msg ctx
     * @param clientId    the client id
     * @param operationId the operation id
     * @return the byte [ ]
     */
    public byte[] executeOrderedFIFO(byte[] command, MessageContext msgCtx, int clientId, int operationId);

    /**
     * Execute unordered fifo byte [ ].
     *
     * @param command     the command
     * @param msgCtx      the msg ctx
     * @param clientId    the client id
     * @param operationId the operation id
     * @return the byte [ ]
     */
    public byte[] executeUnorderedFIFO(byte[] command, MessageContext msgCtx, int clientId, int operationId);

}
