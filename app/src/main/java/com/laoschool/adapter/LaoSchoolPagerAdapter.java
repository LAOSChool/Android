package com.laoschool.adapter;


import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Hue on 2/26/2016.
 */
public class LaoSchoolPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private android.support.v4.app.FragmentManager fragmentManager;

    /**
     * @param fm
     * @param fragments
     */
    public LaoSchoolPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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
        return fragment;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.LaoSchoolPagerAdapter#getCount()
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

    @Override
    public int getItemPosition(Object object) {
        return LaoSchoolPagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position > 4)
            super.destroyItem(container, position, object);
    }
}

