package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thatzit.kjw.stamptour_kyj_client.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 14-04-2015.
 */
public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.VersionViewHolder> {
    private ArrayList<TownDTO> mListData = new ArrayList<TownDTO>();
    Context context;
    OnItemClickListener clickListener;

    public SimpleRecyclerAdapter(Context context) {
        this.context = context;

    }
    public SimpleRecyclerAdapter(Context context,ArrayList<TownDTO> mListData) {
        this.context = context;
        this.mListData = mListData;
    }

    public SimpleRecyclerAdapter(ArrayList<TownDTO> mtownlist) {
        this.mListData = mtownlist;
    }
    public void additem(TownDTO data){
        this.mListData.add(data);
    }
    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.townlistitem, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {
        String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
        String no;
        if(i<9){
            no = "0"+(i+1);
        }else{
            no = i+1+"";
        }
        Log.e("onBindViewHolder",i+":"+mListData.get(i).getName());
        Log.e("onBindViewHolder",i+":"+mListData.get(i).getRegion());
        versionViewHolder.name_text_view.setText(mListData.get(i).getName());
        versionViewHolder.region_text_view.setText(mListData.get(i).getRegion());
        String dirPath = sdcard+"/StampTour_kyj/contents/contents_test/img_list_heap_"+no+"@2x.png";
        Log.e("ListAdapter",dirPath);
        File img = new File(dirPath);
        Glide.with(versionViewHolder.town_img_view.getContext())
                .load(img)
                .centerCrop()
                .into(versionViewHolder.town_img_view);
    }

    @Override
    public int getItemCount() {
            return mListData == null ? 0 : mListData.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView town_img_view;
        public TextView name_text_view;
        public TextView region_text_view;
        public TextView distance_text_view;

        public VersionViewHolder(View itemView) {
            super(itemView);
            town_img_view = (ImageView)itemView.findViewById(R.id.town_img_view);
            name_text_view = (TextView)itemView.findViewById(R.id.town_name_view);
            region_text_view = (TextView)itemView.findViewById(R.id.town_region_view);
            distance_text_view = (TextView)itemView.findViewById(R.id.town_distance_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
