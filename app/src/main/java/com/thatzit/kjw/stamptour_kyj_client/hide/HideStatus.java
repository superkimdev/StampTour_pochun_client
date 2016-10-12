package com.thatzit.kjw.stamptour_kyj_client.hide;

/**
 * Created by kjw on 2016. 10. 12..
 */

public enum  HideStatus {
    UNSET(-1),
    UNHIDE(1),
    HIDE(2);
    private int status;

    HideStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
