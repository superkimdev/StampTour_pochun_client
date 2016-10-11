package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

/**
 * Created by kjw on 2016. 10. 12..
 */
public class RankDTO {
    String name;
    String rank_no;
    String stamp_cnt;

    public RankDTO(String name, String rank_no, String stamp_cnt) {
        this.name = name;
        this.rank_no = rank_no;
        this.stamp_cnt = stamp_cnt;
    }

    public String getName() {
        return name;
    }

    public String getRank_no() {
        return rank_no;
    }

    public String getStamp_cnt() {
        return stamp_cnt;
    }

    @Override
    public String toString() {
        return "RankDTO{" +
                "name='" + name + '\'' +
                ", rank_no='" + rank_no + '\'' +
                ", stamp_cnt='" + stamp_cnt + '\'' +
                '}';
    }
}
