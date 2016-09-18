package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 9. 19..
 */
public class TempTownDTO {
    private String town_code;
    private String nick;
    private String checktime;

    public TempTownDTO(String town_code, String nick, String checktime) {
        this.town_code = town_code;
        this.nick = nick;
        this.checktime = checktime;
    }

    public String getTown_code() {
        return town_code;
    }

    public String getNick() {
        return nick;
    }

    public String getChecktime() {
        return checktime;
    }
}
