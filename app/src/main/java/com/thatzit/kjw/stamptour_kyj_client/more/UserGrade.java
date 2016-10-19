package com.thatzit.kjw.stamptour_kyj_client.more;

/**
 * Created by csc-pc on 2016. 10. 19..
 */

public class UserGrade {
    private String gradeName;
    private int achieve_count;

    public UserGrade(String gradeName, int achieve_count) {
        this.gradeName = gradeName;
        this.achieve_count = achieve_count;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getAchieve_count() {
        return achieve_count;
    }

    public void setAchieve_count(int achieve_count) {
        this.achieve_count = achieve_count;
    }
}
