package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.viewholder.ViewHolder;

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
public class ListViewAdapter extends BaseAdapter {
    private Context mcontext = null;
    private ArrayList<TownDTO> mListData = new ArrayList<TownDTO>();

    public ListViewAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }
    public void additem(TownDTO townDTO){
        mListData.add(townDTO);
    }
    public void remove(int position){
        mListData.remove(position);
        dataChange();
    }

    private void dataChange() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.townlistitem,null);
            holder.town_img_view = (ImageView)convertView.findViewById(R.id.town_img_view);
            holder.name_text_view = (TextView)convertView.findViewById(R.id.town_name_view);
            holder.region_text_view = (TextView)convertView.findViewById(R.id.town_region_view);
            holder.distance_text_view = (TextView)convertView.findViewById(R.id.town_distance_view);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
        String no;
        if(position<9){
//            if(position == 9) no = ""+(position+1);
//            else no = "0"+(position+1);
            no = "0"+(position+1);
        }else{
            no = position+1+"";
        }
        Log.e("GetView no",no);
        String dirPath = sdcard+"/StampTour_kyj/contents/contents_test/img_list_heap_"+no+"@2x.png";
        Log.e("ListAdapter",dirPath);
        File img = new File(dirPath);
        TownDTO townDTO = mListData.get(position);
        Glide.with(holder.town_img_view.getContext())
                .load(img)
                .centerCrop()
                .into(holder.town_img_view);
        holder.name_text_view.setText(townDTO.getName());
        holder.region_text_view.setText(townDTO.getRegion());
        holder.distance_text_view.setText(townDTO.getDistance());
        return convertView;
    }
}
