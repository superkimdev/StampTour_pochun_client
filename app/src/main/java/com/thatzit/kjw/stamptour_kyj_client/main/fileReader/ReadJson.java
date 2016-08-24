package com.thatzit.kjw.stamptour_kyj_client.main.fileReader;

import android.os.Environment;

import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.TownJson;

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
    public ReadJson() {
        this.mListData = new ArrayList<TownJson>();
    }

    public ArrayList<TownJson> ReadFile (){
        try {
            File yourFile = new File(Environment.getExternalStorageDirectory(), "StampTour_kyj/contents/contents_test/kr.json");
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
//            JSONObject jsonObj = new JSONObject(jsonStr);
//
//            // Getting data JSON Array nodes
//            JSONArray data  = jsonObj.getJSONArray("data");
                JSONArray data = new JSONArray(jsonStr);
            // looping through All nodes
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                String no = c.getString("번호");
                String name = c.getString("명소명");
                String region = c.getString("권역명");
                String lat = c.getString("위도");
                String lon = c.getString("경도");
                String range = c.getString("반경");
                String subtitle = c.getString("경도");
                String contents = c.getString("반경");
                //use >  int id = c.getInt("duration"); if you want get an int


                // tmp hashmap for single node

                mListData.add(new TownJson(no,name,region,lat,lon,range,subtitle,contents));

                // do what do you want on your interface
            }
            return mListData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mListData;
    }
}
