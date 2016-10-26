package com.thatzit.kjw.stamptour_kyj_client.preference;

/**
 * Created by kjw on 16. 8. 21..
 */
public enum PreferenceKey {
    FIRST("first"),
    NICK("nick"),
    VERSION("version"),
    SIZE("size"),
    ACCESSTOKEN("accesstoken"),
    LOGGEDINCASE("loggedincase"),
    LOCALE("locale"),
    GCMTOKEN("gcmtoken"),
    DOWNFLAG("downflag"),
    AGO_NOTIFICATION_TOWN("ago_notification_town"),
    AGO_IS_STAMPON("ago_is_stampon");
    private String key;

    PreferenceKey() {
    }

    PreferenceKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
