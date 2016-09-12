package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.thatzit.kjw.stamptour_kyj_client.checker.LocaleChecker;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;
import com.thatzit.kjw.stamptour_kyj_client.main.event.ReadEvent;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.ReadEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kjw on 16. 8. 25..
 */
public class ReadJson {
    private ArrayList<TownJson> mListData;
    private LocaleChecker checker;
    private Context context;
    private String AppDir;
    private ReadEventListener readEventListener;
    public final int READSTART = 1;
    public final int READEND = 2;
    public static ArrayList<TownJson> memCashList;
    public ReadJson(Context context) {

        this.mListData = new ArrayList<TownJson>();
        this.context = context;
        this.checker = new LocaleChecker(context);
    }

    public ArrayList<TownJson> ReadFile (){
        try {
            //시스템설정에 따라 파일 변경(kr.json, jp.json, eng.json, ch.json, etc...)
            checker.check();
            String locale=checker.check_return_locale();
            if(locale.equals("")){
                //Can't read System locale value
                //Error
                return mListData;
            }else{
                //read System locale value
                //case by set dir for json parsing
                //default AppDir value = "StampTour_kyj/contents/contents_test/kr.json"
               AppDir = getPath(locale);
            }
            Log.e("ReadJson-ReadFile","AppDir : "+AppDir);
            File yourFile = new File(Environment.getExternalStorageDirectory(), AppDir);
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                stream.close();
            }
                JSONArray data = new JSONArray(jsonStr);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                String no = c.getString("번호");
                String name = c.getString("명소명");
                String region = c.getString("권역명");
                String lat = c.getString("위도");
                String lon = c.getString("경도");
                String range = c.getString("반경");
                String subtitle = c.getString("서브타이틀");
                String contents = c.getString("소개내용");

                mListData.add(new TownJson(no,name,region,lat,lon,range,subtitle,contents));

                // do what do you want on your interface
            }
            memCashList = mListData;
            Log.e("Data Cashing","Call");
            return mListData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mListData;
    }

    private String getPath(String locale) {
        switch (locale){
            case "ko": return "StampTour_kyj/contents/contents/kr.json";
            case "en": return "StampTour_kyj/contents/contents/eng.json";
            default: return "StampTour_kyj/contents/contents/kr.json";
        }
    }
    public void setReadEventListener(ReadEventListener readEventListener){
        this.readEventListener = readEventListener;
    }
}
