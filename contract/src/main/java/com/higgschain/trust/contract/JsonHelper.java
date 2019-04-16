package com.higgschain.trust.contract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * The type Json helper.
 *
 * @author duhongming
 * @date 2018 /6/13
 */
public class JsonHelper {
    /**
     * The constant JSON_GENERATE_FEATURES.
     */
    public final static int JSON_GENERATE_FEATURES;

    static {
        int features = 0;
        features = features | SerializerFeature.QuoteFieldNames.getMask();
        features |= SerializerFeature.SkipTransientField.getMask();
        features |= SerializerFeature.WriteEnumUsingName.getMask();
        features |= SerializerFeature.SortField.getMask();
        features |= SerializerFeature.MapSortField.getMask();
        JSON_GENERATE_FEATURES = features;
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
