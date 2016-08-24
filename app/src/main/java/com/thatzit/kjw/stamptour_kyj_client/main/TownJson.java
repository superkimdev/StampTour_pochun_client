package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 25..
 */
public class TownJson {
    private String no;
    private String name;
    private String region;
    private String lat;
    private String lon;
    private String range;
    private String subtitle;
    private String contents;

    public TownJson(String no, String name, String region, String lat, String lon, String range, String subtitle, String contents) {
        this.no = no;
        this.name = name;
        this.region = region;
        this.lat = lat;
        this.lon = lon;
        this.range = range;
        this.subtitle = subtitle;
        this.contents = contents;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getRange() {
        return range;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getContents() {
        return contents;
    }
}
