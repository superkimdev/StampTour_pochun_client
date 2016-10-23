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
    private String rank_no;
    private boolean stamp_on;

    public boolean isStamp_on() {
        return stamp_on;
    }

    public void setStamp_on(boolean stamp_on) {
        this.stamp_on = stamp_on;
    }

    public TownDTO() {
    }

    public TownDTO(String no,String name, String region, String distance, String range,String stamp_checked,String rank_no,boolean stamp_on) {
        this.no = no;
        this.name = name;
        this.region = region;
        this.distance = distance;
        this.range = range;
        this.stamp_checked = stamp_checked;
        this.stamp_on = stamp_on;
        this.rank_no = rank_no;
//        2016-10-21 06:27:17
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//        try {
//            this.stamp_checked= String.valueOf(sdf.parse(stamp_checked).toLocaleString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


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

    public String getRank_no() {
        return rank_no;
    }

    @Override
    public String toString() {
        return "TownDTO{" +
                "no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", distance='" + distance + '\'' +
                ", range='" + range + '\'' +
                ", stamp_checked='" + stamp_checked + '\'' +
                ", rank_no='" + rank_no + '\'' +
                ", stamp_on=" + stamp_on +
                '}';
    }

}
