package com.thatzit.kjw.stamptour_kyj_client.login;

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
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.TermsActivity;
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SocialJoinActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SocialJoinActivity";
    private ImageButton btn_close;
    private TextView join_accept_textview;
    private Intent parentIntent;
    private String socialid;
    private String loggedincase;
    private boolean changedflag=false;

    private static final int CANCLEJOINSOCIALUSER = 54321;
    private static final int JOINSOCIALUSER = 54322;
    private Button btn_check_duplicate;
    private EditText nick_input;
    private ProgressWaitDaialog progressWaitDaialog;
    private SocialJoinActivity self;
    private Button btn_sendjoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_join);
        setLayout();
    }

    private void setLayout() {
        self = this;
        //MyApplication.setCurrentActivity(this);

        join_accept_textview = (TextView)findViewById(R.id.join_accept_textview);
        join_accept_textview.setOnClickListener(this);

        progressWaitDaialog = new ProgressWaitDaialog(this);
        parentIntent = getIntent();
        socialid = parentIntent.getStringExtra("id");
        loggedincase = parentIntent.getStringExtra("LoggedIncase");
        btn_close = (ImageButton) findViewById(R.id.btn_close);
        nick_input = (EditText) findViewById(R.id.nick_input);
        btn_sendjoin = (Button) findViewById(R.id.btn_sendjoin);
        btn_check_duplicate = (Button) findViewById(R.id.btn_check_duplicate);
        btn_close.setOnClickListener(this);
        btn_sendjoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close:
                setResult(CANCLEJOINSOCIALUSER);

                finish();
                break;
            case R.id.btn_check_duplicate:

                break;
            case R.id.btn_sendjoin:
                requestSendJoin();
                break;
            case R.id.join_accept_textview:
                Intent intent = new Intent(SocialJoinActivity.this,TermsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void requestSendJoin() {
        String path = RequestPath.req_url_join_normal.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.ID.getKey(),socialid);
        String user_input_nick;
        user_input_nick = nick_input.getText().toString();
        params.put(ResponseKey.NICK.getKey(),user_input_nick);
        params.put("loggedincase", LoggedInCase.KAKAOLogin.getLogin_case());
        progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG,response.toString());
                progressWaitDaialog.dismiss();
                try {
                    String result_msg = response.getString(ResponseKey.MESSAGE.getKey());
                    String result_code = response.getString(ResponseKey.CODE.getKey());
                    if(result_code.equals(ResponseCode.SUCCESS.getCode())&&result_msg.equals(ResponseMsg.SUCCESS.getMessage())){
                        Toast.makeText(self,getResources().getString(R.string.join_success),Toast.LENGTH_LONG).show();
                        setResult(JOINSOCIALUSER);
                        finish();
                    }else{
                        Toast.makeText(self,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(self,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(self,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }
}
