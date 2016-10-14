package com.thatzit.kjw.stamptour_kyj_client.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

public class MyApplication extends Application {

	static Context mContext;
	private static Activity currentActivity;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		KakaoSDK.init(new KakaoSDKAdapter());
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
	}

	public static Context getContext() {
		return mContext;
	}
	private static class KakaoSDKAdapter extends KakaoAdapter {
		public KakaoSDKAdapter() {
			super();
		}

		@Override
		public ISessionConfig getSessionConfig() {
			return new ISessionConfig() {
				@Override
				public AuthType[] getAuthTypes() {
					return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
				}

				@Override
				public boolean isUsingWebviewTimer() {
					return false;
				}


				@Override
				public ApprovalType getApprovalType() {
					return ApprovalType.INDIVIDUAL;
				}

				@Override
				public boolean isSaveFormData() {
					return true;
				}
			};
		}

		@Override
		public IApplicationConfig getApplicationConfig() {
			return new IApplicationConfig() {
				@Override
				public Activity getTopActivity() {
					return MyApplication.getCurrentActivity();
				}

				@Override
				public Context getApplicationContext() {
					return MyApplication.getContext();
				}
			};
		}


	}
	public static Activity getCurrentActivity() {
		return currentActivity;
	}

	// Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
	public static void setCurrentActivity(Activity currentActivity) {
		MyApplication.currentActivity = currentActivity;
	}
}
