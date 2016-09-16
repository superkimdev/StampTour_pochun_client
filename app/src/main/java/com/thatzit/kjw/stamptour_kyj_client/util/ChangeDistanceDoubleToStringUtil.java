package com.thatzit.kjw.stamptour_kyj_client.util;

import java.text.DecimalFormat;

public class ChangeDistanceDoubleToStringUtil {

	public static String onChangeDistanceDoubleToString(double distance) {

		String distanceStr = "";
		String kmStr = "";
		String mStr = "";

		int km = (int) distance / 1000;
		int m = (int) distance % 1000;

		if (km <= 0) {
			kmStr = "";
		} else {
			DecimalFormat df = new DecimalFormat("#,###");
			kmStr = df.format(km) + " km ";
		}

		mStr = m + " m ";

		distanceStr = kmStr + mStr;

		return distanceStr;
	}
}
