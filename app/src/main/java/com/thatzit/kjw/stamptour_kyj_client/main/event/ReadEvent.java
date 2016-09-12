package com.thatzit.kjw.stamptour_kyj_client.main.event;

/**
 * Created by kjw on 16. 9. 13..
 */
public class ReadEvent {
    private int state;
    public final int READSTART = 1;
    public final int READEND = 2;
    public ReadEvent(int state) {
        this.state = state;
    }

    public int isState() {
        return state;
    }
}
