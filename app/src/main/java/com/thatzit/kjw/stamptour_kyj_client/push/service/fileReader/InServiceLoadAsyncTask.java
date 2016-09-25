package com.thatzit.kjw.stamptour_kyj_client.push.service.fileReader;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 8. 25..
 */
public class InServiceLoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "InServiceLoadAsyncTask";
    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private Context context;
    private Location location;
    private PreferenceManager preferenceManager;
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
                    String req_url = context.getString(R.string.req_url_push_test);
                    RequestParams params = new RequestParams();
                    params.put(ResponseKey.NICK.getKey(),preferenceManager.getLoggedIn_Info().getNick());
                    params.put(ResponseKey.TOKEN.getKey(),preferenceManager.getLoggedIn_Info().getAccesstoken());
                    params.put(ResponseKey.DEVICETOKEN.getKey(),preferenceManager.getGCMaccesstoken());
                    StampRestClient.post(req_url,params,new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.e("Backreq_push","success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("Backreq_push","Fail");
                        }
                    });
                }else{
                    return;
                }

            }else{
                Log.e(TAG,"STAMPOFF"+townarr.get(i).getName());
            }
        }
    }
}
