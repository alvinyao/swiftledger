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
package com.higgschain.trust.evmcontract.vm.program.invoke;

import com.higgschain.trust.evmcontract.core.Block;
import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.core.Transaction;
import com.higgschain.trust.evmcontract.db.BlockStore;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.program.Program;

import java.math.BigInteger;

/**
 * The interface Program invoke factory.
 *
 * @author Roman Mandeleil
 * @since 19.12.2014
 */
public interface ProgramInvokeFactory {

    /**
     * Create program invoke program invoke.
     *
     * @param tx         the tx
     * @param block      the block
     * @param repository the repository
     * @param blockStore the block store
     * @return the program invoke
     */
    ProgramInvoke createProgramInvoke(Transaction tx, Block block,
                                      Repository repository, BlockStore blockStore);

    /**
     * Create program invoke program invoke.
     *
     * @param program        the program
     * @param toAddress      the to address
     * @param callerAddress  the caller address
     * @param inValue        the in value
     * @param inGas          the in gas
     * @param balanceInt     the balance int
     * @param dataIn         the data in
     * @param repository     the repository
     * @param blockStore     the block store
     * @param staticCall     the static call
     * @param byTestingSuite the by testing suite
     * @return the program invoke
     */
    ProgramInvoke createProgramInvoke(Program program, DataWord toAddress, DataWord callerAddress,
                                      DataWord inValue, DataWord inGas,
                                      BigInteger balanceInt, byte[] dataIn,
                                      Repository repository, BlockStore blockStore,
                                      boolean staticCall, boolean byTestingSuite);


}
