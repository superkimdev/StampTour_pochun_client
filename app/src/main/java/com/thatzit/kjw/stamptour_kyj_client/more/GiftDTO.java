package com.thatzit.kjw.stamptour_kyj_client.more;

/**
 * Created by csc-pc on 2016. 10. 14..
 */

public class GiftDTO {

    /*"next_stamp_count": 3,
            "stamp_count": 2,
            "grade": "초급자",
            "nick": "김지운"*/
    private String title;
    private String subtitle;
    private String state;

    public GiftDTO(String title, String subtitle, String state) {
        this.title = title;
        this.subtitle = subtitle;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
