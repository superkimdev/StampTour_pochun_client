package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thatzit.kjw.stamptour_kyj_client.main.MainFragment;
import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment2;
import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment3;
import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment4;

/**
 * Created by kjw on 16. 9. 17..
 */
public class DetailPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DetailPageAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MainFragment tab1 = new MainFragment();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            case 3:
                TabFragment4 tab4 = new TabFragment4();
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
