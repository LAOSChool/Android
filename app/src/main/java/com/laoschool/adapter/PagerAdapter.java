package com.laoschool.adapter;


import java.util.List;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.laoschool.shared.LaoSchoolShared;

/**
 * Created by Hue on 2/26/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private android.support.v4.app.FragmentManager fragmentManager;

    /**
     * @param fm
     * @param fragments
     */
    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.fragmentManager = fm;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = this.fragments.get(position);
        //Log.d("Page", "-Tag:" + fragment.getTag());
        return fragment;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return this.fragments.size();
    }


    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position > 5) {
            super.destroyItem(container, position, object);
        }

    }
}

