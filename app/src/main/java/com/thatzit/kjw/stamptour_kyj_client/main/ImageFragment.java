package com.thatzit.kjw.stamptour_kyj_client.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thatzit.kjw.stamptour_kyj_client.R;

import java.io.File;

/**
 * Created by kjw on 16. 9. 17..
 */
public class ImageFragment extends android.app.Fragment{
    private static final String KEY_CONTENT = "ImageFragment:Content";
    private String mContent = "???";
    private View view;
    private ImageView item;
    public static android.app.Fragment newInstance(String content) {
        ImageFragment fragment = new ImageFragment();
        fragment.mContent = content;
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.image_fragment, container, false);
        setLayout();
        return view;
    }

    private void setLayout() {
        item = (ImageView) view.findViewById(R.id.pager_img);
        File img = new File(mContent);
        Glide.with(item.getContext())
                .load(img)
                .centerCrop()
                .into(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
