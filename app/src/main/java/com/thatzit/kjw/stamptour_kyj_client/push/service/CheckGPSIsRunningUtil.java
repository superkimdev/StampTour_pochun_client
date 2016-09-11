package com.thatzit.kjw.stamptour_kyj_client.push.service;

import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;

/**
 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * <pre>
 * Gps & Network State Checker
 *
 * </pre>
 */
public class CheckGPSIsRunningUtil {

	/**
	 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
	 * <pre>
	 * gps & network state check
	 * all or notthing
	 * </pre>
	 * @return true or false
	 */
	@SuppressWarnings("deprecation")
	public static boolean onCheckGPSIsRunning() {
		String gps = android.provider.Settings.Secure.getString(MyApplication.getContext().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
			return false;
		} else {
			return true;
		}
	}
}
