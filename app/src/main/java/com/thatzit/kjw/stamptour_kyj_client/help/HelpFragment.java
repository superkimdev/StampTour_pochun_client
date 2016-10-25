package com.thatzit.kjw.stamptour_kyj_client.help;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thatzit.kjw.stamptour_kyj_client.R;


/**
 * Created by csc-pc on 2016. 4. 29..
 */
public class HelpFragment extends Fragment {
    private static final String EXTRA_MESSAGE = "msg";
    private View view;
    private Context context;
    String message;

    public static final HelpFragment newInstance(String message){
        HelpFragment f = new HelpFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        message = getArguments().getString(EXTRA_MESSAGE);
        view = inflater.inflate(R.layout.fragment_help_viewpager, container, false);
        ImageView img = (ImageView)view.findViewById(R.id.fragment_help_imageview);

        Glide.with(img.getContext())
                .load(getImg(message)) //message 에서 url 받아서 출력한다
                .centerCrop()
                .into(img);

        return view;
    }

    private int getImg(String no){
        int imgNo = Integer.parseInt(no);
        switch (imgNo){
            case 1:
                return R.drawable.store_01;
            case 2:
                return R.drawable.store_02;
            case 3:
                return R.drawable.store_03;
            case 4:
                return R.drawable.store_04;
            default:
                return R.drawable.store_01;
        }

    }
}
