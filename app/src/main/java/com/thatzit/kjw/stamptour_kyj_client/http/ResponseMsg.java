package com.thatzit.kjw.stamptour_kyj_client.http;

/**
 * Created by kjw on 16. 8. 21..
 */
public enum ResponseMsg {
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    EMPTY(""),
    DUPLICATE("duplicate"),
    INVALIDPASSWORD("invalid password"),
    INVALIDID("invalid id"),
    INVALIDACCESSTOKEN("invalid accesstoken"),
    INVALIDNICK("invalid nick"),
    UPDATEFAILPASSWORD("update fail password");
    private String message;

    ResponseMsg() {
    }

    ResponseMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
