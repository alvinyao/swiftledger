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
package com.higgschain.trust.evmcontract.solidity.compiler;

/**
 * The type Contract exception.
 */
public class ContractException extends RuntimeException {

    /**
     * Instantiates a new Contract exception.
     *
     * @param message the message
     */
    public ContractException(String message) {
        super(message);
    }

    /**
     * Permission error contract exception.
     *
     * @param msg  the msg
     * @param args the args
     * @return the contract exception
     */
    public static ContractException permissionError(String msg, Object... args) {
        return error("contract permission error", msg, args);
    }

    /**
     * Compilation error contract exception.
     *
     * @param msg  the msg
     * @param args the args
     * @return the contract exception
     */
    public static ContractException compilationError(String msg, Object... args) {
        return error("contract compilation error", msg, args);
    }

    /**
     * Validation error contract exception.
     *
     * @param msg  the msg
     * @param args the args
     * @return the contract exception
     */
    public static ContractException validationError(String msg, Object... args) {
        return error("contract validation error", msg, args);
    }

    /**
     * Assemble error contract exception.
     *
     * @param msg  the msg
     * @param args the args
     * @return the contract exception
     */
    public static ContractException assembleError(String msg, Object... args) {
        return error("contract assemble error", msg, args);
    }

    private static ContractException error(String title, String message, Object... args) {
        return new ContractException(title + ": " + String.format(message, args));
    }
}
