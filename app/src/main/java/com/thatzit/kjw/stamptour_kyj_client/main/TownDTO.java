package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 25..
 */
public class TownDTO {
    private String no;
    private String name;
    private String region;
    private String distance;
    private String range;
    private String stamp_checked;
    private boolean stamp_on;

    public boolean isStamp_on() {
        return stamp_on;
    }

    public void setStamp_on(boolean stamp_on) {
        this.stamp_on = stamp_on;
    }

    public TownDTO() {
    }

    public TownDTO(String no,String name, String region, String distance, String range,String stamp_checked,boolean stamp_on) {
        this.no = no;
        this.name = name;
        this.region = region;
        this.distance = distance;
        this.range = range;
        this.stamp_checked = stamp_checked;
        this.stamp_on = stamp_on;
    }

    public String getStamp_checked() {
        return stamp_checked;
    }

    public void setStamp_checked(String stamp_checked) {
        this.stamp_checked = stamp_checked;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNo() { return no; }
    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getDistance() {
        return distance;
    }

    public String getRange() {
        return range;
    }
}
