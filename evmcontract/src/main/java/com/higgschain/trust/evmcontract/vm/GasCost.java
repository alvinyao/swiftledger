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
package com.higgschain.trust.evmcontract.vm;

/**
 * The fundamental network cost unit. Paid for exclusively by Ether, which is converted
 * freely to and from Gas as required. Gas does not exist outside of the internal Ethereum
 * computation engine; its price is set by the Transaction and miners are free to
 * ignore Transactions whose Gas price is too low.
 */
public class GasCost {

    /* backwards compatibility, remove eventually */
    private final int STEP = 1;
    private final int SSTORE = 300;
    /* backwards compatibility, remove eventually */

    private final int ZEROSTEP = 0;
    private final int QUICKSTEP = 2;
    private final int FASTESTSTEP = 3;
    private final int FASTSTEP = 5;
    private final int MIDSTEP = 8;
    private final int SLOWSTEP = 10;
    private final int EXTSTEP = 20;

    private final int GENESISGASLIMIT = 1000000;
    private final int MINGASLIMIT = 125000;

    private final int BALANCE = 20;
    private final int SHA3 = 30;
    private final int SHA3_WORD = 6;
    private final int SLOAD = 50;
    private final int STOP = 0;
    private final int SUICIDE = 0;
    private final int CLEAR_SSTORE = 5000;
    private final int SET_SSTORE = 20000;
    private final int RESET_SSTORE = 5000;
    private final int REFUND_SSTORE = 15000;
    private final int CREATE = 32000;

    private final int JUMPDEST = 1;
    private final int CREATE_DATA_BYTE = 5;
    private final int CALL = 40;
    private final int STIPEND_CALL = 2300;
    /**
     * value transfer call
     */
    private final int VT_CALL = 9000;

    /**
     * new account call
     */
    private final int NEW_ACCT_CALL = 25000;
    private final int MEMORY = 3;
    private final int SUICIDE_REFUND = 24000;
    private final int QUAD_COEFF_DIV = 512;
    private final int CREATE_DATA = 200;
    private final int TX_NO_ZERO_DATA = 68;
    private final int TX_ZERO_DATA = 4;
    private final int TRANSACTION = 21000;
    private final int TRANSACTION_CREATE_CONTRACT = 53000;
    private final int LOG_GAS = 375;
    private final int LOG_DATA_GAS = 8;
    private final int LOG_TOPIC_GAS = 375;
    private final int COPY_GAS = 3;
    private final int EXP_GAS = 10;
    private final int EXP_BYTE_GAS = 10;
    private final int IDENTITY = 15;
    private final int IDENTITY_WORD = 3;
    private final int RIPEMD160 = 600;
    private final int RIPEMD160_WORD = 120;
    private final int SHA256 = 60;
    private final int SHA256_WORD = 12;
    private final int EC_RECOVER = 3000;
    private final int EXT_CODE_SIZE = 20;
    private final int EXT_CODE_COPY = 20;
    private final int NEW_ACCT_SUICIDE = 0;

    /**
     * Gets step.
     *
     * @return the step
     */
    public int getSTEP() {
        return STEP;
    }

    /**
     * Gets sstore.
     *
     * @return the sstore
     */
    public int getSSTORE() {
        return SSTORE;
    }

    /**
     * Gets zerostep.
     *
     * @return the zerostep
     */
    public int getZEROSTEP() {
        return ZEROSTEP;
    }

    /**
     * Gets quickstep.
     *
     * @return the quickstep
     */
    public int getQUICKSTEP() {
        return QUICKSTEP;
    }

    /**
     * Gets fasteststep.
     *
     * @return the fasteststep
     */
    public int getFASTESTSTEP() {
        return FASTESTSTEP;
    }

    /**
     * Gets faststep.
     *
     * @return the faststep
     */
    public int getFASTSTEP() {
        return FASTSTEP;
    }

    /**
     * Gets midstep.
     *
     * @return the midstep
     */
    public int getMIDSTEP() {
        return MIDSTEP;
    }

    /**
     * Gets slowstep.
     *
     * @return the slowstep
     */
    public int getSLOWSTEP() {
        return SLOWSTEP;
    }

    /**
     * Gets extstep.
     *
     * @return the extstep
     */
    public int getEXTSTEP() {
        return EXTSTEP;
    }

    /**
     * Gets genesisgaslimit.
     *
     * @return the genesisgaslimit
     */
    public int getGENESISGASLIMIT() {
        return GENESISGASLIMIT;
    }

    /**
     * Gets mingaslimit.
     *
     * @return the mingaslimit
     */
    public int getMINGASLIMIT() {
        return MINGASLIMIT;
    }

    /**
     * Gets balance.
     *
     * @return the balance
     */
    public int getBALANCE() {
        return BALANCE;
    }

    /**
     * Gets sha 3.
     *
     * @return the sha 3
     */
    public int getSHA3() {
        return SHA3;
    }

    /**
     * Gets sha 3 word.
     *
     * @return the sha 3 word
     */
    public int getSHA3_WORD() {
        return SHA3_WORD;
    }

    /**
     * Gets sload.
     *
     * @return the sload
     */
    public int getSLOAD() {
        return SLOAD;
    }

    /**
     * Gets stop.
     *
     * @return the stop
     */
    public int getSTOP() {
        return STOP;
    }

    /**
     * Gets suicide.
     *
     * @return the suicide
     */
    public int getSUICIDE() {
        return SUICIDE;
    }

    /**
     * Gets clear sstore.
     *
     * @return the clear sstore
     */
    public int getCLEAR_SSTORE() {
        return CLEAR_SSTORE;
    }

