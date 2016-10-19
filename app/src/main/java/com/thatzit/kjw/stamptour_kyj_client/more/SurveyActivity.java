package com.thatzit.kjw.stamptour_kyj_client.more;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SurveyActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {

    private EditText survey_msg_text;
    private RadioGroup survey_radio_group;

    private ImageButton survey_toolbar_back;
    private ImageButton survey_toolbar_send;

    private String TAG = "SurveyActivity";
    private ProgressWaitDaialog progressWaitDaialog;
    private PreferenceManager manager;
    private int radioGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        setLayout();
        setInitData();

    }

    private void setInitData() {
        progressWaitDaialog = new ProgressWaitDaialog(this);
        manager = new PreferenceManager(this);
        radioGrade = 0;
    }

    private void setLayout() {
        survey_msg_text = (EditText)findViewById(R.id.survey_msg_text);
        survey_radio_group = (RadioGroup) findViewById(R.id.survey_radio_group);

        survey_toolbar_back = (ImageButton) findViewById(R.id.survey_toolbar_back);
        survey_toolbar_send = (ImageButton)findViewById(R.id.survey_toolbar_send);

        survey_radio_group.setOnCheckedChangeListener(this);
        survey_toolbar_back.setOnClickListener(this);
        survey_toolbar_send.setOnClickListener(this);
    }

    private void send(){
        if(radioGrade != 0){
            LoggedInInfo info = manager.getLoggedIn_Info();
            String nick = info.getNick();
            String token = info.getAccesstoken();
            String msg = survey_msg_text.getText().toString().isEmpty()?"":survey_msg_text.getText().toString();
            String grade = String.valueOf(radioGrade);
            Log.e(TAG,nick+","+token+","+msg+","+grade);
            request_Send(nick,token,msg,grade);
        }else {
            Toast.makeText(this,getResources().getString(R.string.survey_not_enough),Toast.LENGTH_LONG).show();
        }
    }

    private void request_Send(String nick, String token, String msg, String grade) {
        String path = RequestPath.req_url_survey_apply.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),nick);
        params.put(ResponseKey.TOKEN.getKey(),token);
        params.put(ResponseKey.SURVEY_POINT.getKey(),grade);
        params.put(ResponseKey.CONTENTS.getKey(),msg);
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
                        Toast.makeText(SurveyActivity.this,getResources().getString(R.string.survey_complete),Toast.LENGTH_LONG).show();
                        SurveyActivity.this.finish();
                    }else if(result_code.equals(ResponseCode.DBERR.getCode())&&result_msg.equals(ResponseMsg.FAIL.getMessage())){
                        Toast.makeText(SurveyActivity.this,getResources().getString(R.string.survey_aleady),Toast.LENGTH_LONG).show();
                        SurveyActivity.this.finish();
                    }else{
                        Toast.makeText(SurveyActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SurveyActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(SurveyActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.survey_toolbar_back:
                finish();
                break;
            case R.id.survey_toolbar_send:
                send();
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.survey_radio_btn_1:
                radioGrade = 1;
                break;
            case R.id.survey_radio_btn_2:
                radioGrade = 2;
                break;
            case R.id.survey_radio_btn_3:
                radioGrade = 3;
                break;
            case R.id.survey_radio_btn_4:
                radioGrade = 4;
                break;
            case R.id.survey_radio_btn_5:
                radioGrade = 5;
                break;
        }
    }
}
