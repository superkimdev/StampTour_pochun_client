package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.PopUpAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.LoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentGpsStateListener;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentLocationListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;

import java.util.ArrayList;

public class MainFragment extends Fragment implements MainRecyclerAdapter.OnItemClickListener, View.OnClickListener, MainRecyclerAdapter.OnItemLongClickListener, ParentLocationListener, ParentGpsStateListener, PopupMenu.OnMenuItemClickListener {
    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;
    int mutedColor = R.attr.colorPrimary;
    MainRecyclerAdapter mainRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View view;
    private LocationEvent currentLocation;
    private final String TAG = "MainFragment";
    private static boolean turnOff=true;
    private TextView sort_btn;
    private TextView hide_btn;
    private int sort_mode;
    private int setting_flag = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
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

        sort_btn = (TextView) view.findViewById(R.id.sort_btn);
        hide_btn = (TextView) view.findViewById(R.id.hide_btn);
        sort_btn.setOnClickListener(this);
        hide_btn.setOnClickListener(this);

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

        sort_load_before_check();

    }

    private void popUpShow() {



        //normal popup
        PopupMenu popup = new PopupMenu(getActivity(), sort_btn);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();

        //custom popup
        //custom_PopUpShow();
    }

    private void custom_PopUpShow() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(
                getActivity());
        ArrayList<String> arGeneral = new ArrayList<String>();
        arGeneral.add("거리");
        arGeneral.add("권역");
        arGeneral.add("이름");

        PopUpAdapter popUpAdapter = new PopUpAdapter(arGeneral, R.layout.popup_item,getActivity());
        listPopupWindow.setAdapter(popUpAdapter);
        listPopupWindow.setAnchorView(sort_btn);
        listPopupWindow.setWidth((int) getResources().getDimension(R.dimen.popup_width));
        listPopupWindow.setHeight((int) getResources().getDimension(R.dimen.popup_height));

        listPopupWindow.setModal(true);
        //listPopupWindow.setOnItemClickListener(this);

        listPopupWindow.show();
    }

    @Override
    public void onItemClick(View view, int position) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header:
                linearLayoutManager.scrollToPositionWithOffset(7, collapsingToolbar.getBottom());
                Toast.makeText(getContext(),"이미지클릭",Toast.LENGTH_LONG).show();
                break;
            case R.id.hide_btn:
                Toast.makeText(getContext(),"숨김관리버튼클릭",Toast.LENGTH_LONG).show();
                break;
            case R.id.sort_btn:
                popUpShow();
                Toast.makeText(getContext(),"정렬버튼클릭",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.e("RecycleitemLong","position = "+position);

        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("town",position+1);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void OnReceivedLocation(LocationEvent locationEvent) {
        Log.e(TAG,locationEvent.getLocation().getLatitude()+":"+locationEvent.getLocation().getLongitude());
        currentLocation = locationEvent;
        new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();

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
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
            }

        }else{
            turnOff = event.isState();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_distance:
                Toast.makeText(getContext(),"거리클릭",Toast.LENGTH_LONG).show();
                sort_load_before_check();
                break;
            case R.id.action_sort_name:
                Toast.makeText(getContext(),"이름클릭",Toast.LENGTH_LONG).show();
                sort_mode = 1;
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
                break;
            case R.id.action_sort_region:
                Toast.makeText(getContext(),"권역클릭",Toast.LENGTH_LONG).show();
                sort_mode = 2;
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
                break;
            default:
                return false;
        }
        return false;
    }

    private void sort_load_before_check() {
        if(setting_flag == 0){
            //처음 화면 세팅작업중에는 토스트 안띄우고 일단은 데이터 띄워주기 위해서 setting_flag 값에 따라 분기시킴
            if(currentLocation == null) {
                //gps 안켜지거나 못잡으면 0번은 안됨 기본 이름으로
                sort_mode = 1;
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
            }else{
                sort_mode = 0;
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
            }
            setting_flag=1;
        }else{
            if(currentLocation == null){
                Toast.makeText(getContext(),"GPS켜주세요",Toast.LENGTH_LONG).show();
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
            }else{
                sort_mode = 0;
                new LoadAsyncTask(sort_mode,currentLocation,mainRecyclerAdapter,getActivity()).execute();
            }
        }

    }

}
