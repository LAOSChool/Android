package com.laoschool.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.laoschool.R;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.adapter.PagerAdapter;

import com.laoschool.entities.Message;

import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.DataAccessImage;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.screen.ScreenCreateAnnouncement.IScreenCreateAnnouncement;
import com.laoschool.screen.login.ScreenLogin;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

public class HomeActivity extends AppCompatActivity implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ScreenMessage.IScreenMessage,
        ScreenCreateMessage.IScreenCreateMessage,
        ScreenSelectListStudent.IScreenListStudent,
        ScreenExamResults.IScreenExamResults,
        ScreenAnnouncements.IScreenAnnouncements,
        ScreenAttended.IScreenAttended,
        ScreenMore.IScreenMore,
        ScreenListTeacher.IScreenListTeacher,
        ScreenMarkScoreStudent.IScreenMarkScoreStudent,
        IScreenCreateAnnouncement {
    private static final String TAG = "HomeScreen";

    private TabHost mTabHost;
    private ViewpagerDisableSwipeLeft mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, HomeActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    private int currentPosition = 0;
    int containerId;
    private String currentRole;
    public int beforePosition;

    private Bundle savedInstanceState;

    private DataAccessInterface service;
    private Context thiz;

    public enum Role {
        student, teacher;
    }

    public enum DisplayButtonHome {
        show, hide;
    }


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

        service = LaoSchoolSingleton.getInstance().getDataAccessService();
//        Toast.makeText(this, "Getting profile", Toast.LENGTH_SHORT).show();
        if (LaoSchoolShared.myProfile == null) {
            getUserProfile();
        } else {
            _startHome();
        }

    }

    private void _startHome() {
        currentRole = LaoSchoolShared.myProfile.getRoles();
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        intialiseViewPager(currentRole);
    }

    private void getUserProfile() {
        service.getUserProfile(new AsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    LaoSchoolShared.myProfile = result;
                    _startHome();
                } else {
                    LaoSchoolShared.goBackToLoginPage(thiz);
                }
            }

            @Override
            public void onFailure(String message) {
                Intent intent = new Intent(thiz, ScreenLogin.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(thiz);
            }
        });
    }

    private String _getRoleByInten(Intent intent) {
        if (intent.getAction().equals(LaoSchoolShared.ROLE_TEARCHER)) {
            return LaoSchoolShared.ROLE_TEARCHER;
        } else if (intent.getAction().equals(LaoSchoolShared.ROLE_STUDENT)) {
            return LaoSchoolShared.ROLE_STUDENT;
        } else {
            return LaoSchoolShared.ROLE_STUDENT;
        }
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
     *
     * @param currentRole
     */
    private void intialiseViewPager(String currentRole) {
        this.mViewPager = (ViewpagerDisableSwipeLeft) super.findViewById(R.id.viewpager);

        containerId = mViewPager.getId();

        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(ScreenMessage.instantiate(containerId, currentRole));

        fragments.add(ScreenAnnouncements.instantiate(containerId, currentRole));

        fragments.add(ScreenExamResults.instantiate(containerId, currentRole));

        fragments.add(ScreenAttended.instantiate(containerId, currentRole));

        fragments.add(ScreenMore.instantiate(containerId, currentRole));

        fragments.add(ScreenCreateMessage.instantiate(containerId, currentRole));

        fragments.add(ScreenSchedule.instantiate(containerId, currentRole));

        fragments.add(ScreenFinalResultsStudent.instantiate(containerId, currentRole));

        fragments.add(ScreenSchoolInformation.instantiate(containerId, currentRole));

        fragments.add(ScreenListTeacher.instantiate(containerId, currentRole));

        fragments.add(ScreenSelectListStudent.instantiate(containerId, currentRole));

        fragments.add(ScreenMarkScoreStudent.instantiate(containerId, currentRole));

        fragments.add(ScreenSetting.instantiate(containerId, currentRole));

        fragments.add(ScreenProfile.instantiate(containerId, currentRole));

        fragments.add(ScreenMessageDetails.instantiate(containerId, currentRole));

        fragments.add(ScreenAnnouncementsDetails.instantiate(containerId, currentRole));

        fragments.add(ScreenCreateAnnouncement.instantiate(containerId, currentRole));

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
        this.mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        TabInfo tabInfo = null;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthTabIndicator = (size.x) / 5;


        //AddTab message
        TabHost.TabSpec tabSpecMessage = this.mTabHost.newTabSpec(getString(R.string.title_screen_message));
        View tabIncatorMessage = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.title_screen_message, R.drawable.ic_message_black_24dp);
        ((TextView) (tabIncatorMessage.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_selected));
        ((ImageView) (tabIncatorMessage.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_selected));

        tabIncatorMessage.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecMessage.setIndicator(tabIncatorMessage);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecMessage, (tabInfo = new TabInfo(getString(R.string.title_screen_message), ScreenMessage.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        //Add tab announcemen
        TabHost.TabSpec tabSpecAnnouncemen = this.mTabHost.newTabSpec(getString(R.string.title_screen_announcements));
        View tabIncatorAnnouncemen = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.title_screen_announcements, R.drawable.ic_announcement_black_24dp);
        ((TextView) (tabIncatorAnnouncemen.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorAnnouncemen.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorAnnouncemen.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecAnnouncemen.setIndicator(tabIncatorAnnouncemen);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecAnnouncemen, (tabInfo = new TabInfo(getString(R.string.title_screen_announcements), ScreenAnnouncements.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);


        //Add tab mark score
        TabHost.TabSpec tabSpecSchedule = this.mTabHost.newTabSpec(getString(R.string.title_screen_exam_results));

        View tabIncatorSchedule = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.title_screen_exam_results, R.drawable.ic_date_range_black_24dp);
        ((TextView) (tabIncatorSchedule.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorSchedule.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorSchedule.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecSchedule.setIndicator(tabIncatorSchedule);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecSchedule, (tabInfo = new TabInfo(getString(R.string.title_screen_exam_results), ScreenExamResults.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);


        //add tab attended
        TabHost.TabSpec tabSpecAttended = this.mTabHost.newTabSpec(getString(R.string.title_screen_attended));
        View tabIncatorAttended = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.title_screen_attended, R.drawable.ic_event_available_black_24dp);
        ((TextView) (tabIncatorAttended.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorAttended.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorAttended.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecAttended.setIndicator(tabIncatorAttended);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecAttended, (tabInfo = new TabInfo(getString(R.string.title_screen_attended), ScreenAttended.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);


        //Add tab more
        TabHost.TabSpec tabSpecMore = this.mTabHost.newTabSpec(getString(R.string.title_screen_more));
        View tabIncatorMore = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.title_screen_more, R.drawable.ic_more_horiz_black_24dp);
        ((TextView) (tabIncatorMore.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorMore.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorMore.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecMore.setIndicator(tabIncatorMore);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecMore, (tabInfo = new TabInfo(getString(R.string.title_screen_more), ScreenMore.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        //Handler on Tab change
        this.mTabHost.setOnTabChangedListener(this);
        //Remove divider
        mTabHost.getTabWidget().setDividerDrawable(null);
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
        Log.d(TAG, "onTabChanged -CurrentTab" + pos);
        Log.d(TAG, "onTabChanged -tag:" + tag);
        _gotoPage(pos);
        setTabColor(mTabHost);

    }

    public void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            //tabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.color_tab_unselected));
            ((TextView) tabhost.getTabWidget().getChildAt(i).findViewById(R.id.tab_indicator_title)).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));//un selected
            ((ImageView) (tabhost.getTabWidget().getChildAt(i).findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        }
        ((TextView) tabhost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).findViewById(R.id.tab_indicator_title)).setTextColor((getResources().getColor(R.color.color_text_tab_selected))); // selected
        ((ImageView) (tabhost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_selected));
    }

    private void _changeTitleActionBar(int pos) {
        //Log.d(TAG, "Current Page:" + pos);
        switch (pos) {
            case LaoSchoolShared.POSITION_SCREEN_MESSAGE_0:
                _setTitleandShowButtonBack(R.string.title_screen_message, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1:
                _setTitleandShowButtonBack(R.string.title_screen_announcements, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2:
                _setTitleandShowButtonBack(R.string.title_screen_exam_results, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ATTENDED_3:
                _setTitleandShowButtonBack(R.string.title_screen_attended, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MORE_4:
                _setTitleandShowButtonBack(R.string.title_screen_more, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5:
                _setTitleandShowButtonBack(R.string.title_screen_create_message, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHEDULE_6:
                _setTitleandShowButtonBack(R.string.title_screen_schedule, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHOOL_RECORD_YEAR_7:
                _setTitleandShowButtonBack(R.string.title_screen_final_results_student, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHOOL_INFORMATION_8:
                _setTitleandShowButtonBack(R.string.title_screen_school_information, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9:
                _setTitleandShowButtonBack(R.string.title_screen_list_teacher, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_10:
                _setTitleandShowButtonBack(R.string.title_screen_select_list_student, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11:
                _setTitleandShowButtonBack(R.string.title_screen_mark_score_student, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SETTING_12:
                _setTitleandShowButtonBack(R.string.title_screen_setting, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_PROFILE_13:
                _setTitleandShowButtonBack(R.string.title_screen_profile, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MESSAGE_DETAILS_14:
                _setTitleandShowButtonBack(R.string.title_screen_message_details, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENT_DETAILS_15:
                _setTitleandShowButtonBack(R.string.title_screen_announcement_details, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_CREATE_ANNOUNCEMENT_16:
                _setTitleandShowButtonBack(-1, "", DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
                break;
            default:
                _setTitleandShowButtonBack(R.string.title_screen_message, null, DisplayButtonHome.hide);
        }
    }

    private void _setTitleandShowButtonBack(int id, String title, DisplayButtonHome enabled) {
        if (id == -1) {
            getSupportActionBar().setTitle(title);
        } else {
            getSupportActionBar().setTitle(id);
        }
        if (enabled == DisplayButtonHome.show) {
            //show
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            //hide
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
        if (position == LaoSchoolShared.POSITION_SCREEN_MORE_4) {
            mViewPager.setAllowedSwipeDirection(SwipeDirection.left);
            mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        } else if (position > LaoSchoolShared.POSITION_SCREEN_MORE_4) {
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
        } else if (currentPage > LaoSchoolShared.POSITION_SCREEN_MORE_4) {
            if (currentPage == LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5) {
                if (beforePosition == LaoSchoolShared.POSITION_SCREEN_MESSAGE_0) {
                    //back to tab message
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
                } else {
                    //back to tab attender
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_ATTENDED_3);
                }
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_10) {
                //back to tab create message
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
                ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getSupportFragmentManager().findFragmentByTag(tag);
                if (screenCreateMessage != null) {
                    screenCreateMessage.setTestMessage(null);
                }
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11) {
                //back to tab exam
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_MESSAGE_DETAILS_14) {
                //
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
                ScreenMessage screenCreateMessage = (ScreenMessage) getSupportFragmentManager().findFragmentByTag(tag);
                if (screenCreateMessage != null) {
                    screenCreateMessage.setRefeshListMessage(false);
                }
                //back to tab Message
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENT_DETAILS_15) {
                //back to tab Message
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_PROFILE_13) {
                if (beforePosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9) {
                    //back to tab message
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9);
                } else {
                    //back to tab attender
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_MORE_4);
                }
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_CREATE_ANNOUNCEMENT_16) {
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);
            } else {
                //back to tab information
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_MORE_4);
            }
        } else {
            //_gotoPage(currentPage - 1);
            super.onBackPressed();
        }
        //Hide key board
        LaoSchoolShared.hideSoftKeyboard(this);

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
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    public void gotoSchoolRecord(View view) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SCHOOL_RECORD_YEAR_7);
    }

    public void gotoInformationSchool(View view) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SCHOOL_INFORMATION_8);
    }

    public void gotoListTeacher(View view) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9);
    }

    @Override
    public void _gotoScreenCreateMessage() {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_MESSAGE_0;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
    }

    @Override
    public void gotoListStudent() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_10);
    }

    @Override
    public void goBackScreenCreateMessage() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
    }

    public void gotoProfile(View view) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_PROFILE_13);
    }

    public void gotoSetting(View view) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SETTING_12);
    }

    public void gotoExamResult(View view) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
    }

    private void _gotoPage(int position) {
        this.mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void sendData(String message) {
        mViewPager.setCurrentItem(LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
        //set data
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
        Log.d(TAG, "ScreenMarkScoreStudent tag=" + tag);
        ScreenMarkScoreStudent screenMarkScoreStudent = (ScreenMarkScoreStudent) getSupportFragmentManager().findFragmentByTag(tag);
        if (screenMarkScoreStudent != null) {
            screenMarkScoreStudent.setDataMessage(message);
        } else {
            Log.d(TAG, "ScreenMarkScoreStudent is Null");
            Toast.makeText(this, "ScreenMarkScoreStudent is Null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void _gotoMessageDetails(Message message) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_MESSAGE_DETAILS_14);
//        if (message != null)
//            _setTitleandShowButtonBack(-1, message.getTo_user_name(), DisplayButtonHome.show);
//        else
//            Log.d(TAG, "Messages null");
    }

    @Override
    public void gotoScreenAnnouncementDetails(Message notificaiton) {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENT_DETAILS_15);
    }

    @Override
    public void gotoCreateMessageFormScreenAttended() {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_ATTENDED_3;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
    }


    @Override
    public void gotoListTearcherformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9);
    }

    @Override
    public void gotoSchoolInformationformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SCHOOL_INFORMATION_8);
    }

    @Override
    public void gotoSettingformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SETTING_12);
    }

    @Override
    public void logoutApplication() {
        SharedPreferences mySPrefs = this.getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("auth_key");

        if (editor.commit()) {
            //clear cache data
            DataAccessMessage.deleteTable();
            DataAccessImage.deleteTable();

            Intent intent = new Intent(this, ScreenLogin.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Delete auth_key fails", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void gotoDetailsUser() {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_MORE_4;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_PROFILE_13);
    }

    @Override
    public void gotoExamResultsformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
    }

    @Override
    public void gotoSchoolRecordbyYearformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SCHOOL_RECORD_YEAR_7);
    }

    public String selectedTeacher;

    @Override
    public void gotoScreenTeacherDetailsformScreenListTeacher(String s) {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9;
        selectedTeacher = s;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_PROFILE_13);
        _setTitleandShowButtonBack(-1, s, DisplayButtonHome.show);
    }

    String student;

    @Override
    public void gotoScreenMarkScoreStudentFromExamResults(String student) {
        this.student = student;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
    }

    @Override
    public void doneMarkScoreStudent() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);

    }

    @Override
    public void goBackToMessage() {
        onBackPressed();
    }

    @Override
    public void gotoScheduleformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SCHEDULE_6);
    }

    @Override
    public void reLogin() {
        Intent intent = new Intent(this, ScreenLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void _gotoCreateAnnouncement() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_ANNOUNCEMENT_16);
    }

    @Override
    public void _goBackAnnocements() {
        onBackPressed();
    }

    public void reloadApplication(View view) {
        finish();
        Intent reloadApplication = new Intent(this, SplashScreen.class);
        startActivity(reloadApplication);
    }
}
