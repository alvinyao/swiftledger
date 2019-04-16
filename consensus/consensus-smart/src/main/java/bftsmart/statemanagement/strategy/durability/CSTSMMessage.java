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
package bftsmart.statemanagement.strategy.durability;

import bftsmart.reconfiguration.views.View;
import bftsmart.statemanagement.ApplicationState;
import bftsmart.statemanagement.SMMessage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The type Cstsm message.
 */
public class CSTSMMessage extends SMMessage {

    private CSTRequestF1 cstConfig;

    /**
     * Instantiates a new Cstsm message.
     *
     * @param sender    the sender
     * @param cid       the cid
     * @param type      the type
     * @param cstConfig the cst config
     * @param state     the state
     * @param view      the view
     * @param regency   the regency
     * @param leader    the leader
     */
    public CSTSMMessage(int sender, int cid, int type, CSTRequestF1 cstConfig, ApplicationState state, View view,
        int regency, int leader) {
        super(sender, cid, type, state, view, regency, leader);
        this.cstConfig = cstConfig;
    }

    /**
     * Instantiates a new Cstsm message.
     */
    public CSTSMMessage() {
        super();
    }

    /**
     * Gets cst config.
     *
     * @return the cst config
     */
    public CSTRequestF1 getCstConfig() {
        return cstConfig;
    }

    @Override public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(cstConfig);
    }

    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        cstConfig = (CSTRequestF1)in.readObject();
    }

}
