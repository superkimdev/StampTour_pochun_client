package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;

import java.util.ArrayList;

/**
 * Created by kjw on 16. 8. 25..
 */
public class LoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = "LoadAsyncTask";
    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private MainRecyclerAdapter madapter;
    private Context context;
    private RecyclerView recyclerView;
    private LocationEvent locationEvent;
    private String NONLOCATION = "찾지못함";
    private float distance;

    public LoadAsyncTask(LocationEvent locationEvent,MainRecyclerAdapter madapter, Context context) {
        this.context = context;
        this.madapter = madapter;
        this.locationEvent=locationEvent;
        readJson = new ReadJson(context);
    }

    public LoadAsyncTask(RecyclerView recyclerView, Context context) {
        this.context = context;
        readJson = new ReadJson(context);
        this.recyclerView = recyclerView;
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
        madapter.removelist();
        if(locationEvent == null){
            for(int i=0 ;i<list.size();i++){
                TownJson data = list.get(i);
                String region = "광산구";
                if(data.getRegion() == "")region = "북구";
                madapter.additem(new TownDTO(data.getNo(),data.getName(),region,NONLOCATION,data.getRange()));
            }
        }else{
            for(int i=0 ;i<list.size();i++){
                TownJson data = list.get(i);
                String region = "광산구";
                if(data.getRegion() == "")region = "북구";

                Location townlocation = new Location("townLocation");
                townlocation.setLatitude(Double.parseDouble(list.get(i).getLat()));
                townlocation.setLongitude(Double.parseDouble(list.get(i).getLon()));
                distance = locationEvent.getLocation().distanceTo(townlocation);
                if(distance <= Float.parseFloat(list.get(i).getRange())){
                    Log.e(TAG,"STAMPON"+list.get(i).getName());
                }else{
                    Log.e(TAG,"STAMPOFF"+list.get(i).getName());
                }
                madapter.additem(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange()));
            }
        }
        madapter.notifyDataSetChanged();
    }
}
