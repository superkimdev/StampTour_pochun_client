package com.thatzit.kjw.stamptour_kyj_client.checker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;

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
    private ExternalMemoryDTO external_memory;
    private VersionDTO version;
    private Context context;
    private Decompress decompressor;
    private PreferenceManager preferenceManager;
    public int value;
    public ProgressDialog dlg;
    public UsableStorageChecker usableStorageChecker;
    public VersoinChecker(Context context) {
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
        this.usableStorageChecker = new UsableStorageChecker();
        external_memory = usableStorageChecker.check_ext_memory();
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
                            long usable_size = Long.parseLong(external_memory.getExternal_usable_nonformatting_size());
                            long target_contents_size = version.getSize();

                            if(target_contents_size>usable_size){
                                //storage size less
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                String needs_string = context.getString(R.string.needsmemory);
                                needs_string = needs_string + usableStorageChecker.formatSize(target_contents_size);
                                String usable_string = context.getString(R.string.usablememory);
                                usable_string = usable_string + external_memory.getExternal_usable_formatting_size();
                                builder.setTitle(R.string.dialog_storage_less)
                                        .setMessage(needs_string+"\n"+usable_string)
                                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // FIRE ZE MISSILES!
                                                dialog.dismiss();
                                                if(context.getClass().getName().contains("LoginActivity")){
                                                    preferenceManager.user_LoggedOut();
                                                }else if (context.getClass().getName().contains("SplashActivity")){
                                                    preferenceManager.user_LoggedOut();
                                                    Intent intent = new Intent(context, LoginActivity.class);
                                                    ((SplashActivity)context).startActivity(intent);
                                                    ((SplashActivity)context).finish();
                                                }
                                            }
                                        });
                                // Create the AlertDialog object
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                return;
                            }
                            preferenceManager.setVersion(version);
                            preferenceManager.setDownFlag(true);
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

    public String typeCheckDownLoad(){
        if(context.getClass().getName().contains("LoginActivity")){
            return "LoginActivity";
        }
        else if(context.getClass().getName().contains("SplashActivity")){
            return "SplashActivity";
        }
        return null;
    }
    @Override
    public void downloadAndLoggedin(final String nick,final String accesstoken) {
        RequestParams params = new RequestParams();
        params.put("nick",nick);
        params.put("accesstoken",accesstoken);
        String contents_down_url = context.getString(R.string.req_url_download_zip);
        final int filesize = preferenceManager.getVersion().getSize();
        Log.e("download",nick+":"+accesstoken);

        dlg = new ProgressDialog(context,ProgressDialog.STYLE_HORIZONTAL);
        dlg.setProgress(0);
        dlg.setMessage(context.getString(R.string.downloding_string));
        dlg.setCancelable(false);
        dlg.show();

        StampRestClient.get(contents_down_url,params,new FileAsyncHttpResponseHandler(context){

            private String createDirectory(){
                String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
                String dirPath = sdcard+"/StampTour_kyj/download";
                File dir = new File(dirPath);
                if( !dir.exists() ) dir.mkdirs();
                else {
                    int result = deleteDir(dirPath);
                    dir.mkdirs();
                }
                return dirPath;
            }
            private String createunzipDirectory(){
                String sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();
                String dirPath = sdcard+"/StampTour_kyj/contents/";
                File dir = new File(dirPath);
                if( !dir.exists() ) dir.mkdirs();
                else {
                    int result = deleteDir(dirPath);
                    dir.mkdirs();
                }
                return dirPath;
            }
            public int deleteDir(String a_path){
                File file = new File(a_path);
                if(file.exists()){
                    File[] childFileList = file.listFiles();
                    for(File childFile : childFileList){
                        if(childFile.isDirectory()){
                            deleteDir(childFile.getAbsolutePath());
                        }
                        else{
                            Log.e("DeleteFile",childFile.getAbsolutePath());
                            childFile.delete();
                        }
                    }
                    file.delete();
                    return 1;
                }else{
                    return 0;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.e("filedown","fail");
                preferenceManager.setVersion(new VersionDTO(0,0));
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
                value = (int) (bytesWritten*100/filesize);
                Log.e("DownLoad value",value+"");
                dlg.setProgress(value);

            }
        });
        return;
    }

}
