package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.hide.HideStatus;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.main.TempTownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;
import com.thatzit.kjw.stamptour_kyj_client.util.ChangeDistanceDoubleToStringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 8. 25..
 */
public class LoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = "LoadAsyncTask";
    private final TextView sort_mode_textview;
    private final TextView firstline_text_view;
    private final TextView secondline_cnt_text_view;
    private final TextView secondline_nextcnt_text_view;

    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private ArrayList<TownDTO> sorted_array;
    private ArrayList<TempTownDTO> userTownInfo_arr;
    private ArrayList<Integer> hidestatue_arr;
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
    private String mode_title;
    private String nick;
    private String grade;
    private String zosa;
    private String last_string;
    private PreferenceManager preferenceManager;
    private String current_req_url;
    private String accesstoken;

    public LoadAsyncTask(TextView firstline_text_view, TextView secondline_cnt_text_view, TextView secondline_nextcnt_text_view, TextView sort_mode_textview, ArrayList<TempTownDTO> userTownInfo_arr, int sort_mode, LocationEvent locationEvent, MainRecyclerAdapter madapter, Context context) {
        this.context = context;
        this.madapter = madapter;
        this.locationEvent=locationEvent;
        this.sort_mode = sort_mode;
        this.userTownInfo_arr = userTownInfo_arr;
        this.sort_mode_textview = sort_mode_textview;
        this.firstline_text_view = firstline_text_view;
        this.secondline_cnt_text_view = secondline_cnt_text_view;
        this.secondline_nextcnt_text_view = secondline_nextcnt_text_view;
        this.current_req_url = StampRestClient.BASE_URL+context.getString(R.string.req_url_current_stamp);
        readJson = new ReadJson(context);
        preferenceManager = new PreferenceManager(context);
        sorted_array = new ArrayList<TownDTO>();

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        list = readJson.ReadFile();
        hidestatue_arr = new ArrayList<Integer>();
        if(preferenceManager.getTownHideStatus(list.get(0).getNo())==HideStatus.UNSET.getStatus()){
            for(int i = 0 ; i < list.size() ;i++){
                TownJson data = list.get(i);
                hidestatue_arr.add(preferenceManager.setTownHideStatus(data.getNo(), HideStatus.UNHIDE.getStatus()));
                preferenceManager.setTownHideStatus(data.getNo(),hidestatue_arr.get(i));
            }
        }else{
            for(int i = 0 ; i < list.size() ;i++){
                TownJson data = list.get(i);
                hidestatue_arr.add(preferenceManager.getTownHideStatus(data.getNo()));
            }
        }
        nick = preferenceManager.getLoggedIn_Info().getNick();
        accesstoken = preferenceManager.getLoggedIn_Info().getAccesstoken();
        if(locationEvent == null){
            for(int i=0 ;i<list.size();i++){
                TownJson data = list.get(i);
                TempTownDTO tempTownDTO= userTownInfo_arr.get(i);
                String region = "";
                if(data.getRegion().equals("0")){
                    region = "";
                }else{
                    region = data.getRegion();
                }
                if(hidestatue_arr.size()!=0){
                    if(hidestatue_arr.get(i)==HideStatus.UNHIDE.getStatus()){
                        sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,NONLOCATION,data.getRange(),tempTownDTO.getChecktime(),tempTownDTO.getRank_no(),false));
                    }
                }else{
                    sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,NONLOCATION,data.getRange(),tempTownDTO.getChecktime(),tempTownDTO.getRank_no(),false));
                }

            }
            sort_by_mode();
            return null;
        }else{
            for(int i=0 ;i<list.size();i++){
                TownJson data = list.get(i);
                TempTownDTO tempTownDTO= userTownInfo_arr.get(i);
                String region = "";
                if(data.getRegion().equals("0")){
                    region = "";
                }else{
                    region = data.getRegion();
                }
                distance = calculate_Distance(i);
                if(distance <= Float.parseFloat(list.get(i).getRange())){
               // if(distance <= 3558000){
                    Log.e(TAG,"STAMPON"+list.get(i).getName());
                    if(hidestatue_arr.size()!=0){
                        if(hidestatue_arr.get(i)==HideStatus.UNHIDE.getStatus()){
                            sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange(),tempTownDTO.getChecktime(),tempTownDTO.getRank_no(),true));
                        }
                    }else{
                        sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange(),tempTownDTO.getChecktime(),tempTownDTO.getRank_no(),true));
                    }

                }else{
                    Log.e(TAG,"STAMPOFF"+list.get(i).getName());
                    if(hidestatue_arr.size()!=0){
                        if(hidestatue_arr.get(i)==HideStatus.UNHIDE.getStatus()){
                            sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange(),tempTownDTO.getChecktime(),tempTownDTO.getRank_no(),false));
                        }
                    }else{
                        sorted_array.add(new TownDTO(data.getNo(),data.getName(),region,String.valueOf(distance),data.getRange(),tempTownDTO.getChecktime(),tempTownDTO.getRank_no(),false));
                    }

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
            case SORT_BY_DISTANCE:Collections.sort(sorted_array,distanceComparator);
                mode_title = context.getString(R.string.mode_title0);break;
            case SORT_BY_NAME:Collections.sort(sorted_array,nameComparator);
                mode_title = context.getString(R.string.mode_title1);break;
            case SORT_BY_REGION:Collections.sort(sorted_array,regionComparator);
                mode_title = context.getString(R.string.mode_title2);break;
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
//        String space = " ";
//        String firstline = nick+zosa+space+grade+space+last_string;
//        firstline_text_view.setText(firstline);
        sort_mode_textview.setText(mode_title+sorted_array.size());
        sort_mode_textview.setGravity(Gravity.CENTER);
        madapter.notifyDataSetChanged();
    }

    // comparator for distance
    private final static Comparator<TownDTO> distanceComparator = new Comparator<TownDTO>() {
        @Override
        public int compare(TownDTO lhs, TownDTO rhs) {
            if(lhs.getDistance().equals("찾지못함")){
                return lhs.getName().compareTo(rhs.getName());
            }
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
