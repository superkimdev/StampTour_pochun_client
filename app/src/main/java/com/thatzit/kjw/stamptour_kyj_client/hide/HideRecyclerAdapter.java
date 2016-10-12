package com.thatzit.kjw.stamptour_kyj_client.hide;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kjw on 2016. 10. 12..
 */
public class HideRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<TownDTO> mListData = new ArrayList<TownDTO>();
    Context context;
    MainRecyclerAdapter.OnItemClickListener clickListener;
    MainRecyclerAdapter.OnItemLongClickListener longClickListener;
    private final String TAG ="HideRecyclerAdapter";

    public HideRecyclerAdapter(Context context) {
        this.context = context;
    }

    public HideRecyclerAdapter(ArrayList<TownDTO> mListData, Context context) {
        this.mListData = mListData;
        this.context = context;
    }
    public TownDTO getmListData(int position) {
        return mListData.get(position);
    }
    public void additem(TownDTO data){
        this.mListData.add(data);
    }
    public void removelist(){
        this.mListData.clear();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hidetownlistitem, viewGroup, false);
        return new HideRecyclerAdapter.NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof HideRecyclerAdapter.NormalViewHolder){
            String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
            String no;
            no = position + 1+"";
            Log.e(TAG,position+":"+mListData.get(position).getName());
            Log.e(TAG,position+":"+mListData.get(position).getRegion());
            Log.e(TAG,position+":"+mListData.get(position).getDistance());
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).name_text_view.setText("");
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).distance_text_view.setText("");
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).region_text_view.setText("");
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).name_text_view.setText(mListData.get(position).getName());
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).distance_text_view.setText(mListData.get(position).getDistance());
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).region_text_view.setText(mListData.get(position).getRegion());
            //animation and imgview 잔상 초기화
            ((HideRecyclerAdapter.NormalViewHolder)viewHolder).stamp_checked_imgview.setVisibility(View.INVISIBLE);
            String dirPath = sdcard+"/StampTour_kyj/contents/contents/town"+no+"_1.png";
            Log.e("ListAdapter",dirPath);
            File img = new File(dirPath);
            Glide.with(((HideRecyclerAdapter.NormalViewHolder)viewHolder).town_img_view.getContext())
                    .load(img)
                    .centerCrop()
                    .into(((HideRecyclerAdapter.NormalViewHolder)viewHolder).town_img_view);
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
    public void SetOnItemClickListener(final MainRecyclerAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public void SetOnItemLongClickListener(final MainRecyclerAdapter.OnItemLongClickListener itemLongClickListener) {
        this.longClickListener = itemLongClickListener;
    }
}
