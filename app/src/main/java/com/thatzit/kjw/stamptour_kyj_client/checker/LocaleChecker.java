package com.thatzit.kjw.stamptour_kyj_client.checker;

import android.content.Context;
import android.util.Log;

import com.thatzit.kjw.stamptour_kyj_client.checker.action.Check;
import com.thatzit.kjw.stamptour_kyj_client.checker.action.Check_return_locale;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;

/**
 * Created by kjw on 16. 8. 30..
 */
public class LocaleChecker implements Check,Check_return_locale {
    private PreferenceManager preferenceManager;
    private Context context;

    public LocaleChecker(Context context) {
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    @Override
    public void check() {
        String locale = context.getResources().getConfiguration().locale.getLanguage();
        preferenceManager.setLocale(locale);
        Log.e("LocaleChecker-check",locale);

    }

    @Override
    public String check_return_locale() {
        return preferenceManager.getLocale();
    }
}
