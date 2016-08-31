package com.thatzit.kjw.stamptour_kyj_client.checker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.checker.action.Check;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.splash.SplashActivity;
import com.thatzit.kjw.stamptour_kyj_client.util.Decompress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 8. 24..
 */
public class VersoinChecker implements Check,DownLoad{
    private VersionDTO version;
    private Context context;
    private Decompress decompressor;
    private PreferenceManager preferenceManager;
    public VersoinChecker(Context context) {
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
    }

    public VersoinChecker(VersionDTO version, Context context) {
        this.version = version;
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
    }
    public void typeCheck_dlg(Context context,boolean show){
        if(context.getClass().getName().contains("LoginActivity")){
            ((LoginActivity)context).showCheckDialog(show);
        }
        else if(context.getClass().getName().contains("SplashActivity")){
            ((SplashActivity)context).showCheckDialog(show);
        }
    }
    @Override
    public void check() {
        final String nick = preferenceManager.getLoggedIn_Info().getNick();
        final String accesstoken = preferenceManager.getLoggedIn_Info().getAccesstoken();
        VersionDTO lastversion = preferenceManager.getVersion();
        Log.e("CHECK VERSION ",lastversion.getVersion()+":"+lastversion.getSize());
        typeCheck_dlg(context,true);
        RequestParams params = new RequestParams();
        params.put("nick",nick);
        params.put("accesstoken",accesstoken);
        params.put("lastversion",lastversion.getVersion());
        params.put("lastsize",lastversion.getSize());
        String version_check_url = context.getString(R.string.req_url_user_check_version);
        StampRestClient.post(version_check_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("ClassName",context.getClass().getName());
                typeCheck_dlg(context,false);
                String code = null;
                String msg = null;
                JSONObject resultData = null;
                try{
                    code = response.getString(ResponseKey.CODE.getKey());
                    msg = response.getString(ResponseKey.MESSAGE.getKey());
                    Log.e("CheckResponse",code+":"+msg);
                    if(code.equals(ResponseCode.SUCCESS.getCode())&&msg.equals(ResponseMsg.SUCCESS.getMessage())){
                        resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                        VersionDTO version = new VersionDTO(resultData.getInt("Version"),resultData.getInt("Size"));
                        Log.e("Version Response",version.getVersion()+":"+version.getSize());
                        if(version.getVersion() ==-1 && version.getSize() == -1){
                            if(preferenceManager.getLoggedIn_Info().getAccesstoken()!=""){
                                if(context.getClass().getName().contains("LoginActivity")){
                                    Intent intent = new Intent(context, MainActivity.class);
                                    ((LoginActivity)context).startActivity(intent);
                                    ((LoginActivity)context).finish();
                                }else if (context.getClass().getName().contains("SplashActivity")){
                                    Intent intent = new Intent(context, MainActivity.class);
                                    ((SplashActivity)context).startActivity(intent);
                                    ((SplashActivity)context).finish();
                                }

                            }
                            else{
                                Intent intent = new Intent(context, LoginActivity.class);
                                ((SplashActivity)context).startActivity(intent);
                                ((SplashActivity)context).finish();

                            }
                        }else{
                            Log.e("Down?","down");
                            preferenceManager.setVersion(version);
                            downloadAndLoggedin(nick,accesstoken);
                        }

                        Log.e("VersionChecker",preferenceManager.getVersion().getVersion()+":"+preferenceManager.getVersion().getSize());
                    }else if(code.equals(ResponseCode.NOTENOUGHDATA.getCode())&&msg.equals(ResponseMsg.INVALIDACCESSTOKEN.getMessage())){
                        Log.e("invlid Accsestoken",context.getClass().getName());
                        if(context.getClass().getName().contains("LoginActivity")){
//                            Intent intent = new Intent(context, MainActivity.class);
//                            ((LoginActivity)context).startActivity(intent);
//                            ((LoginActivity)context).finish();
                        }else if (context.getClass().getName().contains("SplashActivity")){
                            Intent intent = new Intent(context, LoginActivity.class);
                            ((SplashActivity)context).startActivity(intent);
                            ((SplashActivity)context).finish();
                        }
                    }
                }catch (JSONException e){
                    Log.e("VersionChecker",e.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                typeCheck_dlg(context,false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                typeCheck_dlg(context,false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                typeCheck_dlg(context,false);
            }
        });
    }

    @Override
    public void download(final String nick, final String accesstoken) {
        return;
    }

    @Override
    public void downloadAndLoggedin(final String nick,final String accesstoken) {
        RequestParams params = new RequestParams();
        params.put("nick",nick);
        params.put("accesstoken",accesstoken);
        String contents_down_url = context.getString(R.string.req_url_download_zip);
        final int filesize = preferenceManager.getVersion().getSize();
        Log.e("download",nick+":"+accesstoken);
        final ProgressDialog dlg = new ProgressDialog(this.context,ProgressDialog.STYLE_HORIZONTAL);
        dlg.setProgress(0);
        dlg.setMessage("필요한 컨텐츠 다운로드중...");
        dlg.setCancelable(false);
        dlg.show();

        StampRestClient.get(contents_down_url,params,new FileAsyncHttpResponseHandler(context){
            private String createDirectory(){
                String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
                String dirPath = sdcard+"/StampTour_kyj/download";
                File dir = new File(dirPath);
                if( !dir.exists() ) dir.mkdirs();
                return dirPath;
            }
            private String createunzipDirectory(){
                String sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();
                String dirPath = sdcard+"/StampTour_kyj/contents/";
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
                File zipFile= new File(path,"contents.zip");

                try{
                    byte[] buffer = new byte[1024];
                    FileInputStream in = new FileInputStream(file);

                    int len;
                    FileOutputStream writer = new FileOutputStream(zipFile);

                    while ((len = in.read(buffer)) > 0) {
                        writer.write(buffer, 0, len);
                    }
                    writer.close();
                    decompressor = new Decompress(zipFile.getAbsolutePath(),createunzipDirectory(),context);
                    decompressor.execute();

                    preferenceManager.setFirstStart();

                }catch (FileNotFoundException e) {
                    Log.e("WriteFile", e.toString());
                }catch (IOException e){
                    Log.e("WriteFile",e.toString());
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int value = (int) (bytesWritten*100/filesize);
                dlg.setProgress(value);

            }
        });
        return;
    }
}
