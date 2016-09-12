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
    public MainRecyclerAdapter(Context context) {
        this.context = context;

    }
    public MainRecyclerAdapter(Context context, ArrayList<TownDTO> mListData) {
        this.context = context;
        this.mListData = mListData;
    }
    public MainRecyclerAdapter(ArrayList<TownDTO> mtownlist) {
        this.mListData = mtownlist;
    }
    public void additem(TownDTO data){
        this.mListData.add(data);
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
            if(position<9){
                no = "0"+(position+1);
            }else{
                no = position+1+"";
            }
            Log.e("onBindViewHolder",position+":"+mListData.get(position).getName());
            Log.e("onBindViewHolder",position+":"+mListData.get(position).getRegion());
            ((NormalViewHolder)viewHolder).name_text_view.setText(mListData.get(position).getName());
            ((NormalViewHolder)viewHolder).region_text_view.setText(mListData.get(position).getRegion());
            String dirPath = sdcard+"/StampTour_kyj/contents/contents/img_list_heap_"+no+"@2x.png";
            Log.e("ListAdapter",dirPath);
            File img = new File(dirPath);
            Glide.with(((NormalViewHolder)viewHolder).town_img_view.getContext())
                    .load(img)
                    .centerCrop()
                    .into(((NormalViewHolder)viewHolder).town_img_view);
            if(mListData.get(position).isStamp_on()){
                ((NormalViewHolder)viewHolder).item_container.setBackground(
                        context.getResources().getDrawable(R.drawable.town_list_item_bg));
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
        public NormalViewHolder(View itemView) {
            super(itemView);
            town_img_view = (ImageView)itemView.findViewById(R.id.town_img_view);
            name_text_view = (TextView)itemView.findViewById(R.id.town_name_view);
            region_text_view = (TextView)itemView.findViewById(R.id.town_region_view);
            distance_text_view = (TextView)itemView.findViewById(R.id.town_distance_view);
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
