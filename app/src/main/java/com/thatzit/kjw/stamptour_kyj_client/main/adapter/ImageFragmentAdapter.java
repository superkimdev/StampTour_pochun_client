package com.thatzit.kjw.stamptour_kyj_client.main.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Environment;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

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
    private int mCount = 5;
    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
    private int town_code;
    private final String TAG = "ImageFragmentAdapter";

    public ImageFragmentAdapter(FragmentManager fm, int town_code) {
        super(fm);
        this.town_code = town_code;
        sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
        no = town_code+"";
        dirPath = sdcard+"/StampTour_kyj/contents/contents/town"+no;
    }


    @Override
    public Fragment getItem(int position) {
        position = position+1;
        Log.e(TAG,"getItem : "+dirPath+"_"+position+".png");
        return ImageFragment.newInstance(dirPath+"_"+position+".png");
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
        return 0;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
