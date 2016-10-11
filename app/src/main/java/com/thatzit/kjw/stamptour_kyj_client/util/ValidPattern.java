package com.thatzit.kjw.stamptour_kyj_client.util;

/**
 * Created by kjw on 2016. 10. 11..
 */

public class ValidPattern {
    private static final String pass_patten = "^(?=.*[a-z]+)(?=.*[0-9]+).{6,16}$";
    private static final String email_patten = "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+";
    private static final String nick_patten = "^[a-zA-Z0-9가-힣\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]{2,6}+$";

    public ValidPattern() {
    }

    public static String getPass_patten() {
        return pass_patten;
    }

    public static String getEmail_patten() {
        return email_patten;
    }

    public static String getNick_patten() {
        return nick_patten;
    }
}
