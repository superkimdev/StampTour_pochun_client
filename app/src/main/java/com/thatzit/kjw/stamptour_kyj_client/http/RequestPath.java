package com.thatzit.kjw.stamptour_kyj_client.http;

/**
 * Created by kjw on 2016. 10. 11..
 */

public enum  RequestPath {
    req_url_loggedin("user/login"),
    req_url_loggedout("user/logout"),
    req_url_user_check_version("user/check/version"),
    req_url_download_zip("town/contents/contents.zip"),
    req_url_regist_device("user/device/regist"),
    req_url_push_test("user/push/test"),
    req_url_town_list("town/list"),
    req_url_current_stamp("user/current/stamp"),
    req_url_user_rank("user/rank"),
    req_url_user_remove("user/remove"),
    req_url_stamp_check("stamp/check"),
    req_url_password_chnage("user/change/password"),
    req_url_user_info("user/info"),
    req_url_id_check("/user/id/check"),
    req_url_nick_check("/user/nick/check"),
    req_url_join_normal("/user/join"),
    req_url_find_id("/user/find/id"),
    req_url_find_pass("/user/find/password"),
    req_url_survey_apply("/user/survey/apply"),
    req_url_survey("/user/survey"),
    req_url_gift_aply("/user/current/gift/apply"),
    req_url_gift_list("/user/current/gift");


    private String path;
    private RequestPath(){

    }

    RequestPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
