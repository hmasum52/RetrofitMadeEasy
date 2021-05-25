package com.hmasum18.retrofitmadeeasy.jsonplaceholder;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.reflect.TypeToken;
import com.hmasum18.retrofitmadeeasy.api.JsonApiCaller;
import com.hmasum18.retrofitmadeeasy.api.OnFinishListener;

import java.lang.reflect.Type;
import java.util.List;

public class JsonPlaceHolder {
    public static final String TAG = "JsonPlaceHolder->";
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public MutableLiveData<Topic> fetchTopic(){
        MutableLiveData<Topic> mutableLiveData = new MutableLiveData<>();

        JsonApiCaller<Topic> jsonApiCaller = new JsonApiCaller<>(Topic.class,BASE_URL);
        jsonApiCaller.GET("posts/1")
                .addOnFinishListener(new OnFinishListener<Topic>() {
                    @Override
                    public void onSuccess(Topic topic) {
                        Log.d(TAG,"Success");
                        Log.d(TAG,"fetchTopic()->"+topic.toString());
                        mutableLiveData.postValue(topic);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG,"Failed");
                    }
                });
        return mutableLiveData;
    }

    public MutableLiveData<List<Topic>> fetchTopicList(){
        MutableLiveData<List<Topic>> mutableLiveData = new MutableLiveData<>();

        Type type = new TypeToken<List<Topic>>(){}.getType();
        JsonApiCaller<List<Topic>> jsonApiCaller = new JsonApiCaller<>(type,BASE_URL);
        jsonApiCaller.GET("posts")
                .addOnFinishListener(new OnFinishListener<List<Topic>>() {
                    @Override
                    public void onSuccess(List<Topic> topics) {
                        Log.d(TAG,"Success");
                        Log.d(TAG,"Total topic: "+topics.size());
                        if(topics.size()>0){
                            Log.d(TAG,"fetchTopicList()->"+topics.get(topics.size()-1).toString());
                        }
                        mutableLiveData.postValue(topics);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG,"fetchTopicList()->"+"Failed");
                    }
                });
        return mutableLiveData;
    }
}
