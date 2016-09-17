package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.ImageFragmentAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by kjw on 16. 9. 17..
 */
public class DetailActivity extends AppCompatActivity{
    private int town_code;
    private LinearLayout pager_indicator;
    private ImageFragmentAdapter mAdapter;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        town_code = getTownCode();
        mAdapter = new ImageFragmentAdapter(getFragmentManager(),town_code);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(mAdapter.getCount() - 1);



    }

    private int getTownCode() {
        Intent intent = getIntent();
        return intent.getIntExtra("town",0);
    }

}
