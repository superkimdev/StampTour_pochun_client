package com.thatzit.kjw.stamptour_kyj_client.login;

/**
 * Created by kjw on 16. 8. 20..
 */
public enum LoggedInCase {
    NORMAL("NORMAL"),
    FBLogin("FBLogin"),
    KAKAOLogin("KAKAOLogin");
    private String Login_case;

    private LoggedInCase() {
    }

    private LoggedInCase(String login_case) {
        Login_case = login_case;
    }

    public String getLogin_case() {
        return Login_case;
    }
}
