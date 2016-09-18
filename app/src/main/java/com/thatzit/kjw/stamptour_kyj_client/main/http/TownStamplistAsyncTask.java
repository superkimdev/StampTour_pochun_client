package com.thatzit.kjw.stamptour_kyj_client.main.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

/**
 * Created by kjw on 16. 9. 18..
 */
public class TownStamplistAsyncTask extends AsyncTask<Void, Void, Void> {


    private Context mContext;
    private PreferenceManager preferenceManager;
    private ProgressBar progressBar;
    private LoggedInInfo user;
    private ProgressWaitDaialog progressWaitDaialog;
    public TownStamplistAsyncTask(Context mContext) {
        this.mContext = mContext;
        this.preferenceManager = new PreferenceManager(mContext);
        this.progressWaitDaialog = new ProgressWaitDaialog(mContext);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        user = preferenceManager.getLoggedIn_Info();

        progressWaitDaialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(int i = 0 ; i < 100000000 ; i++){
            for(int j = 0 ; j < 100 ; j++){

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressWaitDaialog.dismiss();
    }
}
