package com.thatzit.kjw.stamptour_kyj_client.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;

/**
 * Created by kjw on 16. 8. 21..
 */
public class PreferenceManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private String nick;
    private int FILEnubmer;

    public PreferenceManager(Context context) {
        this.context = context;
        pref=context.getSharedPreferences(context.getString(R.string.preference_private_name),context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstStart(){
        editor.putBoolean(PreferenceKey.FIRST.getKey(),true);
        editor.commit();
    }
    public boolean getFirstStart(){
        return pref.getBoolean("first",false);
    }
    //로그인 정보 저장
    //유형별 유저에서 각각 호출
    public void normal_LoggedIn(String nick,String accesstoken){
        editor.putString(PreferenceKey.NICK.getKey(),nick);
        editor.putString(PreferenceKey.ACCESSTOKEN.getKey(),accesstoken);
        editor.putString(PreferenceKey.LOGGEDINCASE.getKey(), LoggedInCase.NORMAL.getLogin_case());
        editor.commit();
    }
    public void facebook_LoggedIn(String nick,String accesstoken){
        editor.putString(PreferenceKey.NICK.getKey(),nick);
        editor.putString(PreferenceKey.ACCESSTOKEN.getKey(),accesstoken);
        editor.putString(PreferenceKey.LOGGEDINCASE.getKey(), LoggedInCase.FBLogin.getLogin_case());
        editor.commit();
    }
    public void kakaotalk_LoggedIn(String nick,String accesstoken){
        editor.putString(PreferenceKey.NICK.getKey(),nick);
        editor.putString(PreferenceKey.ACCESSTOKEN.getKey(),accesstoken);
        editor.putString(PreferenceKey.LOGGEDINCASE.getKey(), LoggedInCase.KAKAOLogin.getLogin_case());
        editor.commit();
    }

    public LoggedInInfo getLoggedIn_Info() {
        String loggedincase=pref.getString(PreferenceKey.LOGGEDINCASE.getKey(),"");
        String nick = pref.getString(PreferenceKey.NICK.getKey(),"");
        String accesstoken = pref.getString(PreferenceKey.ACCESSTOKEN.getKey(),"");
        LoggedInInfo info = new LoggedInInfo(nick,accesstoken,loggedincase);
        return info;
    }
}