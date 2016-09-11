package com.thatzit.kjw.stamptour_kyj_client.util;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}

	public static Context getContext() {
		return mContext;
	}
}
