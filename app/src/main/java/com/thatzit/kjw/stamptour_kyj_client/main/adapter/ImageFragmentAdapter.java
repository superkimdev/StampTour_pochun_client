package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.ImageFragment;
import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by kjw on 16. 9. 17..
 */
public class ImageFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private int mCount = CONTENT.length;
    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
    protected static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location
    };

    public ImageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
