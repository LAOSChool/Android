package com.laoschool.screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.laoschool.R;
import com.laoschool.adapter.PagerAdapter;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

public class HomeActivity extends AppCompatActivity implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ScreenMessage.IScreenMessage,
        ScreenCreateMessage.IScreenCreateMessage,
        ScreenSelectListStudent.IScreenListStudent,
        ScreenExamResults.IScreenExamResults {
    private static final String TAG = "HomeScreen";

    private TabHost mTabHost;
    private ViewpagerDisableSwipeLeft mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, HomeActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    private int currentPosition = 0;
    int containerId;


    //private ActionMenuView amvMenu;

//    private static final int POSITION_SCREEN_MESSAGE_0 = 0;
//    private static final int POSITION_SCREEN_ROLL_UP_1 = 1;
//    private static final int POSITION_SCREEN_EXAM_RESULTS_2 = 2;
//    private static final int POSITION_SCREEN_SCHEDULE_3 = 3;
//    private static final int POSITION_SCREEN_INFORMATION_4 = 4;
//    private static final int POSITION_SCREEN_SCHOOL_RECORD_YEAR_5 = 5;
//    private static final int POSITION_SCREEN_SCHOOL_INFORMATION_6 = 6;
//    private static final int POSITION_SCREEN_LIST_TEACHER_7 = 7;
//    private static final int POSITION_SCREEN_CREATE_MESSAGE_8 = 8;
//    private static final int POSITION_SCREEN_LIST_STUDENT_9 = 9;


    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }


    public enum SwipeDirection {
        all, left, right, none;
    }

    /**
     * A simple factory that returns dummy views to the Tabhost
     *
     * @author mwho
     */
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /**
         * (non-Javadoc)
         *
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
//        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return onOptionsItemSelected(menuItem);
//            }
//        });


        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();
    }

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {
        this.mViewPager = (ViewpagerDisableSwipeLeft) super.findViewById(R.id.viewpager);
        containerId = mViewPager.getId();

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(ScreenMessage.instantiate(containerId));

        fragments.add(ScreenRollUp.instantiate(containerId));

        fragments.add(ScreenExamResults.instantiate(containerId));

        fragments.add(ScreenSchedule.instantiate(containerId));

        fragments.add(ScreenInformation.instantiate(containerId));

        fragments.add(ScreenSchoolRecordByYear.instantiate(containerId));

        fragments.add(ScreenSchoolInformation.instantiate(containerId));

        fragments.add(ScreenListTeacher.instantiate(containerId));

        fragments.add(ScreenCreateMessage.instantiate(containerId));

        fragments.add(ScreenSelectListStudent.instantiate(containerId));

        fragments.add(ScreenMarkScoreStudent.instantiate(containerId));

        fragments.add(ScreenSetting.instantiate(containerId));

        fragments.add(ScreenProfile.instantiate(containerId));

        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);

        //set adapter and set handler page change
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.addOnPageChangeListener(this);

        getSupportActionBar().setTitle(R.string.title_screen_message);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();
        TabInfo tabInfo = null;

        TabHost.TabSpec tabSpecMessage = this.mTabHost.newTabSpec(getString(R.string.title_screen_message));
        tabSpecMessage.setIndicator("", getResources().getDrawable(R.drawable.tab_message));

        HomeActivity.AddTab(this, this.mTabHost, tabSpecMessage, (tabInfo = new TabInfo(getString(R.string.title_screen_message), ScreenMessage.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        HomeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.title_screen_rollup)).setIndicator(getString(R.string.title_screen_rollup)), (tabInfo = new TabInfo(getString(R.string.title_screen_rollup), ScreenRollUp.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        HomeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.title_screen_exam_results)).setIndicator(getString(R.string.title_screen_exam_results)), (tabInfo = new TabInfo(getString(R.string.title_screen_exam_results), ScreenExamResults.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        HomeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.title_screen_schedule)).setIndicator(getString(R.string.title_screen_schedule)), (tabInfo = new TabInfo(getString(R.string.title_screen_schedule), ScreenSchedule.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        HomeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.title_screen_information)).setIndicator(getString(R.string.title_screen_information)), (tabInfo = new TabInfo(getString(R.string.title_screen_information), ScreenInformation.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * Add Tab content to the Tabhost
     *
     * @param activity
     * @param tabHost
     * @param tabSpec
     */
    private static void AddTab(HomeActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        Log.d(TAG, "onTabChanged:" + tag);

    }

    private void _changeTitleActionBar(int pos) {
        Log.d(TAG, "Current Page:" + pos);
        switch (pos) {
            case LaoSchoolShared.POSITION_SCREEN_MESSAGE_0:
                getSupportActionBar().setTitle(R.string.title_screen_message);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ROLL_UP_1:
                getSupportActionBar().setTitle(R.string.title_screen_rollup);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2:
                getSupportActionBar().setTitle(R.string.title_screen_exam_results);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHEDULE_3:
                getSupportActionBar().setTitle(R.string.title_screen_schedule);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case LaoSchoolShared.POSITION_SCREEN_INFORMATION_4:
                getSupportActionBar().setTitle(R.string.title_screen_information);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHOOL_RECORD_YEAR_5:
                getSupportActionBar().setTitle(R.string.title_screen_school_record_by_year);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHOOL_INFORMATION_6:
                getSupportActionBar().setTitle(R.string.title_screen_school_information);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_7:
                getSupportActionBar().setTitle(R.string.title_screen_list_teacher);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_8:
                getSupportActionBar().setTitle(R.string.title_screen_create_message);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_9:
                getSupportActionBar().setTitle(R.string.title_screen_select_list_student);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_10:
                getSupportActionBar().setTitle(R.string.title_screen_mark_score_student);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SETTING_11:
                getSupportActionBar().setTitle(R.string.title_screen_setting);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case LaoSchoolShared.POSITION_SCREEN_PROFILE_12:
                getSupportActionBar().setTitle(R.string.title_screen_profile);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            default:
                getSupportActionBar().setTitle(R.string.title_screen_message);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    /* (non-Javadoc)
    * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
    */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);
        this._changeTitleActionBar(position);
        if (position == LaoSchoolShared.POSITION_SCREEN_INFORMATION_4) {
            mViewPager.setAllowedSwipeDirection(SwipeDirection.left);
            mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        } else if (position > LaoSchoolShared.POSITION_SCREEN_INFORMATION_4) {
            mViewPager.setAllowedSwipeDirection(SwipeDirection.none);
            mTabHost.getTabWidget().setVisibility(View.GONE);
        } else {
            mTabHost.getTabWidget().setVisibility(View.VISIBLE);
            mViewPager.setAllowedSwipeDirection(SwipeDirection.all);
        }
        FragmentLifecycle fragmentToShow = (FragmentLifecycle) mPagerAdapter.getItem(position);
        fragmentToShow.onResumeFragment();

        FragmentLifecycle fragmentToHide = (FragmentLifecycle) mPagerAdapter.getItem(currentPosition);
        fragmentToHide.onPauseFragment();

        currentPosition = position;
        //Log.d(TAG,"onPageSelected");
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Home", "resume");

    }

    public void goto1(View view) {

    }

    @Override
    public void onBackPressed() {
        int currentPage = mViewPager.getCurrentItem();
        if (currentPage == LaoSchoolShared.POSITION_SCREEN_MESSAGE_0) {
            //Exit app
            super.onBackPressed();
        } else if (currentPage > LaoSchoolShared.POSITION_SCREEN_INFORMATION_4) {
            if (currentPage == LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_8) {
                //back to tab message
                mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_9) {
                //back to tab create message
                mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_8);
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_8);
                ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getSupportFragmentManager().findFragmentByTag(tag);
                if (screenCreateMessage != null) {
                    screenCreateMessage.setTestMessage(null);
                }
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_10) {
                //back to tab exam
                mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
            } else {
                //back to tab information
                mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_INFORMATION_4);
            }
        } else {
            mViewPager.setCurrentItem(currentPage - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_screen_message, menu);
        return true;
    }


    public void gotoSchoolRecord(View view) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_SCHOOL_RECORD_YEAR_5);
    }

    public void gotoInformationSchool(View view) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_SCHOOL_INFORMATION_6);
    }

    public void gotoListTeacher(View view) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_7);
    }

    @Override
    public void _gotoScreenCreateMessage() {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_8);
    }

    @Override
    public void gotoListStudent() {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_9);
    }

    @Override
    public void gotoCreateMessage() {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_8);
    }

    public void gotoProfile(View view) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_PROFILE_12);
    }
    public void gotoSetting(View view) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_SETTING_11);
    }


    @Override
    public void sendData(String message) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_10);
        //set data
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_10);
        Log.d(TAG, "ScreenMarkScoreStudent tag=" + tag);
        ScreenMarkScoreStudent screenMarkScoreStudent = (ScreenMarkScoreStudent) getSupportFragmentManager().findFragmentByTag(tag);
        if (screenMarkScoreStudent != null) {
            screenMarkScoreStudent.setDataMessage(message);
        } else {
            Log.d(TAG, "ScreenMarkScoreStudent is Null");
            Toast.makeText(this, "ScreenMarkScoreStudent is Null", Toast.LENGTH_SHORT).show();
        }
    }


}
