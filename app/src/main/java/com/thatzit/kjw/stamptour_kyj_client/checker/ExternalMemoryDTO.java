package com.thatzit.kjw.stamptour_kyj_client.checker;

/**
 * Created by kjw on 16. 9. 19..
 */
public class ExternalMemoryDTO {
    private String external_usable_formatting_size;
    private String external_usable_nonformatting_size;
    private String external_total_formatting_size;
    private String external_total_nonformatting_size;

    public ExternalMemoryDTO(String external_usable_formatting_size, String external_usable_nonformatting_size, String external_total_formatting_size, String external_total_nonformatting_size) {
        this.external_usable_formatting_size = external_usable_formatting_size;
        this.external_usable_nonformatting_size = external_usable_nonformatting_size;
        this.external_total_formatting_size = external_total_formatting_size;
        this.external_total_nonformatting_size = external_total_nonformatting_size;
    }

    public String getExternal_usable_formatting_size() {
        return external_usable_formatting_size;
    }

    public String getExternal_usable_nonformatting_size() {
        return external_usable_nonformatting_size;
    }

    public String getExternal_total_formatting_size() {
        return external_total_formatting_size;
    }

    public String getExternal_total_nonformatting_size() {
        return external_total_nonformatting_size;
    }

    @Override
    public String toString() {
        return "ExternalMemoryDTO{" +
                "external_usable_formatting_size='" + external_usable_formatting_size + '\'' +
                ", external_usable_nonformatting_size='" + external_usable_nonformatting_size + '\'' +
                ", external_total_formatting_size='" + external_total_formatting_size + '\'' +
                ", external_total_nonformatting_size='" + external_total_nonformatting_size + '\'' +
                '}';
    }
}
