package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.ListViewAdapter;

import java.util.ArrayList;

/**
 * Created by kjw on 16. 8. 25..
 */
public class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private ListViewAdapter madapter;
    private Context context;
    public LoadAsyncTask(ListViewAdapter madapter, Context context) {
        this.context = context;
        readJson = new ReadJson(context);
        this.madapter = madapter;
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
        for(int i=0 ;i<list.size();i++){
            TownJson data = list.get(i);
            String region = "광산구";
            if(data.getRegion() == "")region = "북구";
            madapter.additem(new TownDTO(data.getName(),region,data.getRange(),data.getRange()));
        }

        madapter.notifyDataSetChanged();
    }
}
