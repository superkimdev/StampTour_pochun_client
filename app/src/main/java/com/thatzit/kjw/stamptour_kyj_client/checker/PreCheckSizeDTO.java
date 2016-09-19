package com.thatzit.kjw.stamptour_kyj_client.checker;

/**
 * Created by kjw on 16. 9. 19..
 */
public class PreCheckSizeDTO {
    private String nonformatting;
    private String formatting;

    public PreCheckSizeDTO(String nonformatting, String formatting) {
        this.nonformatting = nonformatting;
        this.formatting = formatting;
    }

    public String getNonformatting() {
        return nonformatting;
    }

    public String getFormatting() {
        return formatting;
    }

    @Override
    public String toString() {
        return "PreCheckSizeDTO{" +
                "nonformatting='" + nonformatting + '\'' +
                ", formatting='" + formatting + '\'' +
                '}';
    }
}
