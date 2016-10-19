package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.checker.ExternalMemoryDTO;
import com.thatzit.kjw.stamptour_kyj_client.checker.UsableStorageChecker;
import com.thatzit.kjw.stamptour_kyj_client.hide.HideListActivity;
import com.thatzit.kjw.stamptour_kyj_client.hide.HideStatus;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.PopUpAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.event.ListChangeEvent;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.LoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ListChangeListener;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentGpsStateListener;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentLocationListener;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainFragment extends Fragment implements MainRecyclerAdapter.OnItemClickListener, View.OnClickListener,
        MainRecyclerAdapter.OnItemLongClickListener, ParentLocationListener, ParentGpsStateListener,
        PopupMenu.OnMenuItemClickListener, ListChangeListener {

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
    private TextView sort_mode_textview;

    private TextView firstline_text_view;
    private TextView secondline_cnt_text_view;
    private TextView secondline_nextcnt_text_view;

    private int sort_mode;
    private int setting_flag = 0;
    private String current_req_url;
    private String accesstoken;
    private View progressbar;
    private PreferenceManager preferenceManager;
    private ProgressWaitDaialog progressWaitDaialog;
    private LoggedInInfo user;
    private boolean req_flag;
    private ArrayList<TempTownDTO> UserTownInfo_arr;
    private ImageView header;
    private String nick;
    private String grade;
    private String zosa;
    private String last_string;

    private static final int HIDEACTIVITYSTART = 7777;
    private static final int HIDELISTCHANGED = 7778;
    private static final int HIDELISTUNCHANGED = 7779;
    private MainActivity parent;
    private boolean turnOnGpsShow = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        preferenceManager = new PreferenceManager(getActivity());
        progressWaitDaialog = new ProgressWaitDaialog(view.getContext());
        req_flag = false;
        UserTownInfo_arr = ((MainActivity)getActivity()).UserTownInfo_arr;
        setLayout();
        return view;
    }
    private void setLayout(){
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");


        header = (ImageView) view.findViewById(R.id.header);
        header.setOnClickListener(this);

        firstline_text_view = (TextView)view.findViewById(R.id.firstline_text_view);
        secondline_cnt_text_view = (TextView)view.findViewById(R.id.secondline_cnt_text_view);
        secondline_nextcnt_text_view = (TextView)view.findViewById(R.id.secondline_nextcnt_text_view);

        sort_btn = (TextView) view.findViewById(R.id.sort_btn);
        hide_btn = (TextView) view.findViewById(R.id.hide_btn);
        sort_mode_textview = (TextView) view.findViewById(R.id.sort_mode_textview);
        sort_btn.setOnClickListener(this);
        hide_btn.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerAdapter = new MainRecyclerAdapter(view.getContext());
        recyclerView.setAdapter(mainRecyclerAdapter);
        parent = (MainActivity) getActivity();
        parent.setOnListChangeListener(this);
        mainRecyclerAdapter.SetOnItemClickListener(this);
        mainRecyclerAdapter.SetOnItemLongClickListener(this);
        ((MainActivity)getActivity()).setParentLocationListener(this);
        ((MainActivity)getActivity()).setParentGpsStateListener(this);
        progressbar = view.findViewById(R.id.list_progressbar);
        UsableStorageChecker usableStorageChecker = new UsableStorageChecker();
        ExternalMemoryDTO check_result = usableStorageChecker.check_ext_memory();
        Log.d(TAG,check_result.toString());

        current_request();
        sort_load_before_check();

    }
    private void current_request() {
        RequestParams requestParams = new RequestParams();
        current_req_url = MyApplication.getContext().getString(R.string.req_url_current_stamp);
        nick = preferenceManager.getLoggedIn_Info().getNick();
        accesstoken = preferenceManager.getLoggedIn_Info().getAccesstoken();
        zosa = "님은";
        last_string = "등급입니다.";
        requestParams.put(ResponseKey.NICK.getKey(),nick);
        requestParams.put(ResponseKey.TOKEN.getKey(),accesstoken);
        StampRestClient.post(current_req_url,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                String code = null;
                String msg = null;
                Log.e("this request","come");
                try {
                    Log.e("this request","try");
                    code = response.getString(ResponseKey.CODE.getKey());
                    msg = response.getString(ResponseKey.MESSAGE.getKey());
                    if(code.equals(ResponseCode.SUCCESS.getCode())&&msg.equals(ResponseMsg.SUCCESS.getMessage())){
                        Log.e("this request","parse");
                        JSONObject resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                        int res_next_stamp_count = resultData.getInt("next_stamp_count");
                        int res_stamp_count = resultData.getInt("stamp_count");
                        String res_grade = resultData.getString("grade");
                        String res_nick = resultData.getString("nick");
                        nick = res_nick;
                        grade = res_grade;
                        String space = " ";
                        String firstline = nick+zosa+space+grade+space+last_string;
                        firstline_text_view.setText(firstline);
                        secondline_cnt_text_view.setText(res_stamp_count+"");
                        secondline_nextcnt_text_view.setText(res_next_stamp_count+"");
                        Log.e("this request","nick : "+res_nick+"\ngrade : "+res_grade+"\ncur_stamp : "+
                                res_stamp_count+"\nnext_stamp : "+res_next_stamp_count);

                    }else{
                        Log.e("this request else",code+":"+msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("this request","exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void request_TownUserInfo() {
        user = preferenceManager.getLoggedIn_Info();
        progressWaitDaialog.show();
        String req_url = this.getString(R.string.req_url_town_list);
        RequestParams requestParams = new RequestParams();
        requestParams.put(ResponseKey.NICK.getKey(),user.getNick());
        requestParams.put(ResponseKey.TOKEN.getKey(),user.getAccesstoken());
        StampRestClient.post(req_url,requestParams,new JsonHttpResponseHandler(){
            private ArrayList<TempTownDTO> make_TownDataList(JSONArray resultData) {
                ArrayList<TempTownDTO> array = new ArrayList<TempTownDTO>();
                JSONObject town;
                for(int i = 0 ; i < resultData.length() ; i++){
                    try {
                        town = (JSONObject) resultData.get(i);
                        array.add(new TempTownDTO(town.getString("TOWN_CODE"),town.getString("Nick"),town.getString("CheckTime")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return array;
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressWaitDaialog.dismiss();
                String code = null;
                String msg = null;

                try {
                    code = response.getString(ResponseKey.CODE.getKey());
                    msg = response.getString(ResponseKey.MESSAGE.getKey());
                    if(code.equals(ResponseCode.SUCCESS.getCode())&&msg.equals(ResponseMsg.SUCCESS.getMessage())){
                        JSONArray resultData = response.getJSONArray(ResponseKey.RESULTDATA.getKey());
                        JSONObject data = (JSONObject) resultData.get(0);
                        UserTownInfo_arr = make_TownDataList(resultData);
                        Log.e(TAG,"town_code : "+data.getString("TOWN_CODE")+"\nCheckTime : "+data.getString("CheckTime")+"\nRange : "+data.getString("valid_range"));
                        req_flag = true;
                        current_request();
                        sort_load_before_check();
                    }else{
                        Log.e(TAG,code+":"+msg);
                        JSONObject resultData = null;
                        req_flag = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    req_flag = false;
                }
            }



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressWaitDaialog.dismiss();
                req_flag = false;
            }
        });
    }

    private void popUpShow() {

        //normal popup
        PopupMenu popup = new PopupMenu(getActivity(), sort_btn);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();

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
        Log.e("RecycleitemClick","position = "+position);
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        TownDTO town = mainRecyclerAdapter.getmListData(position);
        intent.putExtra("town",Integer.parseInt(town.getNo()));
        getActivity().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header:
//                linearLayoutManager.scrollToPositionWithOffset(7, collapsingToolbar.getBottom());
//                Toast.makeText(getContext(),"이미지클릭",Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"서비스 준비중입니다",Toast.LENGTH_SHORT).show();
                break;
            case R.id.hide_btn:
//                Toast.makeText(getContext(),"숨김관리버튼클릭",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(),HideListActivity.class);
                getActivity().startActivityForResult(intent,HIDEACTIVITYSTART);
                break;
            case R.id.sort_btn:
                popUpShow();
//                Toast.makeText(getContext(),"정렬버튼클릭",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.e("RecycleitemLongClick","position = "+position);
        TownDTO data = mainRecyclerAdapter.getmListData(position);
        if(!data.isStamp_on()){
            Toast.makeText(getActivity(),data.getName()+getResources().getString(R.string.hide_notfication_text),Toast.LENGTH_LONG).show();
            preferenceManager.setTownHideStatus(data.getNo(), HideStatus.HIDE.getStatus());
            sort_load_before_check();
        }
        if(currentLocation==null)return;
        if(data.isStamp_on()){
            String req_stamp_check = getString(R.string.req_url_stamp_check);
            RequestParams params = new RequestParams();
            params.put(ResponseKey.NICK.getKey(),nick);
            params.put(ResponseKey.TOKEN.getKey(),accesstoken);
            params.put("town_code",data.getNo());
            params.put("latitude",currentLocation.getLocation().getLatitude());
            params.put("longitude",currentLocation.getLocation().getLongitude());
            StampRestClient.post(req_stamp_check,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String code = null;
                    String msg = null;

                    try {
                        code = response.getString(ResponseKey.CODE.getKey());
                        msg = response.getString(ResponseKey.MESSAGE.getKey());
                        if(code.equals(ResponseCode.SUCCESS.getCode())&&msg.equals(ResponseMsg.SUCCESS.getMessage())){
                            JSONObject resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                            int res_town_code = resultData.getInt("TOWN_CODE");
                            if(res_town_code == -1){
                                Toast.makeText(getActivity(),"이미 찍으셨습니다",Toast.LENGTH_LONG).show();
                                return;
                            }
                            String res_nick = resultData.getString("Nick");
                            String res_time = resultData.getString("CheckTime");

                            Log.e("STAMP_CHECK_REQ","nick : "+res_nick+"|town : "+res_town_code+"|time : "+res_time);
                            request_TownUserInfo();

                        }else{
                            Log.e(TAG,code+":"+msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });
        }
        //스탬프 찍은후 뷰 업데이팅 할 때 호출해야함
        //request_TownUserInfo();
    }

    @Override
    public void OnReceivedLocation(LocationEvent locationEvent) {
        Log.e(TAG,locationEvent.getLocation().getLatitude()+":"+locationEvent.getLocation().getLongitude());
        currentLocation = locationEvent;
        new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview,UserTownInfo_arr,sort_mode,currentLocation,mainRecyclerAdapter, MyApplication.getContext()).execute();

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
                new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
            }

        }else{
            turnOff = event.isState();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_distance:
                //Toast.makeText(getContext(),"거리클릭",Toast.LENGTH_LONG).show();
                sort_load_before_check();
                break;
            case R.id.action_sort_name:
                // Toast.makeText(getContext(),"이름클릭",Toast.LENGTH_LONG).show();
                sort_mode = 1;
                new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
                break;
            case R.id.action_sort_region:
                // Toast.makeText(getContext(),"권역클릭",Toast.LENGTH_LONG).show();
                sort_mode = 2;
                new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
                break;
            default:
                return false;
        }
        return false;
    }

    private void sort_load_before_check() {
        ShowGpsDialog();
        if(setting_flag == 0){
            //처음 화면 세팅작업중에는 토스트 안띄우고 일단은 데이터 띄워주기 위해서 setting_flag 값에 따라 분기시킴
            if(currentLocation == null) {
                //gps 안켜지거나 못잡으면 0번은 안됨 기본 이름으로
                sort_mode = 1;
                new LoadAsyncTask(firstline_text_view,secondline_cnt_text_view,secondline_nextcnt_text_view,sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
            }else{
                sort_mode = 0;
                new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
            }
            setting_flag=1;
        }else{
            if(currentLocation == null){
                new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
            }else{
                sort_mode = 0;
                new LoadAsyncTask(firstline_text_view, secondline_cnt_text_view, secondline_nextcnt_text_view, sort_mode_textview, UserTownInfo_arr, sort_mode,currentLocation,mainRecyclerAdapter,MyApplication.getContext()).execute();
            }
        }

    }

    private void ShowGpsDialog() {
        if(turnOff == false){
            if(turnOnGpsShow){
                turnOnGpsShow = false;
                AlertDialog.Builder gsDialog = new AlertDialog.Builder(getActivity());
                gsDialog.setTitle("위치 서비스 설정");
                gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
                gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // GPS설정 화면으로 이동
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivity(intent);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
                //Toast.makeText(getContext(),"GPS켜주세요",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void OnRecivedChangeList(ListChangeEvent event) {
        if(event.isChange_status()){
            Log.e(TAG,"LIST CHANGED");
            sort_load_before_check();
        }
        else {
            return;
        }
    }
}