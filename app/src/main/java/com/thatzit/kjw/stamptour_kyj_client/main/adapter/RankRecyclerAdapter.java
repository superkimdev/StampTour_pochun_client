package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.checker.LocaleChecker;
import com.thatzit.kjw.stamptour_kyj_client.main.TownDTO;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by kjw on 2016. 10. 12..
 */
public class RankRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RankRecyclerAdapter";
    Context context;
    private LocaleChecker checker;
    private ArrayList<Object> mListData = new ArrayList<Object>();
    private final int HEADER = 0;
    private final int NORMAL = 2;
    public RankRecyclerAdapter(Context context) {
        this.context = context;
        this.checker = new LocaleChecker(context);
    }

    public RankRecyclerAdapter(Context context, ArrayList<Object> mListData) {
        this.context = context;
        this.mListData = mListData;
        this.checker = new LocaleChecker(context);
    }
    public Object getmListData(int position) {
        return mListData.get(position);
    }
    public void additem(Object data){
        this.mListData.add(data);
    }
    public void removeitem(int position){
        this.mListData.remove(position);
    }
    public void removelist(){
        this.mListData.clear();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG,viewType+"");
        if(HEADER == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranklistheader, parent, false);
            return new HeaderViewHolder(view);
        }else if(NORMAL == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranklistitem, parent, false);
            return new NormalViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid ViewType: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RankRecyclerAdapter.NormalViewHolder){
            Log.e("NormalViewHolder","position : "+position);
            RankDTO dto = (RankDTO) mListData.get(position);
            Log.e(TAG,dto.toString());
            ((RankRecyclerAdapter.NormalViewHolder)holder).rank_no_textview.setText("");
            ((RankRecyclerAdapter.NormalViewHolder)holder).name_text_view.setText("");
            ((RankRecyclerAdapter.NormalViewHolder)holder).user_stamp_cnt_textview.setText("");

            ((RankRecyclerAdapter.NormalViewHolder)holder).rank_no_textview.setText(dto.getRank_no());
            ((RankRecyclerAdapter.NormalViewHolder)holder).name_text_view.setText(dto.getName());
            ((RankRecyclerAdapter.NormalViewHolder)holder).user_stamp_cnt_textview.setText(dto.getStamp_cnt());
            //animation and imgview 잔상 초기화

        }else if(holder instanceof RankRecyclerAdapter.HeaderViewHolder){
            RankHeaderDTO dto = (RankHeaderDTO) mListData.get(position);
            Log.e("HeaderViewHolder","position : "+position);
            Log.e(TAG,dto.toString());
            ((RankRecyclerAdapter.HeaderViewHolder)holder).my_name_textview.setText("");
            ((RankRecyclerAdapter.HeaderViewHolder)holder).my_rank_textview.setText("");

            setRankTextWithLocale((HeaderViewHolder) holder, dto);

        }
    }

    private void setRankTextWithLocale(HeaderViewHolder holder, RankHeaderDTO dto) {
        switch (checker.check_return_locale()){
            case "ko":
                holder.my_name_textview.setText(dto.getName()+context.getString(R.string.beverb_string)+dto.getTotal()+context.getString(R.string.out_of_string));
                holder.my_rank_textview.setText(dto.getRank_no()+context.getString(R.string.rank_string));
                break;
            case "en":
                holder.my_name_textview.setText(dto.getName()+context.getString(R.string.beverb_string)+dto.getRank_no()+context.getString(R.string.out_of_string));
                holder.my_rank_textview.setText(dto.getTotal()+context.getString(R.string.rank_string));
                break;
            default:
                //defalut ko
                holder.my_name_textview.setText(dto.getName()+context.getString(R.string.beverb_string)+dto.getTotal()+context.getString(R.string.out_of_string));
                holder.my_rank_textview.setText(dto.getRank_no()+context.getString(R.string.rank_string));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }
    private boolean isPositionHeader(int position) {
        return position == HEADER;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return HEADER;
        } else {
            return NORMAL;
        }
    }


    class NormalViewHolder extends RecyclerView.ViewHolder{
        public TextView rank_no_textview;
        public TextView name_text_view;
        public TextView user_stamp_cnt_textview;
        public RelativeLayout item_container;
        public NormalViewHolder(View itemView) {
            super(itemView);
            rank_no_textview = (TextView)itemView.findViewById(R.id.rank_no_textview);
            name_text_view = (TextView)itemView.findViewById(R.id.name_text_view);
            user_stamp_cnt_textview = (TextView)itemView.findViewById(R.id.user_stamp_cnt_textview);
            item_container = (RelativeLayout)itemView.findViewById(R.id.item_container);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView my_name_textview;
        public TextView my_rank_textview;
        public TextView rank_finish_textview;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            my_name_textview = (TextView) itemView.findViewById(R.id.my_name_textview);
            my_rank_textview = (TextView) itemView.findViewById(R.id.my_rank_textview);
            rank_finish_textview = (TextView) itemView.findViewById(R.id.rank_finish_textview);

        }
    }
}