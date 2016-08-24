package com.thatzit.kjw.stamptour_kyj_client.checker;

/**
 * Created by kjw on 16. 8. 24..
 */
public interface DownLoad {
    public void download(final String nick,final String accesstoken);
    public void downloadAndLoggedin(final String nick,final String accesstoken);
}
