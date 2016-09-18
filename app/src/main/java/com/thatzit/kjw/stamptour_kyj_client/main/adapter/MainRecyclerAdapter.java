package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Suleiman on 14-04-2015.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TownDTO> mListData = new ArrayList<TownDTO>();
    Context context;
    OnItemClickListener clickListener;
    OnItemLongClickListener longClickListener;
    private final String TAG ="MainRecyclerAdapter";
    public MainRecyclerAdapter(Context context) {
        this.context = context;

    }
    public MainRecyclerAdapter(Context context, ArrayList<TownDTO> mListData) {
        this.context = context;
        this.mListData = mListData;
    }

    public TownDTO getmListData(int position) {
        return mListData.get(position);
    }

    public MainRecyclerAdapter(ArrayList<TownDTO> mtownlist) {
        this.mListData = mtownlist;
    }
    public void additem(TownDTO data){
        this.mListData.add(data);
    }
    public void removeitem(int position){
        this.mListData.remove(position);
    }
    public void removelist(){
        for(int i = mListData.size()-1 ;i>=0 ;i--)this.removeitem(i);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.townlistitem, viewGroup, false);
            return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof NormalViewHolder){
            String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
            String no;
            no = position + 1+"";
            Log.e(TAG,position+":"+mListData.get(position).getName());
            Log.e(TAG,position+":"+mListData.get(position).getRegion());
            Log.e(TAG,position+":"+mListData.get(position).getDistance());
            ((NormalViewHolder)viewHolder).name_text_view.setText(mListData.get(position).getName());
            ((NormalViewHolder)viewHolder).distance_text_view.setText(mListData.get(position).getDistance());
            ((NormalViewHolder)viewHolder).region_text_view.setText(mListData.get(position).getRegion());
            String dirPath = sdcard+"/StampTour_kyj/contents/contents/town"+no+"_1.png";
            Log.e("ListAdapter",dirPath);
            File img = new File(dirPath);
            Glide.with(((NormalViewHolder)viewHolder).town_img_view.getContext())
                    .load(img)
                    .centerCrop()
                    .into(((NormalViewHolder)viewHolder).town_img_view);
            if(!mListData.get(position).getStamp_checked().equals("")){
                ((NormalViewHolder)viewHolder).stamp_checked_imgview.setVisibility(View.VISIBLE);
            }
            if(mListData.get(position).isStamp_on()){
                if(!mListData.get(position).getStamp_checked().equals("")){
                    ((NormalViewHolder)viewHolder).item_container.setBackground(
                            context.getResources().getDrawable(R.drawable.town_list_item_bg));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
            return mListData == null ? 0 : mListData.size();
    }

    private TownDTO getItem(int position) {
        return mListData.get(position);
    }
    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public ImageView town_img_view;
        public TextView name_text_view;
        public TextView region_text_view;
        public TextView distance_text_view;
        public RelativeLayout item_container;
        public ImageView stamp_checked_imgview;
        public NormalViewHolder(View itemView) {
            super(itemView);
            town_img_view = (ImageView)itemView.findViewById(R.id.town_img_view);
            name_text_view = (TextView)itemView.findViewById(R.id.town_name_view);
            region_text_view = (TextView)itemView.findViewById(R.id.town_region_view);
            distance_text_view = (TextView)itemView.findViewById(R.id.town_distance_view);
            stamp_checked_imgview = (ImageView)itemView.findViewById(R.id.stamp_checked_imgview);
            item_container = (RelativeLayout)itemView.findViewById(R.id.item_container);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onItemLongClick(v, getPosition());
            return true;
        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public void SetOnItemLongClickListener(final OnItemLongClickListener itemLongClickListener) {
        this.longClickListener = itemLongClickListener;
    }
}
