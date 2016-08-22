package com.thatzit.kjw.stamptour_kyj_client.user.normal;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.user.User;
import com.thatzit.kjw.stamptour_kyj_client.user.normal.action.NormalLoggedIn_Behavior;
import com.thatzit.kjw.stamptour_kyj_client.user.normal.action.NormalLoggedOut_Behavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 8. 21..
 */
public class NormalUser extends User implements NormalLoggedIn_Behavior,NormalLoggedOut_Behavior{
    private String id;
    private String nick;
    private String password;
    private String accesstoken;
    private Context context;
    private PreferenceManager preferenceManager;
    public NormalUser() {
        super();
    }

    public NormalUser(String id, String password,Context context) {
        this.id = id;
        this.password = password;
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }


    public String getAccesstoken() {
        return accesstoken;
    }

    @Override
    public void LoggedIn(String id, String password) {
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("password",password);
        params.put("loggedincase", LoggedInCase.NORMAL.getLogin_case());
        Log.e("NormalUser-Call","Call");
        ((LoginActivity)context).showProgress(true);
        StampRestClient.post(context.getString(R.string.req_url_loggedin),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.e("NormalUser",response.toString());
                String code = null;
                String msg = null;
                String nick = null;
                String accesstoken = null;
                JSONObject resultData = null;
                try{
                    code = response.getString(ResponseKey.CODE.getKey());
                    msg = response.getString(ResponseKey.MESSAGE.getKey());
                    if(code.equals(ResponseCode.SUCCESS.getCode())&&msg.equals(ResponseMsg.SUCCESS.getMessage())){
                        resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                        nick = resultData.getString(ResponseKey.NICK.getKey());
                        accesstoken = resultData.getString(ResponseKey.TOKEN.getKey());
                        Toast.makeText(context,context.getString(R.string.Toast_login_Success),Toast.LENGTH_LONG).show();
                        preferenceManager.normal_LoggedIn(nick,accesstoken);
                        if(preferenceManager.getFirstStart()){
                            Log.e("FIRST_CHECK",preferenceManager.getFirstStart()+"");
                            preferenceManager.normal_LoggedIn(nick,accesstoken);
                        }else{
                            Log.e("FIRST_CHECK",preferenceManager.getFirstStart()+"");
//                            ((LoginActivity) context).downloadContents(nick,accesstoken,LoggedInCase.NORMAL.getLogin_case());
                            ((LoginActivity) context).downloadContents_zip(nick,accesstoken,LoggedInCase.NORMAL.getLogin_case());
                        }
                    }
                }catch (JSONException e){
                    Log.e("NormalUser",e.toString());
                    try {
                        String data = response.getString(ResponseKey.RESULTDATA.getKey());
                        Toast.makeText(context,data,Toast.LENGTH_LONG).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                ((LoginActivity)context).showProgress(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                // Pull out the first event on the public timeline


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("NormalUser",errorResponse.toString());
                ((LoginActivity)context).showProgress(false);
            }
        });
    }

    @Override
    public void LoggeOut(String accesstoken) {
        return;
    }
}
