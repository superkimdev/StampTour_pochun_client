package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;

import java.util.ArrayList;

/**
 * Created by kjw on 16. 8. 25..
 */
public class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private MainRecyclerAdapter madapter;
    private Context context;
    private RecyclerView recyclerView;

    public LoadAsyncTask(MainRecyclerAdapter madapter, Context context) {
        this.context = context;
        this.madapter = madapter;
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
        for(int i=0 ;i<list.size();i++){
            TownJson data = list.get(i);
            String region = "광산구";
            if(data.getRegion() == "")region = "북구";
            madapter.additem(new TownDTO(data.getNo(),data.getName(),region,data.getRange(),data.getRange()));
        }

        madapter.notifyDataSetChanged();

    }
}
