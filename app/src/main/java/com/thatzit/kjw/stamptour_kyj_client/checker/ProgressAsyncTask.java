package com.thatzit.kjw.stamptour_kyj_client.checker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.Decompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kjw on 16. 9. 6..
 */
public class ProgressAsyncTask extends AsyncTask<Void,Integer,Void> {
    private Context context;
    private PreferenceManager preferenceManager;
    private int value;
    private String contents_down_url;
    public ProgressDialog dlg;
    private Decompress decompressor;
    private String nick;
    private String accesstoken;
    private int filesize;
    public ProgressAsyncTask(Context context, PreferenceManager preferenceManager,String nick,String accesstoken) {
        this.context = context;
        this.preferenceManager = preferenceManager;
        this.contents_down_url = StampRestClient.BASE_URL+context.getString(R.string.req_url_download_zip);
        this.nick = nick;
        this.accesstoken = accesstoken;
        filesize = preferenceManager.getVersion().getSize();
        this.dlg = new ProgressDialog(context,ProgressDialog.STYLE_HORIZONTAL);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dlg.setProgress(0);
        dlg.setMax(100);
        dlg.setMessage("필요한 컨텐츠 다운로드중...");
        dlg.setCancelable(false);
        dlg.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dlg.setProgress(values[0]);
    }

    @Override
    protected Void doInBackground(Void... params) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("nick",nick);
        requestParams.put("accesstoken",accesstoken);
        AsyncHttpClient client = StampRestClient.getClient();
        client.get(contents_down_url,requestParams,new FileAsyncHttpResponseHandler(context){

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
                value = (int) (bytesWritten*100/filesize);
                Log.e("DownLoad value",value+"");
                publishProgress(value);

            }
        });
        return null;
    }
}
