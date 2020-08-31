package com.example.mqttreberrypie;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;

    private EditText textMessage, subscribeTopic, unSubscribeTopic;
    private Button XMYP,Z;
    private ListView mVideosListView;
    private List<Video> mVideosList = new ArrayList<>();
    private VideoAdapter mVideoAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pahoMqttClient = new PahoMqttClient();
        XMYP=findViewById(R.id.XmYp);
       Z=findViewById(R.id.z);
        mVideosListView = (ListView) findViewById(R.id.videoListView);

        //create videos
        Video projectvideo = new Video("https://s3.amazonaws.com/androidvideostutorial/862009639.mp4");
        mVideosList.add(projectvideo);
        mVideoAdapter = new VideoAdapter(this, mVideosList);
        mVideosListView.setAdapter(mVideoAdapter);






        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);


     XMYP.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View view, MotionEvent motionEvent) {
             if(motionEvent.getAction() == MotionEvent.ACTION_BUTTON_PRESS){
                 try {
                     pahoMqttClient.publishMessage(client, " (\"motor\", payload='go(xaxis,-1)', hostname=\"192.168.1.3\")", 1, Constants.PUBLISH_TOPIC);
                     pahoMqttClient.publishMessage(client, "  (\"motor\", payload='go(yaxis,1)', hostname=\"192.168.1.3\")", 1, Constants.PUBLISH_TOPIC);
                 } catch (MqttException e) {
                     e.printStackTrace();
                 } catch (UnsupportedEncodingException e) {
                     e.printStackTrace();
                 }
                 // Do what you want
                 return true;
             }
          else   if(motionEvent.getAction()==MotionEvent.ACTION_BUTTON_RELEASE)
             {
                 try {
                     pahoMqttClient.publishMessage(client, "  (\"motor\", payload='stop(xaxis)', hostname=\"192.168.1.3\")", 1, Constants.PUBLISH_TOPIC);
                     pahoMqttClient.publishMessage(client, "   (\"motor\", payload='stop(yaxis)', hostname=\"192.168.1.3\")", 1, Constants.PUBLISH_TOPIC);
                 } catch (MqttException e) {
                     e.printStackTrace();
                 } catch (UnsupportedEncodingException e) {
                     e.printStackTrace();
                 }
                 return true;
             }


             return false;
         }
     });
     Z.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View view, MotionEvent motionEvent) {
             if(motionEvent.getAction() == MotionEvent.ACTION_BUTTON_PRESS){
                 try {
                     pahoMqttClient.publishMessage(client, "   (\"motor\", payload='go(zaxis,1)', hostname=\"192.168.1.3\")", 1, Constants.PUBLISH_TOPIC);

                 } catch (MqttException e) {
                     e.printStackTrace();
                 } catch (UnsupportedEncodingException e) {
                     e.printStackTrace();
                 }
                 // Do what you want
                 return true;
             }
          else   if(motionEvent.getAction()==MotionEvent.ACTION_BUTTON_RELEASE)
             {
                 try {
                     pahoMqttClient.publishMessage(client, " (\"motor\", payload='stop(zaxis)', hostname=\"192.168.1.3\")", 1, Constants.PUBLISH_TOPIC);

                 } catch (MqttException e) {
                     e.printStackTrace();
                 } catch (UnsupportedEncodingException e) {
                     e.printStackTrace();
                 }
                 return true;
             }
             return false;
         }
     });



        Intent intent = new Intent(MainActivity.this, MqttMessageService.class);
        startService(intent);
    }
}