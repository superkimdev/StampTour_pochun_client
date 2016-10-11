package com.thatzit.kjw.stamptour_kyj_client.http;

/**
 * Created by kjw on 16. 8. 21..
 */
public enum ResponseKey {
    CODE("code"),
    RESULTDATA("resultData"),
    MESSAGE("message"),
    TOKEN("accesstoken"),
    DEVICETOKEN("devicetoken"),
    NICK("nick"),
    PASSWORD("password");
    private String key;

    ResponseKey() {
    }

    ResponseKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
