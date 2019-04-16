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
package com.higgschain.trust.evmcontract.solidity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.crypto.HashUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;
import static com.higgschain.trust.evmcontract.crypto.HashUtil.sha3;
import static java.lang.String.format;
import static org.apache.commons.collections4.ListUtils.select;
import static org.apache.commons.lang3.ArrayUtils.subarray;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.stripEnd;

/**
 * The type Abi.
 */
public class Abi extends ArrayList<Abi.Entry> {
    private final static ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

    private static final String WORD_PATTERN = "(\\w*\\[\\]|\\w*)*";
    private static final String RESULT_PATTERN = "^\\((.*)\\)\\W";

    /**
     * From json abi.
     *
     * @param json the json
     * @return the abi
     */
    public static Abi fromJson(String json) {
        try {
            return DEFAULT_MAPPER.readValue(json, Abi.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dump result string.
     *
     * @param results the results
     * @return the string
     */
    public static String dumpResult(List<?> results) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Object obj : results) {
            sb.append("[" + index + "]=");
            if (obj instanceof byte[]) {
                sb.append(Hex.toHexString((byte[]) obj)).append("\n");
            } else if (obj instanceof Object[]) {
                sb.append("List\n");
                Object[] arr = (Object[]) obj;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] instanceof byte[]) {
                        sb.append(String.format("    [%s]=%s\n", i, Hex.toHexString((byte[]) arr[i])));
                    } else {
                        sb.append(String.format("    [%s]=%s\n", i, arr[i].toString()));
                    }
                }
            } else {
                sb.append(obj.toString()).append("\n");
            }
            index++;
        }
        return sb.toString();
    }

    private static List<String> extractWords(String text) {
        Pattern pattern = Pattern.compile(WORD_PATTERN);
        Matcher matcher = pattern.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            String word = matcher.group();
            if (StringUtils.isNoneBlank(word)) {
                words.add(word);
            }
        }
        return words;
    }

    private static byte[] encodeArguments(List<Entry.Param> inputs, Object... args) {
        if (args.length > inputs.size()) {
            throw new RuntimeException("Too many arguments: " + args.length + " > " + inputs.size());
        }

        int staticSize = 0;
        int dynamicCnt = 0;
        // calculating static size and number of dynamic params
        for (int i = 0; i < args.length; i++) {
            SolidityType type = inputs.get(i).type;
            if (type.isDynamicType()) {
                dynamicCnt++;
            }
            staticSize += type.getFixedSize();
        }

        byte[][] bb = new byte[args.length + dynamicCnt][];
        for (int curDynamicPtr = staticSize, curDynamicCnt = 0, i = 0; i < args.length; i++) {
            SolidityType type = inputs.get(i).type;
            if (type.isDynamicType()) {
                byte[] dynBB = type.encode(args[i]);
                bb[i] = SolidityType.IntType.encodeInt(curDynamicPtr);
                bb[args.length + curDynamicCnt] = dynBB;
                curDynamicCnt++;
                curDynamicPtr += dynBB.length;
            } else {
                bb[i] = type.encode(args[i]);
            }
        }

        return ByteUtil.merge(bb);
    }

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Abi.Entry> T find(Class<T> resultClass, final Abi.Entry.Type type, final Predicate<T> searchPredicate) {
        return (T) CollectionUtils.find(this, entry -> entry.type == type && searchPredicate.evaluate((T) entry));
    }

    /**
     * Find function function.
     *
     * @param searchPredicate the search predicate
     * @return the function
     */
    public Function findFunction(Predicate<Function> searchPredicate) {
        return find(Function.class, Abi.Entry.Type.function, searchPredicate);
    }

    /**
     * Find event event.
     *
     * @param searchPredicate the search predicate
     * @return the event
     */
    public Event findEvent(Predicate<Event> searchPredicate) {
        return find(Event.class, Abi.Entry.Type.event, searchPredicate);
    }

    /**
     * Find constructor abi . constructor.
     *
     * @return the abi . constructor
     */
    public Abi.Constructor findConstructor() {
        return find(Constructor.class, Entry.Type.constructor, object -> true);
    }

    @Override
    public String toString() {
        return toJson();
    }

    /**
     * The type Entry.
     */
    @JsonInclude(Include.NON_NULL)
    public static abstract class Entry {

        /**
         * The Anonymous.
         */
        public final Boolean anonymous;
        /**
         * The Constant.
         */
        public final Boolean constant;
        /**
         * The Name.
         */
        public final String name;
        /**
         * The Inputs.
         */
        public final List<Param> inputs;
        /**
         * The Outputs.
         */
        public final List<Param> outputs;
        /**
         * The Type.
         */
        public final Type type;
        /**
         * The Payable.
         */
        public final Boolean payable;

        /**
         * Instantiates a new Entry.
         *
         * @param anonymous the anonymous
         * @param constant  the constant
         * @param name      the name
         * @param inputs    the inputs
         * @param outputs   the outputs
         * @param type      the type
         * @param payable   the payable
         */
        public Entry(Boolean anonymous, Boolean constant, String name, List<Param> inputs, List<Param> outputs, Type type, Boolean payable) {
            this.anonymous = anonymous;
            this.constant = constant;
            this.name = name;
            this.inputs = inputs;
            this.outputs = outputs;
            this.type = type;
            this.payable = payable;
        }

        /**
         * Create entry.
         *
         * @param anonymous the anonymous
         * @param constant  the constant
         * @param name      the name
         * @param inputs    the inputs
         * @param outputs   the outputs
         * @param type      the type
         * @param payable   the payable
         * @return the entry
         */
        @JsonCreator
        public static Entry create(@JsonProperty("anonymous") boolean anonymous,
                                   @JsonProperty("constant") boolean constant,
                                   @JsonProperty("name") String name,
                                   @JsonProperty("inputs") List<Param> inputs,
                                   @JsonProperty("outputs") List<Param> outputs,
                                   @JsonProperty("type") Type type,
                                   @JsonProperty(value = "payable", required = false, defaultValue = "false") Boolean payable) {
            Entry result = null;
            switch (type) {
                case constructor:
                    result = new Constructor(inputs, outputs);
                    break;
                case function:
                case fallback:
                    result = new Function(constant, name, inputs, outputs, payable);
                    break;
                case event:
                    result = new Event(anonymous, name, inputs, outputs);
                    break;
                default:
                    break;
            }

            return result;
        }

        /**
         * Format signature string.
         *
         * @return the string
         */
        public String formatSignature() {
            StringBuilder paramsTypes = new StringBuilder();
            for (Entry.Param param : inputs) {
                paramsTypes.append(param.type.getCanonicalName()).append(",");
            }

            return format("%s(%s)", name, stripEnd(paramsTypes.toString(), ","));
        }

        /**
         * Fingerprint signature byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] fingerprintSignature() {
            return HashUtil.sha3(formatSignature().getBytes());
        }

        /**
         * Encode signature byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] encodeSignature() {
            return fingerprintSignature();
        }

        /**
         * The enum Type.
         */
        public enum Type {/**
         * Constructor type.
         */
        constructor,
            /**
             * Function type.
             */
            function,
            /**
             * Event type.
             */
            event,
            /**
             * Fallback type.
             */
            fallback
        }

        /**
         * The type Param.
         */
        @JsonInclude(Include.NON_NULL)
        public static class Param {
            /**
             * The Indexed.
             */
            public Boolean indexed;
            /**
             * The Name.
             */
            public String name;
            /**
             * The Type.
             */
            public SolidityType type;

            /**
             * Decode list list.
             *
             * @param params  the params
             * @param encoded the encoded
             * @return the list
             */
            public static List<?> decodeList(List<Param> params, byte[] encoded) {
                return decodeList(params, encoded, false);
            }

            /**
             * Decode list list.
             *
             * @param params        the params
             * @param encoded       the encoded
             * @param humanReadable the human readable
             * @return the list
             */
            public static List<?> decodeList(List<Param> params, byte[] encoded, boolean humanReadable) {
                List<Object> result = new ArrayList<>(params.size());

                int offset = 0;
                for (Param param : params) {
                    Object decoded = param.type.isDynamicType()
                            ? param.type.decode(encoded, SolidityType.IntType.decodeInt(encoded, offset).intValue())
                            : param.type.decode(encoded, offset);
                    if (humanReadable) {
                        if (param.type instanceof SolidityType.StringType) {
                            result.add(decoded);
                        } else if (param.type instanceof SolidityType.AddressType) {
                            result.add(Hex.toHexString((byte[]) decoded));
                        } else if (param.type.isDynamicType()) {
                            List dynResult = new ArrayList<>();
                            Object[] arr = (Object[]) decoded;
                            for (Object value : arr) {
                                convertAndAdd(dynResult, value);
                            }
                            result.add(dynResult);
                        } else {
                            convertAndAdd(result, decoded);
                        }
                    } else {
                        result.add(decoded);
                    }

                    offset += param.type.getFixedSize();
                }

                return result;
            }

            private static void convertAndAdd(List<Object> result, Object value) {
                if ((value instanceof byte[])) {
                    result.add(Hex.toHexString((byte[]) value));
                } else {
                    result.add(value);
                }
            }

            @Override
            public String toString() {
                return format("%s%s%s", type.getCanonicalName(), (indexed != null && indexed) ? " indexed " : " ", name);
            }
        }
    }

    /**
     * The type Constructor.
     */
    public static class Constructor extends Entry {

        /**
         * Instantiates a new Constructor.
         *
         * @param inputs  the inputs
         * @param outputs the outputs
         */
        public Constructor(List<Param> inputs, List<Param> outputs) {
            super(null, null, "", inputs, outputs, Type.constructor, false);
        }

        /**
         * Of byte [ ].
         *
         * @param constructor the constructor
         * @param code        the code
         * @param args        the args
         * @return the byte [ ]
         */
        public static byte[] of(String constructor, byte[] code, Object... args) {
            if (args == null) {
                args = new Object[0];
            }
            List<Abi.Entry.Param> inputs = new ArrayList<>();
            List<String> names = extractWords(constructor);
            for (int i = 1; i < names.size(); i++) {
                Abi.Entry.Param param = new Abi.Entry.Param();
                param.type = SolidityType.getType(names.get(i));
                inputs.add(param);
            }

            byte[] argsData = Abi.encodeArguments(inputs, args);
            return ByteUtil.merge(code, argsData);
        }

        /**
         * Decode list.
         *
         * @param encoded the encoded
         * @return the list
         */
        public List<?> decode(byte[] encoded) {
            return Param.decodeList(inputs, encoded);
        }

        /**
         * Format signature string.
         *
         * @param contractName the contract name
         * @return the string
         */
        public String formatSignature(String contractName) {
            return format("function %s(%s)", contractName, join(inputs, ", "));
        }


    }

    /**
     * The type Function.
     */
    public static class Function extends Entry {
        /**
         * Of function.
         *
         * @param signature the signature
         * @return the function
         */
        public static Function of(String signature) {
            return FunctionParser.getInstance().parse(signature);
        }

        private static final int ENCODED_SIGN_LENGTH = 4;

        /**
         * Instantiates a new Function.
         *
         * @param constant the constant
         * @param name     the name
         * @param inputs   the inputs
         * @param outputs  the outputs
         * @param payable  the payable
         */
        public Function(boolean constant, String name, List<Param> inputs, List<Param> outputs, Boolean payable) {
            super(null, constant, name, inputs, outputs, Type.function, payable);
        }

        /**
         * Of with strict function.
         *
         * @param func the func
         * @return the function
         */
        @Deprecated
        public static Function ofWithStrict(String func) {
            Pattern pattern = Pattern.compile(RESULT_PATTERN);
            List<Abi.Entry.Param> inputs = new ArrayList<>();
            List<Abi.Entry.Param> outputs = new ArrayList<>();

            Matcher matcher = pattern.matcher(func);
            if (matcher.find()) {
                func = func.replace(matcher.group(), "");
                String results = matcher.group(1);
                if (StringUtils.isNotEmpty(results)) {
                    extractWords(results).forEach(result -> {
                        Abi.Entry.Param param = new Abi.Entry.Param();
                        param.type = SolidityType.getType(result);
                        outputs.add(param);
                    });
                }
            }

            List<String> names = extractWords(func);
            for (int i = 1; i < names.size(); i++) {
                Abi.Entry.Param param = new Abi.Entry.Param();
                param.type = SolidityType.getType(names.get(i));
                inputs.add(param);
            }

            Abi.Function function = new Abi.Function(false, names.get(0), inputs, outputs, false);
            return function;
        }

        /**
         * Extract signature byte [ ].
         *
         * @param data the data
         * @return the byte [ ]
         */
        public static byte[] extractSignature(byte[] data) {
            return subarray(data, 0, ENCODED_SIGN_LENGTH);
        }

        /**
         * Encode byte [ ].
         *
         * @param args the args
         * @return the byte [ ]
         */
        public byte[] encode(Object... args) {
            return ByteUtil.merge(encodeSignature(), encodeArguments(args));
        }

        private byte[] encodeArguments(Object... args) {
            return Abi.encodeArguments(inputs, args);
        }

        /**
         * Decode list.
         *
         * @param encoded the encoded
         * @return the list
         */
        public List<?> decode(byte[] encoded) {
            return Param.decodeList(inputs, subarray(encoded, ENCODED_SIGN_LENGTH, encoded.length));
        }

        /**
         * Decode result list.
         *
         * @param encoded the encoded
         * @return the list
         */
        public List<?> decodeResult(byte[] encoded) {
            return Param.decodeList(outputs, encoded, false);
        }

        /**
         * Decode result list.
         *
         * @param encoded       the encoded
         * @param humanReadable the human readable
         * @return the list
         */
        public List<?> decodeResult(byte[] encoded, boolean humanReadable) {
            return Param.decodeList(outputs, encoded, humanReadable);
        }

        @Override
        public byte[] encodeSignature() {
            return extractSignature(super.encodeSignature());
        }

        @Override
        public String toString() {
            String returnTail = "";
            if (constant) {
                returnTail += " constant";
            }
            if (!outputs.isEmpty()) {
                List<String> types = new ArrayList<>();
                for (Param output : outputs) {
                    types.add(output.type.getCanonicalName());
                }
                returnTail += format(" returns(%s)", join(types, ", "));
            }

            return format("function %s(%s)%s;", name, join(inputs, ", "), returnTail);
        }
    }

    /**
     * The type Event.
     */
    public static class Event extends Entry {
        /**
         * From signature event.
         *
         * @param signature the signature
         * @return the event
         */
        public static Event fromSignature(String signature) {
            return EventParser.getInstance().parse(signature);
        }

        /**
         * Instantiates a new Event.
         *
         * @param anonymous the anonymous
         * @param name      the name
         * @param inputs    the inputs
         * @param outputs   the outputs
         */
        public Event(boolean anonymous, String name, List<Param> inputs, List<Param> outputs) {
            super(anonymous, null, name, inputs, outputs, Type.event, false);
        }

        /**
         * Decode list.
         *
         * @param data   the data
         * @param topics the topics
         * @return the list
         */
        public List<?> decode(byte[] data, byte[][] topics) {
            List<Object> result = new ArrayList<>(inputs.size());

            byte[][] argTopics = anonymous ? topics : subarray(topics, 1, topics.length);
            List<Param> indexedParams = filteredInputs(true);
            List<Object> indexed = new ArrayList<>();
            for (int i = 0; i < indexedParams.size(); i++) {
                Object decodedTopic;
                if (indexedParams.get(i).type.isDynamicType()) {
                    // If arrays (including string and bytes) are used as indexed arguments,
                    // the Keccak-256 hash of it is stored as topic instead.
                    decodedTopic = SolidityType.Bytes32Type.decodeBytes32(argTopics[i], 0);
                } else {
                    decodedTopic = indexedParams.get(i).type.decode(argTopics[i]);
                }
                indexed.add(decodedTopic);
            }
            List<?> notIndexed = Param.decodeList(filteredInputs(false), data);

            for (Param input : inputs) {
                result.add(input.indexed ? indexed.remove(0) : notIndexed.remove(0));
            }

            return result;
        }

        private List<Param> filteredInputs(final boolean indexed) {
            return select(inputs, param -> param.indexed == indexed);
        }

        @Override
        public String toString() {
            return format("event %s(%s);", name, join(inputs, ", "));
        }
    }
}
