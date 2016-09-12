package com.thatzit.kjw.stamptour_kyj_client.push.service.fileReader;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;

import java.util.ArrayList;

/**
 * Created by kjw on 16. 8. 25..
 */
public class InServiceLoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "InServiceLoadAsyncTask";
    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private Context context;
    private Location location;
    public InServiceLoadAsyncTask(Location location,Context context) {
        this.context = context;
        this.location = location;
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
            }else{
                Log.e(TAG,"STAMPOFF"+townarr.get(i).getName());
            }
        }
    }
}
