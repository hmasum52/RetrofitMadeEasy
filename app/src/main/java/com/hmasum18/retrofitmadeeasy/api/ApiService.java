package com.hmasum18.retrofitmadeeasy.api;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Hasan Masum
 * @version 1.1
 */
public class ApiService {
    public static final String TAG = "ApiService->";
    private static final Map<String, ApiService> instanceMap = new HashMap<>();
    private final JsonApiEndPoints jsonApiEndPoints;

    private ApiService(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApiEndPoints = retrofit.create(JsonApiEndPoints.class);
        System.out.println(TAG+"Retrofit Api is created successfully");
    }

    public static synchronized ApiService getInstance(String baseUrl) {
        if(instanceMap.get(baseUrl) == null){
            instanceMap.put(baseUrl,new ApiService(baseUrl));
        }
        return instanceMap.get(baseUrl);
    }

    public JsonApiEndPoints getJsonApiEndPoints() {
        return jsonApiEndPoints;
    }
}
