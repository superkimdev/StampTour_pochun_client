package com.thatzit.kjw.stamptour_kyj_client.more;

import java.util.ArrayList;

/**
 * Created by csc-pc on 2016. 10. 19..
 */

public class GiftVO {

    private int stampCount;
    private ArrayList<UserAchieve> achieves;
    private ArrayList<UserGrade> grades;

    public GiftVO(int stampCount, ArrayList<UserAchieve> achieves, ArrayList<UserGrade> grades) {
        this.stampCount = stampCount;
        this.achieves = achieves;
        this.grades = grades;
    }

    public int getStampCount() {
        return stampCount;
    }

    public void setStampCount(int stampCount) {
        this.stampCount = stampCount;
    }

    public ArrayList<UserAchieve> getAchieves() {
        return achieves;
    }

    public void setAchieves(ArrayList<UserAchieve> achieves) {
        this.achieves = achieves;
    }

    public ArrayList<UserGrade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<UserGrade> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "GiftVO{" +
                "grades=" + grades +
                ", achieves=" + achieves +
                ", stampCount=" + stampCount +
                '}';
    }
}
