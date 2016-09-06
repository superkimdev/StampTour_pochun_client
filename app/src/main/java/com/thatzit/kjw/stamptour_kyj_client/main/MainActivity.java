package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.MyFcmListenerService;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageChangeListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.PushMessageEvent;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements PushMessageChangeListener{
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        tabLayout.addTab(tabLayout.newTab().setText("스탬프"));
        tabLayout.addTab(tabLayout.newTab().setText("지도"));
        tabLayout.addTab(tabLayout.newTab().setText("랭킹"));
        tabLayout.addTab(tabLayout.newTab().setText("더보기"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnReceived(PushMessageEvent event) {

    }
}
