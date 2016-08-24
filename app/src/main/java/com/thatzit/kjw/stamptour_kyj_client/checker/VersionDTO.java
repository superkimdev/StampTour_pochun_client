package com.thatzit.kjw.stamptour_kyj_client.checker;

/**
 * Created by kjw on 16. 8. 24..
 */
public class VersionDTO {
    private int version;
    private int size;

    public VersionDTO() {
    }

    public VersionDTO(int version, int size) {
        this.version = version;
        this.size = size;
    }

    public int getVersion() {
        return version;
    }

    public int getSize() {
        return size;
    }
}
