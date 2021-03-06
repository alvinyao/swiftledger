package com.higgschain.trust.rs.tx;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * The type Http utils.
 *
 * @author tangkun
 * @date 2018 -12-13
 */
public class HttpUtils {


    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient httpClient;

    /**
     * Post json string.
     *
     * @param url  the url
     * @param json the json
     * @return the string
     */
    public static String postJson(String url, Object json) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(json));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return execute(request);
    }

    /**
     * Get t.
     *
     * @param <T>        the type parameter
     * @param url        the url
     * @param resultType the result type
     * @return the t
     */
    public static <T> T get(String url, Type resultType) {
        Request request = new Request.Builder().url(url).build();
        return execute(request, resultType);
    }

    private static <T> T execute(Request request, Type resultType) {
        try {
            httpClient = new OkHttpClient.Builder().build();
            Call call = httpClient.newCall(request);
            Response response = call.execute();
            String jsonText = response.body().string();

            if (!response.isSuccessful()) {
                throw new RuntimeException(jsonText);
            }
            return JSON.<T>parseObject(jsonText, resultType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute string.
     *
     * @param request the request
     * @return the string
     */
    public static String execute(Request request) {
        try {
            httpClient = new OkHttpClient.Builder().build();
            Call call = httpClient.newCall(request);
            Response response = call.execute();
            String jsonText = response.body().string();
            if (!response.isSuccessful()) {
                throw new RuntimeException(jsonText);
            }
            return jsonText;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SignedTransaction signedTransaction = new SignedTransaction();
        System.out.println(postJson("http://localhost:7070/transaction/post", signedTransaction));

    }
}
