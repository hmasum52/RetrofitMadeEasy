package com.hmasum18.retrofitmadeeasy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.hmasum18.retrofitmadeeasy.databinding.ActivityMainBinding;
import com.hmasum18.retrofitmadeeasy.jsonplaceholder.JsonPlaceHolder;
import com.hmasum18.retrofitmadeeasy.jsonplaceholder.Topic;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity->";
    public ActivityMainBinding mVB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVB = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mVB.getRoot());
        JsonPlaceHolder jsonPlaceHolder = new JsonPlaceHolder();
        jsonPlaceHolder.fetchTopicList().observe(this,topics -> {
            Log.d(TAG,"Total topics: "+topics.size());
            StringBuilder sb = new StringBuilder();

            sb.append(mVB.responseTV.getText()).append("\n");
            sb.append("START-------------------------------------------------\n");

            sb.append("Response from /posts : ").append("\n");
            sb.append("total topics received : ").append(topics.size()).append("\n\n");
            sb.append("Logging 1st few topics : ").append("\n");
            int iter = Math.min(5, topics.size());
            for (int i = 0; i<iter;i++) {
                sb.append(topics.get(i)).append("\n");
            }
            sb.append("END-------------------------------------------------\n");

            mVB.responseTV.setText(sb.toString());
        });
        jsonPlaceHolder.fetchTopic().observe(this, topic -> {
           // Log.d(TAG,topic.toString());
            String response = "START-------------------------------------------------\n"+
                    mVB.responseTV.getText() + "\n" +
                    "Response from /posts/1 : " + "\n" +
                    topic +"\n"+
                    "END-------------------------------------------------\n" ;
            mVB.responseTV.setText(response);
        });
    }
}