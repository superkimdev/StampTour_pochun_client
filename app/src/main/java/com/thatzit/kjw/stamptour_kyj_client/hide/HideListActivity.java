package com.thatzit.kjw.stamptour_kyj_client.hide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;

/**
 * Created by kjw on 2016. 10. 12..
 */
public class HideListActivity extends AppCompatActivity implements MainRecyclerAdapter.OnItemClickListener, MainRecyclerAdapter.OnItemLongClickListener {

    private TextView hidelist_title_textview;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HideRecyclerAdapter hideRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidelist);
        setLayout();
    }

    private void setLayout() {
        hidelist_title_textview = (TextView) findViewById(R.id.hidelist_toolbar_title_textview);
        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        hideRecyclerAdapter = new HideRecyclerAdapter(this);
        recyclerView.setAdapter(hideRecyclerAdapter);
        hideRecyclerAdapter.SetOnItemClickListener(this);
        hideRecyclerAdapter.SetOnItemLongClickListener(this);


    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
