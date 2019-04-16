/*
 * Copyright 2014-2016 the libsecp256k1 contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.higgschain.trust.common.crypto.ecc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Native secp 256 k 1 util.
 */
public class NativeSecp256k1Util {

    private static final Logger log = LoggerFactory.getLogger(NativeSecp256k1Util.class);

    /**
     * Assert equals.
     *
     * @param val     the val
     * @param val2    the val 2
     * @param message the message
     * @throws AssertFailException the assert fail exception
     */
    public static void assertEquals(int val, int val2, String message) throws AssertFailException {
        if (val != val2)
            throw new AssertFailException("FAIL: " + message);
    }

    /**
     * Assert equals.
     *
     * @param val     the val
     * @param val2    the val 2
     * @param message the message
     * @throws AssertFailException the assert fail exception
     */
    public static void assertEquals(boolean val, boolean val2, String message) throws AssertFailException {
        if (val != val2)
            throw new AssertFailException("FAIL: " + message);
        else
            log.debug("PASS: " + message);
    }

    /**
     * Assert equals.
     *
     * @param val     the val
     * @param val2    the val 2
     * @param message the message
     * @throws AssertFailException the assert fail exception
     */
    public static void assertEquals(String val, String val2, String message) throws AssertFailException {
        if (!val.equals(val2))
            throw new AssertFailException("FAIL: " + message);
        else
            log.debug("PASS: " + message);
    }

    /**
     * The type Assert fail exception.
     */
    public static class AssertFailException extends Exception {
        /**
         * Instantiates a new Assert fail exception.
         *
         * @param message the message
         */
        public AssertFailException(String message) {
            super(message);
        }
    }
}