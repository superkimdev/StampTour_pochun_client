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
    RANKLIST("ranklist"),
    SURVEY_POINT("survey_point"),
    MYCOUNT("myCount"),
    ALLGRADE("allGrade"),
    AGOGIFT("agoGift"),
    GRADE("grade"),
    CHECKTIME("CheckTime"),
    REALNAME("realName"),
    PHONE("phone"),
    CONTENTS("contents"),
    LOGGEDINCASE("loggedincase");
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
