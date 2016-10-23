package com.thatzit.kjw.stamptour_kyj_client.more;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.main.TermsActivity;

import java.util.ArrayList;

/**
 * Created by csc-pc on 2016. 10. 14..
 */

public class GiftRecyclerViewAdapter extends RecyclerView.Adapter<GiftRecyclerViewAdapter.ViewHolder>
{
    private int GIFT_GO_BACK_CODE = 900;
    private Context context;
    private int stampCount;
    private ArrayList<GiftListItem> mItems;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public GiftRecyclerViewAdapter(ArrayList<GiftListItem> items,int stampCount, Context mContext)
    {
        this.mItems = items;
        this.stampCount = stampCount;
        this.context = mContext;
    }

    public GiftRecyclerViewAdapter(Context mContext)
    {
        this.mItems = new ArrayList<>();
        this.stampCount = 0;
        this.context = mContext;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //holder.imageView.setImageResource(mItems.get(position).image);
        //holder.textView.setText(mItems.get(position).imagetitle);
        holder.title.setText(mItems.get(position).getGrade());
        switch (mItems.get(position).getState()){
            case 00:
                holder.subtitle.setText(context.getString(R.string.gift_grade_msg_normal_pre)+
                        (mItems.get(position).getAchieve()-stampCount)+
                        context.getString(R.string.gift_grade_msg_normal_aft));
                holder.gift_btn.setVisibility(View.GONE);
                holder.mainframe.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 01:
                holder.subtitle.setText(context.getString(R.string.gift_grade_msg_active));
                holder.gift_btn.setVisibility(View.VISIBLE);
                holder.mainframe.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 02:
                holder.subtitle.setText(context.getString(R.string.gift_grade_msg_complete));
                holder.gift_btn.setVisibility(View.GONE);
                holder.mainframe.setBackgroundColor(Color.parseColor("#f6f6f6"));
                break;
            default:
                holder.subtitle.setText(context.getString(R.string.server_not_good));
                holder.gift_btn.setVisibility(View.GONE);
                break;
        }


        holder.gift_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GiftActivity.class);
                intent.putExtra(ResponseKey.GRADE.getKey(),mItems.get(position).getGrade());
                intent.putExtra(ResponseKey.MYSTAMPCOUNT.getKey(),String.valueOf(mItems.get(position).getAchieve()));
                context.startActivity(intent);
            }
        });

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
        public LinearLayout mainframe;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_gift_manger_main_text);
            subtitle = (TextView) view.findViewById(R.id.item_gift_manger_sub_text);
            gift_btn = (LinearLayout) view.findViewById(R.id.gift_manage_gift_btn_linearlayout);
            mainframe = (LinearLayout) view.findViewById(R.id.item_gift_manager_mainframe_linearlayout);
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

    public void setItems(ArrayList<GiftListItem> items){
        this.mItems = items;
        this.notifyDataSetChanged();
    }

    public void setStampCount(int count){
        this.stampCount = count;
    }

}
