package com.thatzit.kjw.stamptour_kyj_client.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.RequestPath;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.login.FindIdActivity;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GiftManageActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView gift_manage_recyclerview;
    private ImageButton gift_manage_toolbar_back;
    private ImageButton gift_manage_toolbar_done;
    private ProgressWaitDaialog progressWaitDaialog;
    private String TAG = "GiftManageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_manage);
        setLayout();
        setInitData();
    }

    private void setInitData() {
        progressWaitDaialog = new ProgressWaitDaialog(this);
        LoggedInInfo info = new PreferenceManager(this).getLoggedIn_Info();
        String nick = info.getNick();
        String token = info.getAccesstoken();
        request_gift(nick,token);
    }

    private void setLayout() {
        gift_manage_toolbar_back = (ImageButton) findViewById(R.id.gift_manage_toolbar_back);
        gift_manage_toolbar_done = (ImageButton) findViewById(R.id.gift_manage_toolbar_done);
        gift_manage_recyclerview = (RecyclerView) findViewById(R.id.gift_manage_recyclerview);


        gift_manage_toolbar_back.setOnClickListener(this);
        gift_manage_toolbar_done.setOnClickListener(this);
        setAdapter(gift_manage_recyclerview);

    }

    private void setAdapter(RecyclerView recyclerView){
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter Adapter;

        RecyclerView.LayoutManager layoutManager;
        // Item 리스트에 아이템 객체 넣기
        ArrayList<GiftDTO> items = new ArrayList<>();

        items.add(new GiftDTO("성골 선물","선물받기까지 10개 스탬프 남았습니다", "0"));
        items.add(new GiftDTO("진골 선물","눌러서 선물을 신청하세요", "1"));
        items.add(new GiftDTO("백골 선물","선물 신청 완료", "2"));


        layoutManager = new LinearLayoutManager(this);
        //layoutManager = new GridLayoutManager(this,3);

        // 지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
        recyclerView.setLayoutManager(layoutManager);

        Adapter = new GiftRecyclerViewAdapter(items,this);
        recyclerView.setAdapter(Adapter);
    }

    private void request_gift(String nick, String token) {
        String path = RequestPath.req_url_stamp_gift.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),nick);
        params.put(ResponseKey.TOKEN.getKey(),token);
        progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG,response.toString());
                progressWaitDaialog.dismiss();
                try {
                    String result_msg = response.getString(ResponseKey.MESSAGE.getKey());
                    String result_code = response.getString(ResponseKey.CODE.getKey());
                    String result_data = response.getString(ResponseKey.RESULTDATA.getKey());

                    if(result_code.equals(ResponseCode.SUCCESS.getCode())&&result_msg.equals(ResponseMsg.SUCCESS.getMessage())){




                    }else{
                       // Toast.makeText(GiftManageActivity.this,getResources().getString(R.string.find_id_not_text),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GiftManageActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(GiftManageActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gift_manage_toolbar_back:
                finish();
                break;
        }
    }
}
