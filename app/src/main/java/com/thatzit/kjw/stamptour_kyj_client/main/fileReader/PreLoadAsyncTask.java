package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by kjw on 16. 9. 19..
 */
public class PreLoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private ReadJson readJson;

    public PreLoadAsyncTask(Context context) {
        this.context = context;
        this.readJson = new ReadJson(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        readJson.ReadFile();
        return null;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
