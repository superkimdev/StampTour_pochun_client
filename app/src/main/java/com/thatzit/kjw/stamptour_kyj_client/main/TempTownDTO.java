package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 9. 19..
 */
public class TempTownDTO {
    private String town_code;
    private String nick;
    private String checktime;
    private String region;
    private String rank_no;
    public TempTownDTO(String town_code, String nick, String checktime,String region,String rank_no) {
        this.town_code = town_code;
        this.nick = nick;
        this.checktime = checktime;
        this.region = region;
        this.rank_no = rank_no;
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
    public String getRegion() {
        return region;
    }

    public String getRank_no() {
        return rank_no;
    }

    @Override
    public String toString() {
        return "TempTownDTO{" +
                "town_code='" + town_code + '\'' +
                ", nick='" + nick + '\'' +
                ", checktime='" + checktime + '\'' +
                ", region='" + region + '\'' +
                ", rank_no='" + rank_no + '\'' +
                '}';
    }
}
