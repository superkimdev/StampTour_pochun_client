package com.thatzit.kjw.stamptour_kyj_client.main;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.LoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentGpsStateListener;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentLocationListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;

public class AnimateToolbar extends Fragment implements MainRecyclerAdapter.OnItemClickListener, View.OnClickListener, MainRecyclerAdapter.OnItemLongClickListener, ParentLocationListener, ParentGpsStateListener {
    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;
    int mutedColor = R.attr.colorPrimary;
    MainRecyclerAdapter mainRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View view;
    private LocationEvent currentLocation;
    private final String TAG = "AnimateToolbar";
    private static boolean turnOff=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_animate_toolbar, container, false);
        setLayout();
        return view;
    }
    private void setLayout(){
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");


        ImageView header = (ImageView) view.findViewById(R.id.header);
        header.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerAdapter = new MainRecyclerAdapter(view.getContext());
        recyclerView.setAdapter(mainRecyclerAdapter);
        mainRecyclerAdapter.SetOnItemClickListener(this);
        mainRecyclerAdapter.SetOnItemLongClickListener(this);
        ((MainActivity)getActivity()).setParentLocationListener(this);
        ((MainActivity)getActivity()).setParentGpsStateListener(this);
        if(currentLocation == null) new LoadAsyncTask(currentLocation,mainRecyclerAdapter,getActivity()).execute();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                ((AppCompatActivity)getActivity()).finish();
//                return true;
//            case R.id.action_settings:
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e("Recycleitem","position = "+position);
        Log.e("Recycleitem","포커싱여부1 = "+view.isFocusable());
        view.setFocusable(true); // 포커스 여부
        Log.e("Recycleitem","포커싱여부2 = "+view.isFocusable());
        view.setBackground(getResources().getDrawable(R.drawable.town_list_item_bg));
        view.invalidate();


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.header){
            linearLayoutManager.scrollToPositionWithOffset(10, 20);
            Toast.makeText(getContext(),"이미지클릭",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.e("RecycleitemLong","position = "+position);
    }

    @Override
    public void OnReceivedLocation(LocationEvent locationEvent) {
        Log.e(TAG,locationEvent.getLocation().getLatitude()+":"+locationEvent.getLocation().getLongitude());
        currentLocation = locationEvent;
        new LoadAsyncTask(currentLocation,mainRecyclerAdapter,getActivity()).execute();
    }

    @Override
    public void OnReceivedParentStateEvent(GpsStateEvent event) {
        if(event.isState() == false){
            if(turnOff == false)
            {
                return;
            }else{
                turnOff = event.isState();
                currentLocation = null;
                new LoadAsyncTask(currentLocation,mainRecyclerAdapter,getActivity()).execute();
            }

        }else{
            turnOff = event.isState();
        }
    }
}
