package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.main.TempTownDTO;
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
    private ArrayList<TempTownDTO> userTownInfo_arr;
    private MainRecyclerAdapter madapter;
    private Context context;
    private RecyclerView recyclerView;
    private LocationEvent locationEvent;
    private String NONLOCATION = "찾지못함";
    private int sort_mode;
    private float distance;
    private final int SORT_BY_DISTANCE = 0;
    private final int SORT_BY_NAME = 1;
    private final int SORT_BY_REGION = 2;


    public LoadAsyncTask(ArrayList<TempTownDTO> userTownInfo_arr, int sort_mode, LocationEvent locationEvent, MainRecyclerAdapter madapter, Context context) {
        this.context = context;
        this.madapter = madapter;
        this.locationEvent=locationEvent;
        this.sort_mode = sort_mode;
        this.userTownInfo_arr = userTownInfo_arr;
        readJson = new ReadJson(context);
        sorted_array = new ArrayList<TownDTO>();

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
                TempTownDTO tempTownDTO= userTownInfo_arr.get(i);
                String region = "광산구";
                if(i%2 == 0)region = "북구";
                if(i%3 == 0)region = "남구";
                sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,NONLOCATION,data.getRange(),tempTownDTO.getChecktime(),false));
            }
            sort_by_mode();
            return null;
        }else{
            for(int i=0 ;i<list.size();i++){
                TownJson data = list.get(i);
                TempTownDTO tempTownDTO= userTownInfo_arr.get(i);
                String region = "광산구";
                if(i%2 == 0)region = "북구";
                if(i%3 == 0)region = "남구";
                distance = calculate_Distance(i);
                if(distance <= Float.parseFloat(list.get(i).getRange())){
                    Log.e(TAG,"STAMPON"+list.get(i).getName());
                    sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange(),tempTownDTO.getChecktime(),true));
                }else{
                    Log.e(TAG,"STAMPOFF"+list.get(i).getName());
                    sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange(),tempTownDTO.getChecktime(),false));
                }

            }
            sort_by_mode();
            for(int i = 0; i < sorted_array.size() ; i++){
                String temp_distance = sorted_array.get(i).getDistance();
                String distance_meter_string = ChangeDistanceDoubleToStringUtil.onChangeDistanceDoubleToString(Float.parseFloat(temp_distance));
                sorted_array.get(i).setDistance(distance_meter_string);
            }
            return null;
        }
    }

    @NonNull
    private float calculate_Distance(int i) {
        Location townlocation = new Location("townLocation");
        townlocation.setLatitude(Double.parseDouble(list.get(i).getLat()));
        townlocation.setLongitude(Double.parseDouble(list.get(i).getLon()));
        locationEvent.getLocation().distanceTo(townlocation);
        return locationEvent.getLocation().distanceTo(townlocation);
    }

    private void sort_by_mode() {
        switch (sort_mode){
            case SORT_BY_DISTANCE:Collections.sort(sorted_array,distanceComparator);break;
            case SORT_BY_NAME:Collections.sort(sorted_array,nameComparator);break;
            case SORT_BY_REGION:Collections.sort(sorted_array,regionComparator);break;
            default: Collections.sort(sorted_array,distanceComparator);break;
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

    // comparator for distance
    private final static Comparator<TownDTO> distanceComparator = new Comparator<TownDTO>() {
        @Override
        public int compare(TownDTO lhs, TownDTO rhs) {
            return (Float.parseFloat(lhs.getDistance()) < Float.parseFloat(rhs.getDistance())) ? -1 : (Float.parseFloat(lhs.getDistance()) > Float.parseFloat(rhs.getDistance())) ? 1 : 0;
        }
    };
    // comparator for distance
    private final static Comparator<TownDTO> regionComparator = new Comparator<TownDTO>() {
        @Override
        public int compare(TownDTO lhs, TownDTO rhs) {
            return lhs.getRegion().compareTo(rhs.getRegion());
        }
    };
    // comparator for distance
    private final static Comparator<TownDTO> nameComparator = new Comparator<TownDTO>() {
        @Override
        public int compare(TownDTO lhs, TownDTO rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    };
}
