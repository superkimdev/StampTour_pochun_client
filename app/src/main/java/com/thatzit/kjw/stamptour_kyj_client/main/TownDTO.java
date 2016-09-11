package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 25..
 */
public class TownDTO {
    private String no;
    private String name;
    private String region;
    private String distance;
    private String checked;
    private boolean stamp_on;
    public TownDTO() {
    }

    public TownDTO(String no,String name, String region, String distance, String checked) {
        this.no = no;
        this.name = name;
        this.region = region;
        this.distance = distance;
        this.checked = checked;
        this.stamp_on = false;
    }

    public boolean isStamp_on() {
        return stamp_on;
    }

    public void setStamp_on(boolean stamp_on) {
        this.stamp_on = stamp_on;
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

    public String getChecked() {
        return checked;
    }
}
