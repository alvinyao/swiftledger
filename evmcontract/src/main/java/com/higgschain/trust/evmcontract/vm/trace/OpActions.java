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
package com.higgschain.trust.evmcontract.vm.trace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.higgschain.trust.evmcontract.vm.DataWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.higgschain.trust.evmcontract.util.ByteUtil.toHexString;

/**
 * The type Op actions.
 */
public class OpActions {

    /**
     * The type Action.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Action {

        /**
         * The enum Name.
         */
        public enum Name {/**
         * Pop name.
         */
        pop,
            /**
             * Push name.
             */
            push,
            /**
             * Swap name.
             */
            swap,
            /**
             * Extend name.
             */
            extend,
            /**
             * Write name.
             */
            write,
            /**
             * Put name.
             */
            put,
            /**
             * Remove name.
             */
            remove,
            /**
             * Clear name.
             */
            clear;
        }

        private Name name;
        private Map<String, Object> params;

        /**
         * Gets name.
         *
         * @return the name
         */
        public Name getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(Name name) {
            this.name = name;
        }

        /**
         * Gets params.
         *
         * @return the params
         */
        public Map<String, Object> getParams() {
            return params;
        }

        /**
         * Sets params.
         *
         * @param params the params
         */
        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        /**
         * Add param action.
         *
         * @param name  the name
         * @param value the value
         * @return the action
         */
        Action addParam(String name, Object value) {
            if (value != null) {
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put(name, value.toString());
            }
            return this;
        }
    }

    private List<Action> stack = new ArrayList<>();
    private List<Action> memory = new ArrayList<>();
    private List<Action> storage = new ArrayList<>();

    /**
     * Gets stack.
     *
     * @return the stack
     */
    public List<Action> getStack() {
        return stack;
    }

    /**
     * Sets stack.
     *
     * @param stack the stack
     */
    public void setStack(List<Action> stack) {
        this.stack = stack;
    }

    /**
     * Gets memory.
     *
     * @return the memory
     */
    public List<Action> getMemory() {
        return memory;
    }

    /**
     * Sets memory.
     *
     * @param memory the memory
     */
    public void setMemory(List<Action> memory) {
        this.memory = memory;
    }

    /**
     * Gets storage.
     *
     * @return the storage
     */
    public List<Action> getStorage() {
        return storage;
    }

    /**
     * Sets storage.
     *
     * @param storage the storage
     */
    public void setStorage(List<Action> storage) {
        this.storage = storage;
    }

    private static Action addAction(List<Action> container, Action.Name name) {
        Action action = new Action();
        action.setName(name);

        container.add(action);

        return action;
    }

    /**
     * Add stack pop action.
     *
     * @return the action
     */
    public Action addStackPop() {
        return addAction(stack, Action.Name.pop);
    }

    /**
     * Add stack push action.
     *
     * @param value the value
     * @return the action
     */
    public Action addStackPush(DataWord value) {
        return addAction(stack, Action.Name.push)
                .addParam("value", value);
    }

    /**
     * Add stack swap action.
     *
     * @param from the from
     * @param to   the to
     * @return the action
     */
    public Action addStackSwap(int from, int to) {
        return addAction(stack, Action.Name.swap)
                .addParam("from", from)
                .addParam("to", to);
    }

    /**
     * Add memory extend action.
     *
     * @param delta the delta
     * @return the action
     */
    public Action addMemoryExtend(long delta) {
        return addAction(memory, Action.Name.extend)
                .addParam("delta", delta);
    }

    /**
     * Add memory write action.
     *
     * @param address the address
     * @param data    the data
     * @param size    the size
     * @return the action
     */
    public Action addMemoryWrite(int address, byte[] data, int size) {
        return addAction(memory, Action.Name.write)
                .addParam("address", address)
                .addParam("data", toHexString(data).substring(0, size));
    }

    /**
     * Add storage put action.
     *
     * @param key   the key
     * @param value the value
     * @return the action
     */
    public Action addStoragePut(DataWord key, DataWord value) {
        return addAction(storage, Action.Name.put)
                .addParam("key", key)
                .addParam("value", value);
    }

    /**
     * Add storage remove action.
     *
     * @param key the key
     * @return the action
     */
    public Action addStorageRemove(DataWord key) {
        return addAction(storage, Action.Name.remove)
                .addParam("key", key);
    }

    /**
     * Add storage clear action.
     *
     * @return the action
     */
    public Action addStorageClear() {
        return addAction(storage, Action.Name.clear);
    }
}
