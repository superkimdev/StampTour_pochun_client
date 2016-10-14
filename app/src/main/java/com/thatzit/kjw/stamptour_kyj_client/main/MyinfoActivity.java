package com.thatzit.kjw.stamptour_kyj_client.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;
import com.thatzit.kjw.stamptour_kyj_client.util.ValidPattern;
import com.thatzit.kjw.stamptour_kyj_client.util.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyinfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyinfoActivity";
    private ImageButton comm_toolbar_home_close;
    private ImageButton comm_toolbar_home_confirm;
    private TextView email_contents;
    private TextView nick_contents;
    private Button account_delete;
    private EditText password_input;
    private EditText password_input_repeat;
    private MyinfoActivity self;
    private String input_pw;
    private String input_pw_repeat;

    private Validator validator;
    private PreferenceManager preferenceManager;
    private ProgressWaitDaialog progressWaitDaialog;
    private final int USERINFOCHANGED = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        self = this;
        validator = new Validator();
        preferenceManager = new PreferenceManager(this);
        progressWaitDaialog = new ProgressWaitDaialog(this);
        setLayout();
    }

    private void setLayout() {
        comm_toolbar_home_close = (ImageButton) findViewById(R.id.comm_toolbar_home_close);
        comm_toolbar_home_confirm = (ImageButton) findViewById(R.id.comm_toolbar_home_confirm);
        email_contents = (TextView) findViewById(R.id.email_contents);
        nick_contents = (TextView) findViewById(R.id.nick_contents);
        password_input = (EditText) findViewById(R.id.password_input);
        password_input_repeat = (EditText) findViewById(R.id.password_input_repeat);
        account_delete = (Button) findViewById(R.id.account_delete);
        LoggedInInfo login_info = preferenceManager.getLoggedIn_Info();

        if(!(login_info.getLoggedincase().equals(LoggedInCase.NORMAL.getLogin_case()))){
            password_input.setHint(getResources().getString(R.string.not_input_socialuser_password));
            password_input_repeat.setHint(getResources().getString(R.string.not_input_socialuser_password));

            password_input.setFocusable(false);
            password_input.setFocusableInTouchMode(false);
            password_input.setClickable(false);
            password_input_repeat.setFocusable(false);
            password_input_repeat.setFocusableInTouchMode(false);
            password_input_repeat.setClickable(false);
        }
        comm_toolbar_home_close.setOnClickListener(this);
        comm_toolbar_home_confirm.setOnClickListener(this);
        account_delete.setOnClickListener(this);
        request_User_Info();
    }

    private void request_User_Info() {
        String path = RequestPath.req_url_user_info.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.TOKEN.getKey(),preferenceManager.getLoggedIn_Info().getAccesstoken());
        params.put(ResponseKey.NICK.getKey(),preferenceManager.getLoggedIn_Info().getNick());
        progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressWaitDaialog.dismiss();
                try {
                    String res_code = response.getString(ResponseKey.CODE.getKey());
                    String message = response.getString(ResponseKey.MESSAGE.getKey());
                    Log.e(TAG,response.toString());
                    if(res_code.equals(ResponseCode.SUCCESS.getCode())&&message.equals(ResponseMsg.SUCCESS.getMessage())){
                        JSONObject resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                        String nick = resultData.getString(ResponseKey.NICKHIGH.getKey());
                        String id = resultData.getString(ResponseKey.ID.getKey());
                        nick_contents.setText(nick);
                        email_contents.setText(id);
                    }else if(res_code.equals(ResponseCode.NOTENOUGHDATA.getCode())
                            &&message.equals(ResponseMsg.INVALIDACCESSTOKEN.getMessage())){
                        preferenceManager.user_LoggedOut();
                        Intent intent = new Intent(self, LoginActivity.class);
                        self.setResult(USERINFOCHANGED);
                        self.startActivity(intent);
                        self.finish();
                    }else{
                        Toast.makeText(self,self.getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressWaitDaialog.dismiss();
                Toast.makeText(self,self.getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_toolbar_home_close:
                finish();
                break;
            case R.id.comm_toolbar_home_confirm:
                check_Password_validation();
                break;
            case R.id.account_delete:
                remove_User();
                break;
        }
    }

    private void remove_User() {
        String path = RequestPath.req_url_user_remove.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.TOKEN.getKey(),preferenceManager.getLoggedIn_Info().getAccesstoken());
        params.put(ResponseKey.DEVICETOKEN.getKey(),preferenceManager.getGCMaccesstoken());
        params.put(ResponseKey.NICK.getKey(),preferenceManager.getLoggedIn_Info().getNick());
        progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressWaitDaialog.dismiss();
                try {
                    String res_code = response.getString(ResponseKey.CODE.getKey());
                    String message = response.getString(ResponseKey.MESSAGE.getKey());
                    Log.e(TAG,response.toString());
                    if(res_code.equals(ResponseCode.SUCCESS.getCode())&&message.equals(ResponseMsg.SUCCESS.getMessage())){
                        Toast.makeText(self,self.getString(R.string.remove_acoount_success),Toast.LENGTH_LONG).show();
                        preferenceManager.user_LoggedOut();
                        Intent intent = new Intent(self, LoginActivity.class);
                        self.setResult(USERINFOCHANGED);
                        self.startActivity(intent);
                        self.finish();
                    }else if(res_code.equals(ResponseCode.NOTENOUGHDATA.getCode())
                            &&message.equals(ResponseMsg.INVALIDACCESSTOKEN.getMessage())){
                        preferenceManager.user_LoggedOut();
                        Intent intent = new Intent(self, LoginActivity.class);
                        self.setResult(USERINFOCHANGED);
                        self.startActivity(intent);
                        self.finish();
                    }else{
                        Toast.makeText(self,self.getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressWaitDaialog.dismiss();
                Toast.makeText(self,self.getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void check_Password_validation() {
        input_pw = password_input.getText().toString();
        input_pw_repeat = password_input_repeat.getText().toString();
        if(validator.passwordValidateForChange(input_pw,input_pw_repeat)){
            Toast.makeText(this,getResources().getString(R.string.password_change_confirm_string),Toast.LENGTH_LONG).show();
        }else{
            if(validator.passwordValidate(ValidPattern.getPass_patten(),input_pw)){
                request_Change_Password();
            }else{
                Toast.makeText(this,getResources().getString(R.string.password_setting_string),Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void request_Change_Password() {
        String path = RequestPath.req_url_password_chnage.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.TOKEN.getKey(),preferenceManager.getLoggedIn_Info().getAccesstoken());
        params.put(ResponseKey.PASSWORD.getKey(),input_pw);
        params.put(ResponseKey.NICK.getKey(),preferenceManager.getLoggedIn_Info().getNick());
        progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressWaitDaialog.dismiss();
                try {
                    String res_code = response.getString(ResponseKey.CODE.getKey());
                    String message = response.getString(ResponseKey.MESSAGE.getKey());
                    Log.e(TAG,response.toString());
                    if(res_code.equals(ResponseCode.SUCCESS.getCode())&&message.equals(ResponseMsg.SUCCESS.getMessage())){
                        Toast.makeText(self,self.getString(R.string.password_change_success),Toast.LENGTH_LONG).show();
                        preferenceManager.user_LoggedOut();
                        Intent intent = new Intent(self, LoginActivity.class);
                        self.setResult(USERINFOCHANGED);
                        self.startActivity(intent);
                        self.finish();
                    }else if(res_code.equals(ResponseCode.NOTENOUGHDATA.getCode())
                            &&message.equals(ResponseMsg.INVALIDACCESSTOKEN.getMessage())){
                        preferenceManager.user_LoggedOut();
                        Intent intent = new Intent(self, LoginActivity.class);
                        self.setResult(USERINFOCHANGED);
                        self.startActivity(intent);
                        self.finish();
                    }else{
                        Toast.makeText(self,self.getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressWaitDaialog.dismiss();
                Toast.makeText(self,self.getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }
}
