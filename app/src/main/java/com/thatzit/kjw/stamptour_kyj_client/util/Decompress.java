package com.thatzit.kjw.stamptour_kyj_client.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.thatzit.kjw.stamptour_kyj_client.main.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by kjw on 16. 8. 22..
 */
public class Decompress extends AsyncTask<Void, Void, Void> {
    private String _zipFile;
    private String _location;
    private Context context;
    private ProgressDialog dlg;
    public Decompress(String zipFile, String location,Context context) {
        _zipFile = zipFile;
        _location = location;
        this.context = context;
        _dirChecker("");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dlg = new ProgressDialog(this.context,ProgressDialog.STYLE_HORIZONTAL);
        dlg.setProgress(0);
        dlg.setMessage("컨텐츠 압축해제중...");
        dlg.setCancelable(false);
        dlg.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dlg.dismiss();
        Intent intent = new Intent(context, MainActivity.class);
        ((LoginActivity)context).startActivity(intent);
        ((LoginActivity)context).finish();
    }

    @Override
    protected Void doInBackground(Void... params) {
        unzip();
        return null;
    }

    private void unzip() {

        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
