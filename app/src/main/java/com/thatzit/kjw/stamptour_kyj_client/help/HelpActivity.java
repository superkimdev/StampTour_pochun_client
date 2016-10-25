package com.thatzit.kjw.stamptour_kyj_client.help;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.login.LoginActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;


public class HelpActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager pager;
    private CirclePageIndicator indicator;
    private ImageView close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setLayout();
    }

    private void setLayout() {
        pager = (ViewPager)findViewById(R.id.help_viewpager);
        indicator = (CirclePageIndicator)findViewById(R.id.help_indicator);
        close = (ImageView)findViewById(R.id.help_close_imageview);

        close.setOnClickListener(this);
        List<Fragment> listData = new ArrayList<>();
        listData.add(new HelpFragment().newInstance("1"));
        listData.add(new HelpFragment().newInstance("2"));
        listData.add(new HelpFragment().newInstance("3"));
        listData.add(new HelpFragment().newInstance("4"));
        /*istData.add(new HelpFragment().newInstance("5"));
        listData.add(new HelpFragment().newInstance("6"));*/

        pager.setAdapter(new HelpViewPagerAdapter(getSupportFragmentManager(), listData));
        indicator.setViewPager(pager);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.help_close_imageview:
                finish();
                break;
        }
    }
}
