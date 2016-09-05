package com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener;

/**
 * Created by kjw on 16. 9. 3..
 */
public class PushMessageEvent {
    private String lecture_id;
    private String title;
    private String desc;
    private String param1;
    private String param2;

    public PushMessageEvent(String lecture_id, String title, String desc, String param1, String param2) {
        this.lecture_id = lecture_id;
        this.title = title;
        this.desc = desc;
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getLecture_id() {
        return lecture_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    @Override
    public String toString() {
        return "PushMessageEvent{" +
                "lecture_id='" + lecture_id + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                '}';
    }
}
