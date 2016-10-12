package com.thatzit.kjw.stamptour_kyj_client.main.event;

/**
 * Created by kjw on 2016. 10. 13..
 */

public class ListChangeEvent {
    public boolean change_status;

    public ListChangeEvent(boolean change_status) {
        this.change_status = change_status;
    }

    public boolean isChange_status() {
        return change_status;
    }
}
