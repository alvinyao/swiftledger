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
package bftsmart.tom.util;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Logger.
 */
public class Logger {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger("SmartLog");
    /**
     * The constant debug.
     */
    //public static long startInstant = System.currentTimeMillis();
    public static boolean debug = false;

    /**
     * Println.
     *
     * @param msg the msg
     */
    public static void println(String msg) {
        if (debug) {
            //            String dataActual = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(new Date());
            //            Logger.println((
            //                    "(" + dataActual
            //                    + " - " + Thread.currentThread().getName()
            //                    + ") " + msg);
            log.info(msg);
        }
    }

    /**
     * Println 2.
     *
     * @param l   the l
     * @param msg the msg
     */
    public static void println2(java.util.logging.Logger l, String msg) {
        if (debug) {
            String dataActual = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
            l.info("(" + dataActual
                //+ " - " + Thread.currentThread().getName()
                //+ " - " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName()+":"+stackTraceElement.getLineNumber()
                + ") " + msg);
        }
    }

    /**
     * Print error.
     *
     * @param msg the msg
     * @param e   the e
     */
    public static void printError(String msg, Throwable e) {
        log.error(msg, e);
    }

    /**
     * Print error.
     *
     * @param msg the msg
     */
    public static void printError(String msg) {
        log.error(msg);
    }
}
