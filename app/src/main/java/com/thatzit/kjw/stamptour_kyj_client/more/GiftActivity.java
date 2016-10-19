package com.thatzit.kjw.stamptour_kyj_client.more;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.RequestPath;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.TermsActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GiftActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gift_accept_textview;
    private EditText gift_name_input_text;
    private EditText gift_phone_input_text;
    private Button gift_sendjoin_btn;
    private ImageButton gift_toolbar_back;

    private ProgressWaitDaialog progressWaitDaialog;
    private String TAG = "GiftActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setLayout();
        setInitData();
    }

    private void setInitData() {
        progressWaitDaialog = new ProgressWaitDaialog(this);

    }

    private void setLayout() {
        gift_accept_textview = (TextView)findViewById(R.id.gift_accept_textview);
        gift_name_input_text = (EditText)findViewById(R.id.gift_name_input_text);
        gift_phone_input_text = (EditText)findViewById(R.id.gift_phone_input_text);
        gift_sendjoin_btn = (Button)findViewById(R.id.gift_sendjoin_btn);
        gift_toolbar_back = (ImageButton)findViewById(R.id.gift_toolbar_back);

        gift_accept_textview.setOnClickListener(this);
        gift_sendjoin_btn.setOnClickListener(this);
        gift_toolbar_back.setOnClickListener(this);
    }

    void send(){
        LoggedInInfo info = new PreferenceManager(this).getLoggedIn_Info();
        String nick = info.getNick();
        String token = info.getAccesstoken();
        String grade = (String)getIntent().getStringExtra(ResponseKey.GRADE.getKey());
        String name = gift_name_input_text.getText().toString();
        String phone = gift_phone_input_text.getText().toString();
        String count = (String)getIntent().getStringExtra(ResponseKey.MYSTAMPCOUNT.getKey());
        if(name.trim().isEmpty()||phone.trim().isEmpty()){
            Toast.makeText(GiftActivity.this,getResources().getString(R.string.input_empty_msg),Toast.LENGTH_LONG).show();
        }else{
            request_gift(nick,token,grade,name,phone,count);
        }

    }

    private void request_gift(String nick, String token, String grade,String name, String phone, String count) {
        String path = RequestPath.req_url_gift_aply.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),nick);
        params.put(ResponseKey.TOKEN.getKey(),token);
        params.put(ResponseKey.GRADE.getKey(),grade);
        params.put(ResponseKey.REALNAME.getKey(),name);
        params.put(ResponseKey.PHONE.getKey(),phone);
        params.put(ResponseKey.MYSTAMPCOUNT.getKey(),count);

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
                        Toast.makeText(GiftActivity.this,getResources().getString(R.string.gift_apply_succ),Toast.LENGTH_LONG).show();
                        GiftActivity.this.finish();
                        // resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());

                    }else{
                        Toast.makeText(GiftActivity.this,getResources().getString(R.string.gift_apply_fail),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GiftActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(GiftActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.gift_accept_textview:
                Intent intent = new Intent(GiftActivity.this,TermsActivity.class);
                startActivity(intent);
                break;
            case R.id.gift_sendjoin_btn:
                send();
                Toast.makeText(this,"선물신청",Toast.LENGTH_SHORT).show();
                break;
            case R.id.gift_toolbar_back:
                finish();
                break;

        }
    }
}
