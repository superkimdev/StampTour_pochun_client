package com.thatzit.kjw.stamptour_kyj_client.main.msgListener;

import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;

/**
 * Created by kjw on 16. 9. 15..
 */
public interface ParentLocationListener {
    public void OnReceivedLocation(LocationEvent locationEvent);
}
