package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 25..
 */
public class TownDTO {
    private String name;
    private String region;
    private String distance;
    private String checked;
    public TownDTO() {
    }

    public TownDTO(String name, String region, String distance, String checked) {
        this.name = name;
        this.region = region;
        this.distance = distance;
        this.checked = checked;
    }

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
