package com.thatzit.kjw.stamptour_kyj_client.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;
import com.thatzit.kjw.stamptour_kyj_client.util.ValidPattern;
import com.thatzit.kjw.stamptour_kyj_client.util.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView join_accept_textview;
    private EditText email_input;
    private EditText nick_input;
    private EditText password_input;
    private EditText password_input_repeat;
    private Button btn_check_email_duplicate;
    private Button btn_check_nick_duplicate;
    private Button btn_sendjoin;
    private ImageButton btn_close;

    private String TAG = "JoinActivity";
    private boolean duplicate_check_email;
    private boolean duplicate_check_nick;
    private ProgressWaitDaialog progressWaitDaialog;
    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_join);
        setLayout();
        setInitData();
    }

    private void setInitData() {
        duplicate_check_email = false;
        duplicate_check_nick = false;
        validator = new Validator();
        progressWaitDaialog = new ProgressWaitDaialog(this);
    }


    private void setLayout() {
        join_accept_textview = (TextView)findViewById(R.id.join_accept_textview);
        email_input = (EditText)findViewById(R.id.email_input);
        nick_input = (EditText)findViewById(R.id.nick_input);
        password_input = (EditText)findViewById(R.id.password_input);
        password_input_repeat  = (EditText)findViewById(R.id.password_input_repeat);
        btn_check_email_duplicate  = (Button)findViewById(R.id.btn_check_email_duplicate);
        btn_check_nick_duplicate = (Button)findViewById(R.id.btn_check_nick_duplicate);
        btn_sendjoin  = (Button)findViewById(R.id.btn_sendjoin);
        btn_close = (ImageButton)findViewById(R.id.btn_close);

        join_accept_textview.setOnClickListener(this);
        btn_check_email_duplicate.setOnClickListener(this);
        btn_check_nick_duplicate.setOnClickListener(this);
        btn_sendjoin.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        email_input.addTextChangedListener(textWatcherEmail);
        nick_input.addTextChangedListener(textWatcherNick);

    }



    private void request_Duplicate_Check_Nick(String user_input_nick) {
        String path = RequestPath.req_url_nick_check.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),user_input_nick);
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
                        if(result_data.equals(ResponseMsg.DUPLICATE.getMessage())){
                            Toast.makeText(JoinActivity.this,getResources().getString(R.string.join_normal_duplicate_nick),Toast.LENGTH_LONG).show();
                            duplicate_check_nick = false;
                        }else{
                            Toast.makeText(JoinActivity.this,getString(R.string.join_normal_valid_nick),Toast.LENGTH_LONG).show();
                            duplicate_check_nick = true;
                        }
                    }else{
                        Toast.makeText(JoinActivity.this,getResources().getString(R.string.join_normal_duplicate_nick),Toast.LENGTH_LONG).show();
                        duplicate_check_nick = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                duplicate_check_nick = false;
            }
        });
    }

    private void request_Duplicate_Check_Email(String user_input_email) {
        String path = RequestPath.req_url_id_check.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.ID.getKey(),user_input_email);
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
                        if(result_data.equals(ResponseMsg.DUPLICATE.getMessage())){
                            Toast.makeText(JoinActivity.this,getResources().getString(R.string.join_normal_duplicate_email),Toast.LENGTH_LONG).show();
                            duplicate_check_email = false;
                        }else{
                            Toast.makeText(JoinActivity.this,getResources().getString(R.string.join_normal_valid_email),Toast.LENGTH_LONG).show();
                            duplicate_check_email = true;
                        }

                    }else{
                        Toast.makeText(JoinActivity.this,getResources().getString(R.string.join_normal_duplicate_email),Toast.LENGTH_LONG).show();
                        duplicate_check_email = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                duplicate_check_email = false;
            }
        });
    }

    private void request_Send_Join(String user_input_nick, String user_input_email, String user_input_password) {
        String path = RequestPath.req_url_join_normal.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.ID.getKey(),user_input_email);
        params.put(ResponseKey.NICK.getKey(),user_input_nick);
        params.put(ResponseKey.PASSWORD.getKey(),user_input_password);
        params.put("loggedincase", LoggedInCase.NORMAL.getLogin_case());
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
                        Toast.makeText(JoinActivity.this,getResources().getString(R.string.join_success),Toast.LENGTH_LONG).show();
                        duplicate_check_email = true;
                        duplicate_check_nick = true;
                        Intent intent = new Intent(JoinActivity.this,LoginActivity.class);
                        JoinActivity.this.startActivity(intent);
                        JoinActivity.this.finish();
                    }else{
                        Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                        duplicate_check_email = false;
                        duplicate_check_nick = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(JoinActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                duplicate_check_nick = false;
                duplicate_check_email = false;
            }
        });
    }

    private void Duplicate_Check_Nick() {
        String user_input_nick = nick_input.getText().toString();
        if(validator.nickValidate(ValidPattern.getNick_patten(),user_input_nick)){
            request_Duplicate_Check_Nick(user_input_nick);
        }else{
            duplicate_check_nick = false;
            Toast.makeText(this,getString(R.string.validate_failure),Toast.LENGTH_LONG).show();
        }
    }

    private void Duplicate_Check_Email() {
        String user_input_email = email_input.getText().toString();
        if(validator.emailValidate(ValidPattern.getEmail_patten(),user_input_email)){
            request_Duplicate_Check_Email(user_input_email);
        }else{
            duplicate_check_email = false;
            Toast.makeText(this,getString(R.string.validate_failure),Toast.LENGTH_LONG).show();
        }
    }

    void send_join(){
        String user_input_nick = nick_input.getText().toString().trim();
        String user_input_email = email_input.getText().toString().trim();
        String user_input_password = password_input.getText().toString().trim();
        String user_input_password_repeat = password_input_repeat.getText().toString().trim();
        if(duplicate_check_email&&duplicate_check_nick){
            if(user_input_password.equals(user_input_password_repeat)){
                if(validator.nickValidate(ValidPattern.getNick_patten(),user_input_nick)){
                    //nick은 valid
                    if(validator.emailValidate(ValidPattern.getEmail_patten(),user_input_email)){
                        //email은 valid
                        if(validator.passwordValidate(ValidPattern.getPass_patten(),user_input_password)){
                            //password는 valid
                            request_Send_Join(user_input_nick,user_input_email,user_input_password);
                        }else{
                            //password invalid
                            Toast.makeText(this,getResources().getString(R.string.invalid_password),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        //email invalid
                        Toast.makeText(this,getResources().getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
                    }
                }else{
                    //nick invalid
                    Toast.makeText(this,getResources().getString(R.string.invalid_nick),Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,getResources().getString(R.string.not_equal_password),Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,getResources().getString(R.string.please_duplicate_check),Toast.LENGTH_LONG).show();
        }
    }

    TextWatcher textWatcherNick = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable edit) {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            duplicate_check_email = false;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher textWatcherEmail = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable edit) {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            duplicate_check_nick = false;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_check_email_duplicate:
                Duplicate_Check_Email();
                break;
            case R.id.btn_check_nick_duplicate:
                Duplicate_Check_Nick();
                break;
            case R.id.btn_sendjoin:
                send_join();
                break;
            case R.id.btn_close:
                finish();
                break;
            case R.id.join_accept_textview:
                Toast.makeText(this,getString(R.string.validate_failure),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(JoinActivity.this,TermsActivity.class);
                this.startActivity(intent);
                break;

        }
    }
}
