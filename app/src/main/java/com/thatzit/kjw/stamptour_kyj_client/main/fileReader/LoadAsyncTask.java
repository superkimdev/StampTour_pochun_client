package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;
import com.thatzit.kjw.stamptour_kyj_client.util.ChangeDistanceDoubleToStringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kjw on 16. 8. 25..
 */
public class LoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = "LoadAsyncTask";
    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private ArrayList<TownDTO> sorted_array;
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
        sorted_array = new ArrayList<TownDTO>();
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
        if(locationEvent == null){
            for(int i=0 ;i<list.size();i++){
                TownJson data = list.get(i);
                String region = "광산구";
                if(data.getRegion() == "")region = "북구";
                sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,NONLOCATION,data.getRange()));
            }
            return null;
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
                sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange()));
            }
            Collections.sort(sorted_array,distanceComparator);
            for(int i = 0; i < sorted_array.size() ; i++){
                String temp_distance = sorted_array.get(i).getDistance();
                String distance_meter_string = ChangeDistanceDoubleToStringUtil.onChangeDistanceDoubleToString(Float.parseFloat(temp_distance));
                sorted_array.get(i).setDistance(distance_meter_string);
            }
            return null;
        }
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        madapter.removelist();
        for(int i=0 ;i<sorted_array.size();i++){
            madapter.additem(sorted_array.get(i));
        }
        madapter.notifyDataSetChanged();
    }
    // comparator
    private final static Comparator<TownDTO> distanceComparator = new Comparator<TownDTO>() {
        @Override
        public int compare(TownDTO lhs, TownDTO rhs) {
            return (Float.parseFloat(lhs.getDistance()) < Float.parseFloat(rhs.getDistance())) ? -1 : (Float.parseFloat(lhs.getDistance()) > Float.parseFloat(rhs.getDistance())) ? 1 : 0;
        }
    };
}
