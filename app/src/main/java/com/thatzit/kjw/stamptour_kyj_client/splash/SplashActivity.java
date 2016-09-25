package com.thatzit.kjw.stamptour_kyj_client.splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.checker.VersoinChecker;
import com.thatzit.kjw.stamptour_kyj_client.help.HelpActivity;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.GpsService;
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;

public class SplashActivity extends Activity {
    private ProgressDialog dlg;
    private AnimationDrawable aniFrame;
    private PreferenceManager preferenceManger;
    private ImageView splashAnimation;
    private Activity self;
    private Context myApplication= MyApplication.getContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        self = this;
        preferenceManger = new PreferenceManager(this);
        dlg = new ProgressDialog(self);
        Intent findGPS = new Intent(myApplication, GpsService.class);
        startService(findGPS);
        setLayout();
    }
    private void setLayout() {
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000); // 3초 후에 hd Handler 실행
    }

    private class splashhandler implements Runnable{
        public void run() {
            if(!preferenceManger.getFirstStart()){
                preferenceManger.setFirstStart();
//                Toast.makeText(getApplicationContext(),"처음임",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplication(), LoginActivity.class));
                SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
                return;
            }else{
                //loggedin after version check, Not loggedIn don't do this
                //because version check needs parameters nick & accesstoken
                if(preferenceManger.getLoggedIn_Info().getNick().equals(""))
                {
                    startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
                    SplashActivity.this.finish();
                }else{
//                    Toast.makeText(getApplicationContext(),"로그인 안되있음 처음은"+preferenceManger.getFirstStart(),Toast.LENGTH_LONG).show();
                    VersoinChecker checker = new VersoinChecker(self);
                    checker.check();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        //백키로 종료되지않게 제어
    }
    public void showCheckDialog(final boolean show){

        if(show){

            dlg.setProgress(0);
            dlg.setMessage("콘텐츠 리소스 확인중...");
            dlg.setCancelable(false);
            dlg.show();
        }else{
            dlg.dismiss();
        }
    }
}
