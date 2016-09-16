package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.viewholder.PopupViewHolder;

import java.util.ArrayList;

/**
 * Created by kjw on 16. 9. 17..
 */
public class PopUpAdapter extends BaseAdapter {
    private ArrayList<String> mlist;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayout;

    public PopUpAdapter(ArrayList<String> mlist,int layout,Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.mLayout = layout;
        this.mInflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMlist(ArrayList<String> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public String getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopupViewHolder popupViewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(mLayout, parent, false);
            popupViewHolder = new PopupViewHolder();
            popupViewHolder.action_name = (TextView) convertView.findViewById(R.id.popup_item_action);
            convertView.setTag(popupViewHolder);

        }else
        {
            popupViewHolder = (PopupViewHolder) convertView.getTag();
        }

        popupViewHolder.action_name.setText(getItem(position));
        return convertView;
    }
    class ViewHolder{
        public TextView action_name = null;
    }

}
