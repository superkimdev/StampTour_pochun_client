package com.thatzit.kjw.stamptour_kyj_client.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.ImageFragmentAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;
import com.viewpagerindicator.CirclePageIndicator;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by kjw on 16. 9. 17..
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private int town_code;
    private LinearLayout pager_indicator;
    private ImageFragmentAdapter mAdapter;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private Toolbar toolbar;
    private ImageView backBtn;
    private TextView title;
    private TextView subtitle;
    private TextView contents;
    private TownJson town;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        town_code = getTownCode();
        toolbar = (Toolbar) findViewById(R.id.image_fragment_toolbar);
        backBtn = (ImageView)findViewById(R.id.detail_toolbar_back_button);
        title = (TextView)findViewById(R.id.detail_town_title_textview);
        subtitle = (TextView)findViewById(R.id.image_description_subtitle);
        contents = (TextView)findViewById(R.id.image_description_contents);
        setTownData_to_view();

        mAdapter = new ImageFragmentAdapter(getFragmentManager(),town_code);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(0);
        backBtn.setOnClickListener(this);

    }

    private void setTownData_to_view() {
        town = ReadJson.memCashList.get(town_code-1);
        title.setText(town.getName());
        subtitle.setText(town.getSubtitle());
        contents.setText(town.getContents());
    }

    private int getTownCode() {
        Intent intent = getIntent();
        return intent.getIntExtra("town",0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.detail_toolbar_back_button){
            finish();
        }
    }
}
