package com.thatzit.kjw.stamptour_kyj_client.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;
import com.thatzit.kjw.stamptour_kyj_client.util.ValidPattern;
import com.thatzit.kjw.stamptour_kyj_client.util.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FindPassActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nick_input;
    private EditText email_input;
    private ImageButton find_pass_back;
    private ImageButton find_pass_done;

    private String TAG = "FindPassActivity";
    private Validator validator;
    private ProgressWaitDaialog progressWaitDaialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        setLayout();
        setInitData();

    }

    private void setInitData() {
        validator = new Validator();
        progressWaitDaialog = new ProgressWaitDaialog(this);
    }

    private void setLayout() {
        nick_input = (EditText)findViewById(R.id.nick_input);
        email_input = (EditText)findViewById(R.id.email_input);
        find_pass_back = (ImageButton) findViewById(R.id.find_pass_back);
        find_pass_done = (ImageButton)findViewById(R.id.find_pass_done);

        find_pass_back.setOnClickListener(this);
        find_pass_done.setOnClickListener(this);
    }

    private void find() {
        String user_input_nick = nick_input.getText().toString();
        String user_input_email = email_input.getText().toString();
        if(validator.nickValidate(ValidPattern.getNick_patten(),user_input_nick)
                &&validator.emailValidate(ValidPattern.getEmail_patten(),user_input_email)){
            request_find(user_input_nick,user_input_email);
        }else{
            Toast.makeText(this,getString(R.string.validate_failure),Toast.LENGTH_LONG).show();
        }
    }

    private void request_find(String user_input_nick, String user_input_email) {
        String path = RequestPath.req_url_find_pass.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),user_input_nick);
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

                        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(FindPassActivity.this);

                        alertdialog.setMessage(result_data);
                        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FindPassActivity.this.finish();
                                Intent intent = new Intent(FindPassActivity.this,LoginActivity.class);
                                FindPassActivity.this.startActivity(intent);
                            }
                        });
                        alertdialog.setNegativeButton("Cancel",null);
                        AlertDialog alert = alertdialog.create();
                        alert.setTitle(getString(R.string.find_pass_title_text));
                        alert.show();


                    }else{
                        Toast.makeText(FindPassActivity.this,getResources().getString(R.string.find_pass_not_text),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(FindPassActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                progressWaitDaialog.dismiss();
                Toast.makeText(FindPassActivity.this,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.find_pass_back:
                finish();
                break;
            case R.id.find_pass_done:
                find();
                break;
        }
    }
}
