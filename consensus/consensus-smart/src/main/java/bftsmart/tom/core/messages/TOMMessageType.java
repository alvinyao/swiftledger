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
package bftsmart.tom.core.messages;

/**
 * Possible types of TOMMessage
 *
 * @author alysson
 */
public enum TOMMessageType {/**
 * Ordered request tom message type.
 */
ORDERED_REQUEST, //0
    /**
     * Unordered request tom message type.
     */
    UNORDERED_REQUEST, //1
    /**
     * Reply tom message type.
     */
    REPLY, //2
    /**
     * Reconfig tom message type.
     */
    RECONFIG, //3
    /**
     * Ask status tom message type.
     */
    ASK_STATUS, // 4
    /**
     * Status reply tom message type.
     */
    STATUS_REPLY,// 5
    /**
     * Unordered hashed request tom message type.
     */
    UNORDERED_HASHED_REQUEST; //6

    /**
     * To int int.
     *
     * @return the int
     */
    public int toInt() {
        switch (this) {
            case ORDERED_REQUEST:
                return 0;
            case UNORDERED_REQUEST:
                return 1;
            case REPLY:
                return 2;
            case RECONFIG:
                return 3;
            case ASK_STATUS:
                return 4;
            case STATUS_REPLY:
                return 5;
            case UNORDERED_HASHED_REQUEST:
                return 6;
            default:
                return -1;
        }
    }

    /**
     * From int tom message type.
     *
     * @param i the
     * @return the tom message type
     */
    public static TOMMessageType fromInt(int i) {
        switch (i) {
            case 0:
                return ORDERED_REQUEST;
            case 1:
                return UNORDERED_REQUEST;
            case 2:
                return REPLY;
            case 3:
                return RECONFIG;
            case 4:
                return ASK_STATUS;
            case 5:
                return STATUS_REPLY;
            case 6:
                return UNORDERED_HASHED_REQUEST;
            default:
                return RECONFIG;
        }
    }
}
