package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainPageAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.event.ListChangeEvent;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.LoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.PreLoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ListChangeListener;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentGpsStateListener;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ParentLocationListener;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.GpsService;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.GpsStateEventListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.LocationEventListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageChangeListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.GpsService.MyLocalBinder;
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements PushMessageChangeListener, LocationEventListener, GpsStateEventListener {
    private PreferenceManager preferenceManager;
    private boolean mBound;
    private AppCompatActivity self;
    private GpsService mService;
    private Context myapplication= MyApplication.getContext();
    private static final String TAG = "MainActivity";
    private ParentLocationListener parentLocationListener;
    private ParentGpsStateListener parentGpsStateListener;
    private LoggedInInfo user;
    private ProgressWaitDaialog progressWaitDaialog;
    public static ArrayList<TempTownDTO> UserTownInfo_arr;
    private boolean req_flag;
    private ViewPager viewPager;
    private MainPageAdapter adapter;
    private TabLayout tabLayout;
    private ListChangeListener listener;

    private final int USERINFOCHANGED = 1001;
    private static final int HIDELISTCHANGED = 7778;
    private static final int HIDELISTUNCHANGED = 7779;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.thatzit.kjw.stamptour_kyj_client",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash NameNotFound:",e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash Nosuch :",e.toString());
        }
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setTabIcon(tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setLayoutDirection(TabLayout.LAYOUT_DIRECTION_INHERIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.setTabTextColors(getColor(R.color.cardview_dark_background),getColor(R.color.com_facebook_blue));
        }else{
            tabLayout.setTabTextColors(getResources().getColor(R.color.cardview_dark_background),getResources().getColor(R.color.com_facebook_blue));
        }

        preferenceManager = new PreferenceManager(this);
        //Fragment에서 사용할 데이터 서버쪽에서 받을 데이터들 로딩후 페이지셋팅
        new PreLoadAsyncTask(this).execute();
        progressWaitDaialog = new ProgressWaitDaialog(this);
        request_TownUserInfo();
//        pushRequest();

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
                        array.add(new TempTownDTO(town.getString("TOWN_CODE"),town.getString("Nick"),town.getString("CheckTime"),town.getString("region"),town.getString("rank_no")));
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
                        viewPager = (ViewPager) findViewById(R.id.pager);
                        adapter = new MainPageAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());
                        viewPager.setAdapter(adapter);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                viewPager.setCurrentItem(tab.getPosition());
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });
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
    private void setTabIcon(TabLayout tabLayout) {
        View view1 = getLayoutInflater().inflate(R.layout.tabiconview, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.btn_tabs_stamp);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        View view2 = getLayoutInflater().inflate(R.layout.tabiconview, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.btn_tabs_map);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        View view3 = getLayoutInflater().inflate(R.layout.tabiconview, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.btn_tabs_ranking);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        View view4 = getLayoutInflater().inflate(R.layout.tabiconview, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.btn_tabs_more);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
    }


    private void pushRequest() {
        LoggedInInfo loggedIn_Info = preferenceManager.getLoggedIn_Info();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.TOKEN.getKey(),loggedIn_Info.getAccesstoken());
        params.put(ResponseKey.NICK.getKey(),loggedIn_Info.getNick());
        params.put(ResponseKey.DEVICETOKEN.getKey(),preferenceManager.getGCMaccesstoken());
        StampRestClient.post(getResources().getString(R.string.req_url_push_test), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("pushRequest",response+"");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("pushRequest",errorResponse+"");
            }
        });
    }



    @Override
    public void OnReceived(PushMessageEvent event) {

    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.d("servicebinding", "come0");
            MyLocalBinder binder = (MyLocalBinder)service;
            mService = binder.getService();
            if(mService==null)Log.d(TAG, "null");
            else Log.d(TAG, "not null");
            mBound = true;
            mService.setOnLocationEventListener(MainActivity.this);
            mService.setOnGpsStateEventListener(MainActivity.this);
        }
    };

    @Override
    public void OnReceivedEvent(LocationEvent event) {
        Log.e(TAG,event.getLocation().getLatitude()+":"+event.getLocation().getLongitude());
        if(UserTownInfo_arr != null)
        if(parentLocationListener != null)parentLocationListener.OnReceivedLocation(event);
    }

    @Override
    public void OnReceivedStateEvent(GpsStateEvent event) {
        Log.e(TAG,event.isState()+"");
        if(UserTownInfo_arr != null)
        if(parentGpsStateListener != null)parentGpsStateListener.OnReceivedParentStateEvent(event);
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        bindService(new Intent(this, GpsService.class), mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if(mBound == true && mService != null)
        {
            mService.setOnGpsStateEventListener(null);
            mService.setOnLocationEventListener(null);
            Log.d(TAG, "unbindService");
            unbindService(mConnection);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void setParentLocationListener(ParentLocationListener listener){
        this.parentLocationListener = listener;
    }
    public void setParentGpsStateListener(ParentGpsStateListener listener){
        this.parentGpsStateListener = listener;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"RequestCode : "+requestCode+"\nResultCode : "+resultCode);
        if(resultCode == USERINFOCHANGED)
        {
            Log.e(TAG,"USERINFOCHANGED");
            finish();
        }else if(resultCode == HIDELISTCHANGED){
            if(listener!=null)listener.OnRecivedChangeList(new ListChangeEvent(true));
        }else if(resultCode == HIDELISTUNCHANGED){
            if(listener!=null)listener.OnRecivedChangeList(new ListChangeEvent(false));
        }
    }
    public void setOnListChangeListener(ListChangeListener listener){
        this.listener = listener;
    }
}
