package com.thatzit.kjw.stamptour_kyj_client.push.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.checker.VersoinChecker;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 9. 2..
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
    PreferenceManager preferenceManager;
    public MyInstanceIDListenerService() {

    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("MyInstanceService","CALL");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("MyInstanceService", "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        //Device GCM AccessToken(InstanceID) Send and Regist To Server
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        RequestParams params = new RequestParams();
        Context context = getApplicationContext();
        preferenceManager = new PreferenceManager(context);
        preferenceManager.setGCMaccesstoken(refreshedToken);
        params.put("devicetoken",refreshedToken);
        AsyncHttpClient client = StampRestClient.getClient();
        client.post(context,StampRestClient.BASE_URL+context.getResources().getString(R.string.req_url_regist_device),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("GCM_Regist_SUCCESS",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("GCM_Regist_Fail",errorResponse+"");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("GCM_Regist_Fail",errorResponse+"");
            }
        });
    }
}
