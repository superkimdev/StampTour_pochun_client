package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

/**
 * Created by kjw on 16. 8. 22..
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thatzit.kjw.stamptour_kyj_client.main.MainFragment;
import com.thatzit.kjw.stamptour_kyj_client.main.MapFragment;
import com.thatzit.kjw.stamptour_kyj_client.main.RankingFragment;
import com.thatzit.kjw.stamptour_kyj_client.main.MoreFragment;

public class MainPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MainFragment tab1 = new MainFragment();
                return tab1;
            case 1:
                MapFragment tab2 = new MapFragment();
                return tab2;
            case 2:
                RankingFragment tab3 = new RankingFragment();
                return tab3;
            case 3:
                MoreFragment tab4 = new MoreFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}