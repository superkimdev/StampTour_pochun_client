package com.thatzit.kjw.stamptour_kyj_client.hide;

import android.content.Context;
import android.os.AsyncTask;

import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.TempTownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by kjw on 2016. 10. 12..
 */

public class HideListLoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final ArrayList<TownJson> townlist;
    private final ArrayList<TempTownDTO> usertowninfo_arr;
    private Context context;
    private PreferenceManager preferenceManager;
    private HideRecyclerAdapter hideRecyclerAdapter;

    public HideListLoadAsyncTask(Context context,HideRecyclerAdapter hideRecyclerAdapter) {
        this.context = context;
        townlist = ReadJson.memCashList;
        usertowninfo_arr = MainActivity.UserTownInfo_arr;
        this.hideRecyclerAdapter = hideRecyclerAdapter;
        preferenceManager = new PreferenceManager(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        hideRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(int i =0 ; i < townlist.size() ;i++){
            TownJson data = townlist.get(i);
            TempTownDTO userdata = usertowninfo_arr.get(i);
            int status = preferenceManager.getTownHideStatus(data.getNo());
            String region;
            if(data.getRegion().equals("-1")){
                region = "";
            }else{
                region = userdata.getRegion();
            }
            if(status == HideStatus.HIDE.getStatus()){

                hideRecyclerAdapter.additem(new TownDTO(data.getNo(),data.getName(),region,"",data.getRange(),userdata.getChecktime(),userdata.getRank_no(),false));
            }
        }
        return null;
    }
}
