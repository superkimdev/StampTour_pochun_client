package com.thatzit.kjw.stamptour_kyj_client.main.msgListener;

import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;

/**
 * Created by kjw on 16. 9. 15..
 */
public interface ParentGpsStateListener {
    public void OnReceivedParentStateEvent(GpsStateEvent event);
}
