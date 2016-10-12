package com.thatzit.kjw.stamptour_kyj_client.push.service.fileReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageEvent;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 8. 25..
 */
public class InServiceLoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "InServiceLoadAsyncTask";
    private static final int NOTIFICATION_ID = 12345;
    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private Context context;
    private Location location;
    private PreferenceManager preferenceManager;
    private NotificationManager mNotificationManager;

    public InServiceLoadAsyncTask(Location location,Context context) {
        this.context = context;
        this.location = location;
        this.preferenceManager = new PreferenceManager(context);
        readJson = new ReadJson(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
        list=readJson.ReadFile();
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ArrayList<TownJson> townarr = ReadJson.memCashList;
        for(int i =0 ; i < townarr.size();i++){
            Location townlocation = new Location("townLocation");
            townlocation.setLatitude(Double.parseDouble(townarr.get(i).getLat()));
            townlocation.setLongitude(Double.parseDouble(townarr.get(i).getLon()));
            float distance = location.distanceTo(townlocation);
            if(distance <= Float.parseFloat(townarr.get(i).getRange())){
                Log.e(TAG,"STAMPON"+townarr.get(i).getName());
                if(preferenceManager.getLoggedIn_Info().getAccesstoken()!=""){
                    sendNotification(townarr.get(i));
                }else{
                    return;
                }
            }else{
                Log.e(TAG,"STAMPOFF"+townarr.get(i).getName());
            }
        }
    }
    private void sendNotification(TownJson town) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.btn_tabs_stamp_on)
                        .setContentTitle(town.getName())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(town.getSubtitle()))
                        .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                        .setContentText(context.getResources().getString(R.string.stamp_zone_come_message));

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
