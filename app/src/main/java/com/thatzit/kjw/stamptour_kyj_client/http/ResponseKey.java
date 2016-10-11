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
    NICKHIGH("Nick"),
    PASSWORD("password"),
    PAGE("pageno"),
    ID("id"),
    MYRANK("myrank"),
    RANK("rank"),
    MYSTAMPCOUNT("stamp_count"),
    TOTAL("total"),
    RANKLIST("ranklist");
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
