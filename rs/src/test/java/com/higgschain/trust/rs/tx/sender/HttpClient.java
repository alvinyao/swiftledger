package com.higgschain.trust.rs.tx.sender;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * The type Http client.
 *
 * @author Chen Jiawei
 * @date 2019 -01-03
 */
@Slf4j
public class HttpClient {
    private Retrofit retrofit;

    /**
     * Instantiates a new Http client.
     *
     * @param serverIp   the server ip
     * @param serverPort the server port
     */
    public HttpClient(String serverIp, int serverPort) {
        this(serverIp, serverPort, 60, 60, 60);
    }

    /**
     * Instantiates a new Http client.
     *
     * @param serverIp       the server ip
     * @param serverPort     the server port
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param writeTimeout   the write timeout
     */
    public HttpClient(String serverIp, int serverPort, long connectTimeout, long readTimeout, long writeTimeout) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(String.format("http://%s:%s/", serverIp, serverPort))
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        log.info("HttpClient [serverIp=" + serverIp + ", serverPort=" + serverPort + "] is created");
    }

    /**
     * Create api t.
     *
     * @param <T>      the type parameter
     * @param apiClazz the api clazz
     * @return the t
     */
    public <T> T createApi(Class<T> apiClazz) {
        T api = retrofit.create(apiClazz);
        log.info("Service api " + apiClazz.getName() + " is ready");
        return api;
    }
}