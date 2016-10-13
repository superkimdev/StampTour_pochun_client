package com.thatzit.kjw.stamptour_kyj_client.hide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;

/**
 * Created by kjw on 2016. 10. 12..
 */
public class HideListActivity extends AppCompatActivity implements HideRecyclerAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = "HideListActivity";
    private TextView hidelist_title_textview;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HideRecyclerAdapter hideRecyclerAdapter;
    private PreferenceManager preferenceManager;
    private static final int HIDELISTCHANGED = 7778;
    private static final int HIDELISTUNCHANGED = 7779;
    private boolean change_list=false;
    private Intent parent_intent;
    private ImageButton hidelist_toolbar_btn_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidelist);
        setLayout();
    }

    private void setLayout() {
        parent_intent = getIntent();
        hidelist_title_textview = (TextView) findViewById(R.id.hidelist_toolbar_title_textview);
        hidelist_toolbar_btn_close = (ImageButton) findViewById(R.id.hidelist_toolbar_btn_close);
        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        hideRecyclerAdapter = new HideRecyclerAdapter(this);
        recyclerView.setAdapter(hideRecyclerAdapter);
        hideRecyclerAdapter.SetOnItemClickListener(this);
        preferenceManager = new PreferenceManager(this);
        hidelist_toolbar_btn_close.setOnClickListener(this);
        setItem();
    }
    public void setItem(){
        new HideListLoadAsyncTask(this,hideRecyclerAdapter).execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e(TAG,hideRecyclerAdapter.getmListData(position).toString());
        TownDTO towndata = hideRecyclerAdapter.getmListData(position);
        preferenceManager.setTownHideStatus(towndata.getNo(),HideStatus.UNHIDE.getStatus());
        hideRecyclerAdapter.removeItem(position);
        hideRecyclerAdapter.notifyDataSetChanged();
        change_list = true;
    }

    @Override
    protected void onDestroy() {
        if(change_list){
            setResult(HIDELISTCHANGED,parent_intent);
        }else{
            setResult(HIDELISTUNCHANGED,parent_intent);
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.hidelist_toolbar_btn_close){
            if(change_list){
                setResult(HIDELISTCHANGED,parent_intent);
            }else{
                setResult(HIDELISTUNCHANGED,parent_intent);
            }
            finish();
        }
    }
}
