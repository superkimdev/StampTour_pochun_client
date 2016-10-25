package com.thatzit.kjw.stamptour_kyj_client.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by csc-pc on 2016. 4. 29..
 */
public class HelpViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public HelpViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
