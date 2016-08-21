package com.thatzit.kjw.stamptour_kyj_client.http;

/**
 * Created by kjw on 16. 8. 21..
 */
public enum ResponseCode {
    SUCCESS("00"),
    NOTENOUGHDATA("01"),
    EMPTYDATA("02"),
    DBERR("03");
    private String code;
    private ResponseCode(){

    }

    ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
