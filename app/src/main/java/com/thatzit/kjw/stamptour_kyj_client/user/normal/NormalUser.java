package com.thatzit.kjw.stamptour_kyj_client.user.normal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.checker.VersoinChecker;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.splash.SplashActivity;
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
    public NormalUser(String accesstoken,Context context){
        this.context = context;
        preferenceManager = new PreferenceManager(context);
        this.nick = preferenceManager.getLoggedIn_Info().getNick();
        this.accesstoken = accesstoken;

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
            public String login_success_check(String nick,String accesstoken){
                String message;
                if(nick.equals("0")&&accesstoken.equals("-1")){
                    return message = "잘못된 비밀번호입니다.";
                }else if(nick.equals("-1")&&accesstoken.equals("-1")){
                    return message = "잘못된 아이디이거나 회원이 아닙니다.";
                }
                return message = "성공";
            }
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
                        String result = login_success_check(nick,accesstoken);
                        if(!result.equals("성공")){
                            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
                            ((LoginActivity)context).showProgress(false);
                            return;
                        }
                        Toast.makeText(context,context.getString(R.string.Toast_login_Success),Toast.LENGTH_LONG).show();
                        preferenceManager.normal_LoggedIn(nick,accesstoken);
                        if(preferenceManager.getVersion().getVersion() == 0 && preferenceManager.getVersion().getSize() == 0){
                            Log.e("FIRST_CHECK",preferenceManager.getVersion().getVersion()+"");
                            preferenceManager.normal_LoggedIn(nick,accesstoken);

                        }

                        //버전 체크에 accesstoken이 필요하므로 로그아웃 했다가 다시 로그인할 시 스플래쉬에선 체크 불가
                        //로그인에서 다시 한번 체크할 수 있도록 로그인에서는 항상 버전 체크
                        VersoinChecker versoinChecker = new VersoinChecker(context);
                        versoinChecker.check();
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
    public void LoggeOut() {
        RequestParams params = new RequestParams();
        params.put("nick",nick);
        params.put("accesstoken",accesstoken);
        StampRestClient.post(context.getString(R.string.req_url_loggedout),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("NormalUser",response.toString());
                String code = null;
                String msg = null;
                String resultData = null;
                try{
                    code = response.getString(ResponseKey.CODE.getKey());
                    msg = response.getString(ResponseKey.MESSAGE.getKey());
                    resultData = response.getString(ResponseKey.RESULTDATA.getKey());
                    if(code.equals("00")){
                        Toast.makeText(context,"로그아웃 성공",Toast.LENGTH_LONG).show();
                        preferenceManager.user_LoggedOut();
                        Intent intent = new Intent(context, SplashActivity.class);
                        context.startActivity(intent);
                        ((MainActivity)context).finish();
                    }else if(code.equals("03")){
                        Toast.makeText(context,"로그아웃 실패 계속되면 다시 설치해주세요",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    Log.e("NormalUser",e.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
