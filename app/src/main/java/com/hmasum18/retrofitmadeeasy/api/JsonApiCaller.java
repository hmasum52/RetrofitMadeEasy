package com.hmasum18.retrofitmadeeasy.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class fetch data from the remote server as json object or json array
 *
 * It calls api end point for data by retrofit2
 * and call OnCompleteListeners when data is received.
 *
 * @author Hasan Masum. Email: connectwithmasum@gmail.com
 * @version 1.0
 * @since 21/01/2021
 */
public class JsonApiCaller<T>{
    public static final String TAG = "JsonApiCaller:-->";
    private final JsonApiEndPoints apiEndPoints; //api endpoints
    private OnFinishListener<T> onFinishListener;
    private final Gson gson = new Gson();
    private final Type type;

    /**
     *  if we have a class name "Dummy"
     *  <ul>
     *      <li>type is Dummy.class for Dummy object</li>
     *      <li>type is Dummy[].class for a array of Dummy class</li>
     *      <li>type is new TypeToken<List<Dummy>>(){}.getType() for a list of Dummy class</li>
     *  </ul>
     *
    // * @param type is the type of response we want to get
     * @param baseUrl is baseUrl of the API endpoint
     */
    public JsonApiCaller(Type type, String baseUrl){
        this.type = type;
        apiEndPoints = ApiService.getInstance(baseUrl).getJsonApiEndPoints();
    }
    public void addOnFinishListener(OnFinishListener<T> onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public JsonApiCaller<T> GET(String relativePath){
        Call<JsonElement> call = apiEndPoints.GET(relativePath);
        this.enqueueRequest(call);
        return this;
    }

    public JsonApiCaller<T> GET(Map<String,String> headerMap, String relativePath){
        Call<JsonElement> call = apiEndPoints.GET(headerMap,relativePath);
        this.enqueueRequest(call);
        return this;
    }

    public JsonApiCaller<T> POST(String relativePath, Object object) {
        Call<JsonElement> call = apiEndPoints.POST(relativePath, object);
        Log.d(TAG, "POST()=> posting json element ");
        this.enqueueRequest(call);
        return this;
    }

    public JsonApiCaller<T> POST(Map<String,String> headerMap,String relativePath, Object object) {
        Call<JsonElement> call = apiEndPoints.POST(headerMap,relativePath, object);
        Log.d(TAG, "POST()=> posting json element ");
        this.enqueueRequest(call);
        return this;
    }

    private void enqueueRequest(Call<JsonElement> call){
        //for debugging
        //start
        Request request = call.request();
        Log.d(TAG,"enqueueRequest>method: "+request.method());
        Log.d(TAG,"enqueueRequest>url: "+request.url());
        Log.d(TAG,"enqueueRequest>headers: "+request.headers());
       // Log.d(TAG,"callForObject>body: "+request.body());
        //end

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement json = response.body();
                    Log.d(TAG,"enqueueRequest>onResponse: "+call.request().url()+" call is successful");
                    Log.d(TAG,"enqueueRequest>onResponse:"+type);
                    onFinishListener.onSuccess(gson.fromJson(json,type));
                }else{
                    onFinishListener.onFailure(new Exception("Error "+response.code()));
                    Log.d(TAG,"enqueueRequest>onResponse:  process is not successful "+response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                String className = t.getClass().toString();
                onFinishListener.onFailure(new Exception(t));
                if(className.endsWith("UnknownHostException") )
                    Log.d(TAG,"enqueueRequest>Server is not responding");
                else if(className.endsWith("JsonSyntaxException"))
                    Log.d(TAG,"enqueueRequest>Response is not a com.google.gson.JsonArray");
                t.printStackTrace();
            }
        });
    }
}
