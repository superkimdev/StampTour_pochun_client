package com.thatzit.kjw.stamptour_kyj_client.preference;

/**
 * Created by kjw on 16. 8. 22..
 */
public class LoggedInInfo {
    private String nick;
    private String accesstoken;
    private String loggedincase;

    public LoggedInInfo(String nick, String accesstoken, String loggedincase) {
        this.nick = nick;
        this.accesstoken = accesstoken;
        this.loggedincase = loggedincase;
    }

    public String getNick() {
        return nick;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public String getLoggedincase() {
        return loggedincase;
    }
}
