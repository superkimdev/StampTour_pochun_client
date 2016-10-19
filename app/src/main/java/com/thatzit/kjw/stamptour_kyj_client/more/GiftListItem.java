package com.thatzit.kjw.stamptour_kyj_client.more;

/**
 * Created by csc-pc on 2016. 10. 19..
 */

public class GiftListItem {
    private String grade;
    private int achieve;
    private int state;

    //state code
    // 00 : normal
    // 01 : active
    // 02 : complete

    public GiftListItem(String grade, int achieve, int state) {
        this.grade = grade;
        this.achieve = achieve;
        this.state = state;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getAchieve() {
        return achieve;
    }

    public void setAchieve(int achieve) {
        this.achieve = achieve;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
