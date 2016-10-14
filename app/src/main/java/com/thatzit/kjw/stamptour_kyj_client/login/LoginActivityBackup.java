package com.thatzit.kjw.stamptour_kyj_client.login;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.user.User;
import com.thatzit.kjw.stamptour_kyj_client.user.normal.NormalUser;
import com.thatzit.kjw.stamptour_kyj_client.util.Decompress;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONException;

import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivityBackup extends AppCompatActivity implements OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int MY_RMISSION_REQUEST_WRITE = 33;
    private static final int CANCLEJOINSOCIALUSER = 54321;
    private static final int JOINSOCIALUSER = 54322;
    private static final int STARTSOCIALJOIN = 50000;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private User user;
    private boolean permission_on=false;
    private PreferenceManager preferenceManager;
    private Decompress decompressor;
    private ProgressDialog dlg;

    private TextView join_btn;
    private TextView find_auth_btn;
    private Button email_sign_in_button;
    private Button login_btn_facebook;
    private Button login_btn_kakao;
    private ISessionCallback mKakaoCallback;
    private String TAG = "LoginActivity";
    private ProgressWaitDaialog progressWaitDaialog;
    private LoginActivityBackup self;
    private Session session;
    private String kakaouserid;
    private String phone;
    private int session_openCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ButterKnife.bind(this);
        join_btn = (TextView)findViewById(R.id.join_btn);
        find_auth_btn = (TextView)findViewById(R.id.find_auth_btn);
        email_sign_in_button = (Button)findViewById(R.id.email_sign_in_button);
        login_btn_facebook = (Button)findViewById(R.id.login_btn_facebook);
        login_btn_kakao = (Button)findViewById(R.id.login_btn_kakao);

        login_btn_facebook.setOnClickListener(this);
        login_btn_kakao.setOnClickListener(this);
        email_sign_in_button.setOnClickListener(this);
        join_btn.setOnClickListener(this);
        find_auth_btn.setOnClickListener(this);


        preferenceManager = new PreferenceManager(this);
        dlg = new ProgressDialog(this,ProgressDialog.STYLE_HORIZONTAL);
//        if(!preferenceManager.getLoggedIn_Info().getAccesstoken().equals("")){
//            VersoinChecker versoinChecker = new VersoinChecker(this);
//            versoinChecker.check();
//        }
        
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        checkPermission();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //progressWaitDaialog = new ProgressWaitDaialog(this);
        //self = this;
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
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED){

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "앱 내의 컨텐츠 저장용으로 사용됩니다.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE
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
            if(apply_cnt==permissions.length)
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
            //일반회원 로그인 로직
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
    @Override
    protected void onStop() {
        super.onStop();
        dlg.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dlg.dismiss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.join_btn:
                Intent intent = new Intent(LoginActivityBackup.this,JoinActivity.class);
                this.startActivity(intent);
                break;
            case R.id.find_auth_btn:
                final CharSequence[] items = {getString(R.string.find_id_title_text), getString(R.string.find_pass_title_text)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setItems(items, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int index){
                                switch (index){
                                    case 0:
                                        Intent intent = new Intent(LoginActivityBackup.this,FindIdActivity.class);
                                        LoginActivityBackup.this.startActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(LoginActivityBackup.this,FindPassActivity.class);
                                        LoginActivityBackup.this.startActivity(intent);
                                        break;
                                }
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
                break;
            case R.id.email_sign_in_button:
                try {
                    attemptLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.login_btn_facebook:
                break;
            case R.id.login_btn_kakao:
                //kakaoLogin();
                break;
        }

    }
/*
    private void kakaoLogin() {
        mKakaoCallback = new ISessionCallback() {


            @Override
            public void onSessionOpened() {
                Log.e(TAG,"KAKAO Session Open");
                if(session_openCount==0){
                    session_openCount = 1;
                    String kakaoAccessToken = session.getAccessToken();
                    kakaoRequestMe();
                }

            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Log.e(TAG,exception+"");
            }
        };
        session = getCurrentSession();
        session.addCallback(mKakaoCallback);
        session.checkAndImplicitOpen();
        session.open(AuthType.KAKAO_LOGIN_ALL, this);
    }

    private void kakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback(){

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                Log.e(TAG,userProfile.toString());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    SubscriptionManager telephonyMgr = (SubscriptionManager) getSystemService (Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                    Log.d(TAG,telephonyMgr.getActiveSubscriptionInfoList().get(0).getNumber());
                    phone=telephonyMgr.getActiveSubscriptionInfoList().get(0).getNumber();
                    kakaouserid = String.valueOf(userProfile.getId());
                }else
                {
                    TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    Log.d("LoginActivity!!",telephonyMgr.getLine1Number());
                    phone=telephonyMgr.getLine1Number();
                    kakaouserid = String.valueOf(userProfile.getId());
                }

                request_Duplicate_Check_id(kakaouserid, LoggedInCase.KAKAOLogin.getLogin_case());

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG,errorResult.getErrorMessage());
            }

            @Override
            public void onNotSignedUp() {

            }
        });
    }

    private void request_Duplicate_Check_id(final String user_input_id, final String login_case) {
        String path = RequestPath.req_url_id_check.getPath();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.ID.getKey(),user_input_id);
        //progressWaitDaialog.show();
        StampRestClient.post(path,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG,response.toString());
                //progressWaitDaialog.dismiss();
                try {
                    String result_msg = response.getString(ResponseKey.MESSAGE.getKey());
                    String result_code = response.getString(ResponseKey.CODE.getKey());
                    String result_data = response.getString(ResponseKey.RESULTDATA.getKey());

                    if(result_code.equals(ResponseCode.SUCCESS.getCode())&&result_msg.equals(ResponseMsg.SUCCESS.getMessage())){
                        if(result_data.equals(ResponseMsg.DUPLICATE.getMessage())){
                            //already join kakao user
                            SocialUser user = new SocialUser(user_input_id,login_case,self);
                            user.LoggedIn(user_input_id);
                        }else{
                            //need join kakao user
                            Toast.makeText(self,getResources().getString(R.string.join_normal_valid_email),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(self, SocialJoinActivity.class);
                            intent.putExtra("id",user_input_id);
                            intent.putExtra("LoggedIncase",LoggedInCase.KAKAOLogin.getLogin_case());
                            self.startActivityForResult(intent,STARTSOCIALJOIN);
                        }

                    }else{
                        Toast.makeText(self,getResources().getString(R.string.join_normal_duplicate_email),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(self,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,errorResponse.toString());
                //progressWaitDaialog.dismiss();
                Toast.makeText(self,getResources().getString(R.string.server_not_good),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        session_openCount = 0;
        if(resultCode == CANCLEJOINSOCIALUSER){
            return;
        }else if(resultCode == JOINSOCIALUSER){
            Toast.makeText(self,"로그인 해주세요",Toast.LENGTH_LONG).show();
        }
    }*/
}

