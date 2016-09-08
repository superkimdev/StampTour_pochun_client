package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.SimpleRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.ListViewAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kjw on 16. 8. 25..
 */
public class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

    private ReadJson readJson;
    private ArrayList<TownJson> list;
    private SimpleRecyclerAdapter madapter;
    private Context context;
    private RecyclerView recyclerView;

    public LoadAsyncTask(SimpleRecyclerAdapter madapter,Context context) {
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
            madapter.additem(new TownDTO(data.getName(),region,data.getRange(),data.getRange()));
        }
        madapter.notifyDataSetChanged();
    }
}
