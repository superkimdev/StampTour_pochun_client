package com.thatzit.kjw.stamptour_kyj_client.more;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thatzit.kjw.stamptour_kyj_client.R;

import java.util.ArrayList;

/**
 * Created by csc-pc on 2016. 10. 14..
 */

public class GiftRecyclerViewAdapter extends RecyclerView.Adapter<GiftRecyclerViewAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<GiftDTO> mItems;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public GiftRecyclerViewAdapter(ArrayList<GiftDTO> items, Context mContext)
    {
        mItems = items;
        context = mContext;
    }

    // 필수로 Generate 되어야 하는 메소드 1 : 새로운 뷰 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 새로운 뷰를 만든다
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_gift_manger,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    // 필수로 Generate 되어야 하는 메소드 2 : ListView의 getView 부분을 담당하는 메소드
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //holder.imageView.setImageResource(mItems.get(position).image);
        //holder.textView.setText(mItems.get(position).imagetitle);
        holder.title.setText(mItems.get(position).getTitle());
        holder.subtitle.setText(mItems.get(position).getSubtitle());
        holder.gift_btn.setVisibility(View.GONE);
      /*  if(mItems.get(position).getState().equals("0")) {
            holder.subtitle.setText(mItems.get(position).getSubtitle());
            holder.gift_btn.setVisibility(View.GONE);
        }else if(mItems.get(position).getState().equals("1")) {
            holder.subtitle.setText(mItems.get(position).getSubtitle());
            holder.gift_btn.setVisibility(View.VISIBLE);
        }else {
            holder.subtitle.setText(mItems.get(position).getSubtitle());
            holder.gift_btn.setVisibility(View.GONE);
        }*/


    }

    // // 필수로 Generate 되어야 하는 메소드 3
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView subtitle;
        public LinearLayout gift_btn;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_gift_manger_main_text);
            subtitle = (TextView) view.findViewById(R.id.item_gift_manger_sub_text);
            gift_btn = (LinearLayout) view.findViewById(R.id.gift_manage_gift_btn_linearlayout);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // 새로 보여지는 뷰라면 애니메이션을 해줍니다
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
