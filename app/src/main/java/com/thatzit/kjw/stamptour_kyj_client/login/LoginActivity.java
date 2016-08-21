package com.thatzit.kjw.stamptour_kyj_client.login;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.thatzit.kjw.stamptour_kyj_client.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.user.User;
import com.thatzit.kjw.stamptour_kyj_client.user.normal.NormalUser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int MY_RMISSION_REQUEST_WRITE = 33;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private User user;
    private boolean permission_on=false;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        preferenceManager = new PreferenceManager(this);
        if(preferenceManager.getFirstStart()&&(!preferenceManager.getLoggedIn_Info().getAccesstoken().equals(""))){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        checkPermission();
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "앱 내의 컨텐츠 저장용으로 사용됩니다.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS
                }, MY_RMISSION_REQUEST_WRITE);


                // MY_PERMISSION_REQUEST_STORAGE is an
                // app-defined int constant

            } else {
                // 다음 부분은 항상 허용일 경우에 해당이 됩니다.

                return true;
            }
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode== MY_RMISSION_REQUEST_WRITE&&grantResults.length>0) {
            Log.e("grant size",grantResults.length+"");
            int apply_cnt=0;
            for(int i=0;i<grantResults.length;i++)
            {
                Log.e("GrantResult"+i,grantResults[i]+":"+permissions[i]);
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED)apply_cnt++;
            }
            if(apply_cnt==3)
            {
                //허용됨

            }else
            {
                Log.d("permission", "Permission always deny");
                Toast.makeText(this,"앱 권한을 다시 설정해주세요",Toast.LENGTH_LONG).show();
            }
        }
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() throws JSONException {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            Log.e("LoginActivity","email & password Invalid");
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //일반회원 로그인 로
            user = new NormalUser(email,password,this);
            ((NormalUser) user).LoggedIn(email,password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public String checkLangauagelocale(){
        String contents_down_url;
        Locale systemLocale = getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();
        switch (strLanguage){
            case "ko":contents_down_url=getString(R.string.req_url_download_kr);break;
            case "en":contents_down_url=getString(R.string.req_url_download_en);break;
            case "zh":contents_down_url=getString(R.string.req_url_download_ch);break;
            case "ja":contents_down_url=getString(R.string.req_url_download_jp);break;
            default:contents_down_url=getString(R.string.req_url_download_kr);break;
        }
        return contents_down_url;
    }
    public void downloadContents(final String nick, final String accesstoken, final String loggedincase){
        RequestParams params = new RequestParams();
        params.put("nick",nick);
        params.put("accesstoken",accesstoken);
        String contents_down_url = checkLangauagelocale();
        Log.e("download",nick+":"+accesstoken);
        final ProgressDialog dlg = new ProgressDialog(this,ProgressDialog.STYLE_HORIZONTAL);
        dlg.setProgress(0);
        dlg.setMessage("필요한 컨텐츠 다운로드중...");
        dlg.setCancelable(false);
        dlg.show();
        //ko
        //en
        //zh
        //ja

        StampRestClient.get(contents_down_url,params,new FileAsyncHttpResponseHandler(this){
            private String createDirectory(){
                String sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();
                String dirPath = sdcard+"/StampTour_kyj/kr";
                File dir = new File(dirPath);
                if( !dir.exists() ) dir.mkdirs();
                return dirPath;
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.e("filedown","fail");
                dlg.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Log.e("filedown", "File name :" + file.toString());
                dlg.dismiss();

                Log.e("dir", Environment.getExternalStorageDirectory().getAbsolutePath());
                String sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();

                String path=createDirectory();
                File jsonFile= new File(path,"kr.json");
                StringBuilder text = new StringBuilder();
                String line;

                try{

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while((line=br.readLine())!=null){
                        text.append(line);
                        Log.e("Text",line);
                    }
                    Writer writer = new OutputStreamWriter(new FileOutputStream(jsonFile), "UTF-8");
                    writer.write(text.toString());
                    writer.close();
                    switch (loggedincase){
                        case "NORMAL":preferenceManager.normal_LoggedIn(nick,accesstoken);break;
                        case "FBLogin":preferenceManager.facebook_LoggedIn(nick,accesstoken);break;
                        case "KAKAOLogin":preferenceManager.kakaotalk_LoggedIn(nick,accesstoken);break;
                    }
                    preferenceManager.setFirstStart();
                }catch (FileNotFoundException e) {
                    Log.e("WriteFile", e.toString());
                }catch (IOException e){
                    Log.e("WriteFile",e.toString());
                }
            }
        });
    }

}

