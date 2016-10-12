package com.thatzit.kjw.stamptour_kyj_client.push.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageChangeListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageEvent;

import java.util.Map;

/**
 * Created by kjw on 16. 9. 3..
 */
public class MyFcmListenerService extends FirebaseMessagingService {
    private PushMessageChangeListener listener;
    private final String MESSAGE_TITLE = "title";
    private final String MESSAGE_LECTURE_ID = "lecture_id";
    private final String MESSAGE_DESC = "desc";
    private final String MESSAGE_PARAM1 = "param1";
    private final String MESSAGE_PARAM2 = "param2";
    private final int NOTIFICATION_ID = 12345;
    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();
        Log.e("onMessageReceived", "FROM : " + from + " |||| TITLE : " + data.get("title").toString());
        String lecture_id = data.get(MESSAGE_LECTURE_ID).toString();
        String title = data.get(MESSAGE_TITLE).toString();
        String desc = data.get(MESSAGE_DESC).toString();
        String param1 = data.get(MESSAGE_PARAM1).toString();
        String param2 = data.get(MESSAGE_PARAM2).toString();
        PushMessageEvent pushMessageEvent = new PushMessageEvent(lecture_id,title,desc,param1,param2);
        //sendNotification(pushMessageEvent);

    }
    private void sendNotification(PushMessageEvent pushMessageEvent) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.btn_tabs_stamp_on)
                        .setContentTitle(pushMessageEvent.getTitle())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(pushMessageEvent.getDesc()))
                        .setContentText("스탬프를 모아보세요");

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}