    /**
     * Gets set sstore.
     *
     * @return the set sstore
     */
    public int getSET_SSTORE() {
        return SET_SSTORE;
    }

    /**
     * Gets reset sstore.
     *
     * @return the reset sstore
     */
    public int getRESET_SSTORE() {
        return RESET_SSTORE;
    }

    /**
     * Gets refund sstore.
     *
     * @return the refund sstore
     */
    public int getREFUND_SSTORE() {
        return REFUND_SSTORE;
    }

    /**
     * Gets create.
     *
     * @return the create
     */
    public int getCREATE() {
        return CREATE;
    }

    /**
     * Gets jumpdest.
     *
     * @return the jumpdest
     */
    public int getJUMPDEST() {
        return JUMPDEST;
    }

    /**
     * Gets create data byte.
     *
     * @return the create data byte
     */
    public int getCREATE_DATA_BYTE() {
        return CREATE_DATA_BYTE;
    }

    /**
     * Gets call.
     *
     * @return the call
     */
    public int getCALL() {
        return CALL;
    }

    /**
     * Gets stipend call.
     *
     * @return the stipend call
     */
    public int getSTIPEND_CALL() {
        return STIPEND_CALL;
    }

    /**
     * Gets vt call.
     *
     * @return the vt call
     */
    public int getVT_CALL() {
        return VT_CALL;
    }

    /**
     * Gets new acct call.
     *
     * @return the new acct call
     */
    public int getNEW_ACCT_CALL() {
        return NEW_ACCT_CALL;
    }

    /**
     * Gets new acct suicide.
     *
     * @return the new acct suicide
     */
    public int getNEW_ACCT_SUICIDE() {
        return NEW_ACCT_SUICIDE;
    }

    /**
     * Gets memory.
     *
     * @return the memory
     */
    public int getMEMORY() {
        return MEMORY;
    }

    /**
     * Gets suicide refund.
     *
     * @return the suicide refund
     */
    public int getSUICIDE_REFUND() {
        return SUICIDE_REFUND;
    }

    /**
     * Gets quad coeff div.
     *
     * @return the quad coeff div
     */
    public int getQUAD_COEFF_DIV() {
        return QUAD_COEFF_DIV;
    }

    /**
     * Gets create data.
     *
     * @return the create data
     */
    public int getCREATE_DATA() {
        return CREATE_DATA;
    }

    /**
     * Gets tx no zero data.
     *
     * @return the tx no zero data
     */
    public int getTX_NO_ZERO_DATA() {
        return TX_NO_ZERO_DATA;
    }

    /**
     * Gets tx zero data.
     *
     * @return the tx zero data
     */
    public int getTX_ZERO_DATA() {
        return TX_ZERO_DATA;
    }

    /**
     * Gets transaction.
     *
     * @return the transaction
     */
    public int getTRANSACTION() {
        return TRANSACTION;
    }

    /**
     * Gets transaction create contract.
     *
     * @return the transaction create contract
     */
    public int getTRANSACTION_CREATE_CONTRACT() {
        return TRANSACTION_CREATE_CONTRACT;
    }

    /**
     * Gets log gas.
     *
     * @return the log gas
     */
    public int getLOG_GAS() {
        return LOG_GAS;
    }

    /**
     * Gets log data gas.
     *
     * @return the log data gas
     */
    public int getLOG_DATA_GAS() {
        return LOG_DATA_GAS;
    }

    /**
     * Gets log topic gas.
     *
     * @return the log topic gas
     */
    public int getLOG_TOPIC_GAS() {
        return LOG_TOPIC_GAS;
    }

    /**
     * Gets copy gas.
     *
     * @return the copy gas
     */
    public int getCOPY_GAS() {
        return COPY_GAS;
    }

    /**
     * Gets exp gas.
     *
     * @return the exp gas
     */
    public int getEXP_GAS() {
        return EXP_GAS;
    }

    /**
     * Gets exp byte gas.
     *
     * @return the exp byte gas
     */
    public int getEXP_BYTE_GAS() {
        return EXP_BYTE_GAS;
    }

    /**
     * Gets identity.
     *
     * @return the identity
     */
    public int getIDENTITY() {
        return IDENTITY;
    }

    /**
     * Gets identity word.
     *
     * @return the identity word
     */
    public int getIDENTITY_WORD() {
        return IDENTITY_WORD;
    }

    /**
     * Gets ripemd 160.
     *
     * @return the ripemd 160
     */
    public int getRIPEMD160() {
        return RIPEMD160;
    }

    /**
     * Gets ripemd 160 word.
     *
     * @return the ripemd 160 word
     */
    public int getRIPEMD160_WORD() {
        return RIPEMD160_WORD;
    }

    /**
     * Gets sha 256.
     *
     * @return the sha 256
     */
    public int getSHA256() {
        return SHA256;
    }

    /**
     * Gets sha 256 word.
     *
     * @return the sha 256 word
     */
    public int getSHA256_WORD() {
        return SHA256_WORD;
    }

    /**
     * Gets ec recover.
     *
     * @return the ec recover
     */
    public int getEC_RECOVER() {
        return EC_RECOVER;
    }

    /**
     * Gets ext code size.
     *
     * @return the ext code size
     */
    public int getEXT_CODE_SIZE() {
        return EXT_CODE_SIZE;
    }

    /**
     * Gets ext code copy.
     *
     * @return the ext code copy
     */
    public int getEXT_CODE_COPY() {
        return EXT_CODE_COPY;
    }
}
