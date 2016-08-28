package com.thatzit.kjw.stamptour_kyj_client.help;

/**
 * Created by kjw on 16. 8. 22..
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment1;
import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment2;
import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment3;
import com.thatzit.kjw.stamptour_kyj_client.main.TabFragment4;

public class HelpPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HelpPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
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