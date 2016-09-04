package com.thatzit.kjw.stamptour_kyj_client.push.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by kjw on 16. 9. 3..
 */
public class MyFcmListenerService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        Map data = message.getData();
        Log.e("onMessageReceived",data.get("title").toString());
    }

}