package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Environment;
import android.support.v13.app.FragmentPagerAdapter;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.ImageFragment;
import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by kjw on 16. 9. 17..
 */
public class ImageFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private final String sdcard;
    private final String no;
    private final String dirPath;
    private int mCount = CONTENT.length;
    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
    protected static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location
    };
    private int town_code;
    public ImageFragmentAdapter(FragmentManager fm, int town_code) {
        super(fm);
        this.town_code = town_code;
        sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
        if(town_code<10){
            no = "0"+(town_code);
        }else{
            no = town_code+"";
        }
        dirPath = sdcard+"/StampTour_kyj/contents/contents/img_list_heap_"+no+"@2x.png";
    }


    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(dirPath);
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
