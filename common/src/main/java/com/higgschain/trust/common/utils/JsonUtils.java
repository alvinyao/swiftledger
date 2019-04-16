package com.higgschain.trust.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;

/**
 * The type Json utils.
 *
 * @author duhongming
 * @date 2018 /8/1
 */
public class JsonUtils {
    /**
     * The constant JSON_GENERATE_FEATURES.
     */
    public final static int JSON_GENERATE_FEATURES;

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        int features = 0;
        features = features | SerializerFeature.QuoteFieldNames.getMask();
        features |= SerializerFeature.SkipTransientField.getMask();
        features |= SerializerFeature.WriteEnumUsingName.getMask();
        features |= SerializerFeature.MapSortField.getMask();
        JSON_GENERATE_FEATURES = features;
    }

    /**
     * Add deserializer.
     *
     * @param type         the type
     * @param deserializer the deserializer
     */
    public static void addDeserializer(Type type, ObjectDeserializer deserializer) {
        ParserConfig.getGlobalInstance().putDeserializer(type, deserializer);
    }

    /**
     * Serialize string.
     *
     * @param obj the obj
     * @return the string
     */
    public static String serialize(Object obj) {
        return JSON.toJSONString(obj, JSON_GENERATE_FEATURES);
    }

    /**
     * Serialize string.
     *
     * @param obj          the obj
     * @param prettyFormat the pretty format
     * @return the string
     */
    public static String serialize(Object obj, boolean prettyFormat) {
        return prettyFormat
                ? JSON.toJSONString(obj, (JSON_GENERATE_FEATURES | SerializerFeature.PrettyFormat.getMask()))
                : JSON.toJSONString(obj, JSON_GENERATE_FEATURES);
    }

    /**
     * Serialize with type string.
     *
     * @param obj the obj
     * @return the string
     */
    public static String serializeWithType(Object obj) {
        return JSON.toJSONString(obj, (JSON_GENERATE_FEATURES | SerializerFeature.WriteClassName.getMask()));
    }

    /**
     * Serialize with type string.
     *
     * @param obj          the obj
     * @param prettyFormat the pretty format
     * @return the string
     */
    public static String serializeWithType(Object obj, boolean prettyFormat) {
        return prettyFormat
                ? JSON.toJSONString(obj, (JSON_GENERATE_FEATURES | SerializerFeature.WriteClassName.getMask() | SerializerFeature.PrettyFormat.getMask()))
                : JSON.toJSONString(obj, JSON_GENERATE_FEATURES | SerializerFeature.WriteClassName.getMask());
    }

    /**
     * Parse object.
     *
     * @param json the json
     * @return the object
     */
    public static Object parse(String json) {
        Object obj = JSON.parse(json);
        return obj;
    }

    /**
     * Parse object json object.
     *
     * @param json the json
     * @return the json object
     */
    public static JSONObject parseObject(String json) {
        return JSON.parseObject(json);
    }

    /**
     * Parse object t.
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the t
     */
    public static <T> T  parseObject(String json, Class<T> clazz) {
        T obj = JSON.parseObject(json, clazz);
        return obj;
    }

    /**
     * Parse array object.
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the object
     */
    public static <T> Object parseArray(String json, Class<T> clazz) {
        Object obj = JSON.parseArray(json, clazz);
        return obj;
    }

    /**
     * Clone object.
     *
     * @param obj the obj
     * @return the object
     */
    public static Object clone(Object obj) {
        String json = serialize(obj);
        Object cloneObj = parse(json);
        return cloneObj;
    }

    /**
     * Clone t.
     *
     * @param <T>   the type parameter
     * @param obj   the obj
     * @param clazz the clazz
     * @return the t
     */
    public static <T> T clone(Object obj, Class<T> clazz) {
        String json = serialize(obj);
        T cloneObj = JSON.parseObject(json, clazz);
        return cloneObj;
    }
}
