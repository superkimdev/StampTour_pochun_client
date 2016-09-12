package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainPageAdapter;
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

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements PushMessageChangeListener, LocationEventListener, GpsStateEventListener {
    private PreferenceManager preferenceManager;
    private boolean mBound;
    private AppCompatActivity self;
    private GpsService mService;
    private Context myapplication= MyApplication.getContext();
    private static final String TAG = "MainActivity";
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
          TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.btn_tabs_stamp_on)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.btn_tabs_map_on)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.btn_tabs_ranking_on)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.btn_tabs_more_on)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setLayoutDirection(TabLayout.LAYOUT_DIRECTION_INHERIT);
        tabLayout.setTabTextColors(getColor(R.color.cardview_dark_background),getColor(R.color.com_facebook_blue));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MainPageAdapter adapter = new MainPageAdapter
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
        preferenceManager = new PreferenceManager(this);

        pushRequest();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
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

    }

    @Override
    public void OnReceivedStateEvent(GpsStateEvent event) {
        Log.e(TAG,event.isState()+"");

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
}
