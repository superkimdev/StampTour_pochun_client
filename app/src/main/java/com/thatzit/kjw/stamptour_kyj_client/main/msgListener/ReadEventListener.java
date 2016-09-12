package com.thatzit.kjw.stamptour_kyj_client.main.msgListener;

import com.thatzit.kjw.stamptour_kyj_client.main.event.ReadEvent;

/**
 * Created by kjw on 16. 9. 13..
 */
public interface ReadEventListener {
    public void OnReceivedReadEvent(ReadEvent readEvent);
}
