package com.thatzit.kjw.stamptour_kyj_client.more;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONArray;
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
    private ArrayList<GiftListItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_manage);
        setLayout();
        //setInitData();
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

    }

    private void setAdapter(RecyclerView recyclerView,GiftVO giftVO){
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter Adapter;

        RecyclerView.LayoutManager layoutManager;
        // Item 리스트에 아이템 객체 넣기
        ArrayList<GiftListItem> items = setAdapterItem(giftVO);

        layoutManager = new LinearLayoutManager(this);
        //layoutManager = new GridLayoutManager(this,3);

        // 지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
        recyclerView.setLayoutManager(layoutManager);

        Adapter = new GiftRecyclerViewAdapter(items, giftVO.getStampCount(),this);
        recyclerView.setAdapter(Adapter);
    }

    private ArrayList<GiftListItem> setAdapterItem(GiftVO vo){
        ArrayList<GiftListItem> items = new ArrayList<>();
        for(UserGrade grade : vo.getGrades()){
            int achieveCode = 00;
            if(grade.getAchieve_count()<=vo.getStampCount()){
                achieveCode = 01;
            }
            GiftListItem item = new GiftListItem(grade.getGradeName(),grade.getAchieve_count(),achieveCode);
            items.add(item);
        }

        //달성도 검사
        for(UserAchieve achieve : vo.getAchieves()){
            for(int i = 0; i<vo.getGrades().size(); i++){
                if(achieve.getGrede().equals(vo.getGrades().get(i).getGradeName())){
                    items.get(i).setState(02);
                }
            }
        }
        return items;
    }

    private void request_gift(String nick, String token) {
        String path = RequestPath.req_url_gift_list.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),nick);
        params.put(ResponseKey.TOKEN.getKey(),token);
        progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG,response.toString());
                progressWaitDaialog.dismiss();
                JSONObject resultData = null;
                try {
                    String result_msg = response.getString(ResponseKey.MESSAGE.getKey());
                    String result_code = response.getString(ResponseKey.CODE.getKey());


                    if(result_code.equals(ResponseCode.SUCCESS.getCode())&&result_msg.equals(ResponseMsg.SUCCESS.getMessage())){

                        resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                        JSONObject myCount = resultData.getJSONObject(ResponseKey.MYCOUNT.getKey());
                        JSONArray allGrade = resultData.getJSONArray(ResponseKey.ALLGRADE.getKey());
                        JSONArray agoGift = resultData.getJSONArray(ResponseKey.AGOGIFT.getKey());

                        int stamp_count = myCount.getInt(ResponseKey.MYSTAMPCOUNT.getKey());
                        ArrayList<UserGrade> grades = new ArrayList();
                        ArrayList<UserAchieve> achieves = new ArrayList();

                        for(int i = 0; i<agoGift.length(); i++){
                            JSONObject obj = agoGift.getJSONObject(i);
                            String myGarde = obj.getString(ResponseKey.GRADE.getKey());
                            String checkTime = obj.getString(ResponseKey.CHECKTIME.getKey());
                            achieves.add(new UserAchieve(myGarde,checkTime));
                        }

                        for(int i = 0; i<allGrade.length(); i++){
                            JSONObject obj = allGrade.getJSONObject(i);
                            String grade = obj.getString(ResponseKey.GRADE.getKey());
                            int achieve = obj.getInt(ResponseKey.MYSTAMPCOUNT.getKey());
                            grades.add(new UserGrade(grade,achieve));
                        }


                        GiftVO giftVO = new GiftVO(stamp_count,achieves,grades);
                        Log.e("giftVO",giftVO.toString());
                        setAdapter(gift_manage_recyclerview,giftVO);

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
    protected void onPostResume() {
        super.onPostResume();
        setInitData();
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
