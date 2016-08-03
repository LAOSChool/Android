package com.laoschool.screen;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.laoschool.R;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.adapter.LaoSchoolPagerAdapter;

import com.laoschool.entities.Message;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.DataAccessImage;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.screen.ScreenCreateAnnouncement.IScreenCreateAnnouncement;
import com.laoschool.screen.ScreenProfile.IProfile;
import com.laoschool.screen.login.ScreenLogin;
import com.laoschool.screen.view.Languages;
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
        ScreenInputExamResultsStudent.IScreenInputExamResults,
        IScreenCreateAnnouncement,
        ScreenListStudent.IScreenListStudentOfClass,
        IProfile {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private TabHost mTabHost;
    private ViewpagerDisableSwipeLeft mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, HomeActivity.TabInfo>();
    private LaoSchoolPagerAdapter mPagerAdapter;
    private int currentPosition = 0;
    int containerId;
    private String currentRole;
    public int beforePosition;
    private DataAccessInterface service;
    private Bundle savedInstanceState;
    private Context thiz;
    public User selectedStudent;
    private Animation animShow, animHide;
    private int clickPressBack = 0;
    public List<User> selectedUserList;
    private FirebaseAnalytics mFirebaseAnalytics;


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
        thiz = this;
        service = LaoSchoolSingleton.getInstance().getDataAccessService();
//        Toast.makeText(this, "Getting profile", Toast.LENGTH_SHORT).show();
        if (LaoSchoolShared.myProfile == null) {
            getUserProfile();
        } else {
            _startHome();
        }
        initAnimation();

    }

    private void _startHome() {
        currentRole = LaoSchoolShared.myProfile.getRoles();
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        intialiseViewPager(currentRole);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }

        }
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "current_token:" + token);
        saveToken(token);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(LaoSchoolShared.FA_CURRENT_ROLE, currentRole);
        mFirebaseAnalytics.logEvent(getString(R.string.SCCommon_AppName), new Bundle());
    }

    private void saveToken(String token) {
        DataAccessImpl.getInstance(this).saveToken(token, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "Save token sucess:" + result);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure()/Save token fail:" + message);
            }

            @Override
            public void onAuthFail(String message) {
                Log.d(TAG, "onAuthFail()/Save token fail :" + message);
            }
        });
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

        ScreenAttended screenAttended = (ScreenAttended) ScreenAttended.instantiate(containerId, currentRole);
        fragments.add(screenAttended);

        fragments.add(ScreenMore.instantiate(containerId, currentRole));

        fragments.add(ScreenCreateMessage.instantiate(containerId, currentRole));

        fragments.add(ScreenSchedule.instantiate(containerId, currentRole));

        fragments.add(ScreenFinalResultsStudent.instantiate(containerId, currentRole));

        fragments.add(ScreenSchoolInformation.instantiate(containerId, currentRole));

        fragments.add(ScreenListTeacher.instantiate(containerId, currentRole));

        fragments.add(ScreenSelectListStudent.instantiate(containerId, currentRole));

        fragments.add(ScreenInputExamResultsStudent.instantiate(containerId, currentRole));

        fragments.add(ScreenSetting.instantiate(containerId, currentRole));

        fragments.add(ScreenProfile.instantiate(containerId, currentRole));

        fragments.add(ScreenMessageDetails.instantiate(containerId, currentRole));

        fragments.add(ScreenAnnouncementsDetails.instantiate(containerId, currentRole));

        fragments.add(ScreenCreateAnnouncement.instantiate(containerId, currentRole));

        fragments.add(ScreenRequestAttendance.instantiate(containerId, currentRole, this, screenAttended));

        fragments.add(ScreenListStudent.instantiate(containerId, currentRole));

        this.mPagerAdapter = new LaoSchoolPagerAdapter(super.getSupportFragmentManager(), fragments);
        //set adapter and set handler page change
        this.mViewPager.setAdapter(this.mPagerAdapter);
        //disable swipe

        this.mViewPager.addOnPageChangeListener(this);

        mViewPager.setAllowedSwipeDirection(SwipeDirection.none);

        getSupportActionBar().setTitle(R.string.SCCommon_Message);
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
        TabHost.TabSpec tabSpecMessage = this.mTabHost.newTabSpec(getString(R.string.SCCommon_Message));
        View tabIncatorMessage = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.SCCommon_Message, R.drawable.ic_message_black_24dp);
        ((TextView) (tabIncatorMessage.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_selected));
        ((ImageView) (tabIncatorMessage.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_selected));

        tabIncatorMessage.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecMessage.setIndicator(tabIncatorMessage);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecMessage, (tabInfo = new TabInfo(getString(R.string.SCCommon_Message), ScreenMessage.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        //Add tab announcemen
        TabHost.TabSpec tabSpecAnnouncemen = this.mTabHost.newTabSpec(getString(R.string.SCCommon_Announcements));
        View tabIncatorAnnouncemen = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.SCCommon_Announcements, R.drawable.ic_announcement_black_24dp);
        ((TextView) (tabIncatorAnnouncemen.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorAnnouncemen.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorAnnouncemen.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecAnnouncemen.setIndicator(tabIncatorAnnouncemen);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecAnnouncemen, (tabInfo = new TabInfo(getString(R.string.SCCommon_Announcements), ScreenAnnouncements.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);


        //Add tab mark score
        TabHost.TabSpec tabSpecSchedule = this.mTabHost.newTabSpec(getString(R.string.SCCommon_ExamResults));

        View tabIncatorSchedule = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.SCCommon_ExamResults, R.drawable.ic_date_range_black_24dp);
        ((TextView) (tabIncatorSchedule.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorSchedule.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorSchedule.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecSchedule.setIndicator(tabIncatorSchedule);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecSchedule, (tabInfo = new TabInfo(getString(R.string.SCCommon_ExamResults), ScreenExamResults.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);


        //add tab attended
        TabHost.TabSpec tabSpecAttended = this.mTabHost.newTabSpec(getString(R.string.SCCommon_Attendance));
        View tabIncatorAttended = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.SCCommon_Attendance, R.drawable.ic_event_available_black_24dp);
        ((TextView) (tabIncatorAttended.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorAttended.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorAttended.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecAttended.setIndicator(tabIncatorAttended);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecAttended, (tabInfo = new TabInfo(getString(R.string.SCCommon_Attendance), ScreenAttended.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);


        //Add tab more
        TabHost.TabSpec tabSpecMore = this.mTabHost.newTabSpec(getString(R.string.SCCommon_More));
        View tabIncatorMore = LaoSchoolShared.createTabIndicator(getLayoutInflater(), mTabHost, R.string.SCCommon_More, R.drawable.ic_more_horiz_black_24dp);
        ((TextView) (tabIncatorMore.findViewById(R.id.tab_indicator_title))).setTextColor(getResources().getColor(R.color.color_text_tab_unselected));
        ((ImageView) (tabIncatorMore.findViewById(R.id.tab_indicator_icon))).setColorFilter(getResources().getColor(R.color.color_icon_tab_unselected));

        tabIncatorMore.setLayoutParams(new LinearLayout.LayoutParams(widthTabIndicator, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabSpecMore.setIndicator(tabIncatorMore);
        HomeActivity.AddTab(this, this.mTabHost, tabSpecMore, (tabInfo = new TabInfo(getString(R.string.SCCommon_More), ScreenMore.class, args)));
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
                _setTitleandShowButtonBack(R.string.SCCommon_Message, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1:
                _setTitleandShowButtonBack(R.string.SCCommon_Announcements, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2:
                _setTitleandShowButtonBack(R.string.SCCommon_ExamResults, null, DisplayButtonHome.hide);
                getSupportActionBar().setDisplayShowCustomEnabled(false);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ATTENDED_3:
                _setTitleandShowButtonBack(R.string.SCCommon_Attendance, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MORE_4:
                _setTitleandShowButtonBack(R.string.SCCommon_More, null, DisplayButtonHome.hide);
                break;
            case LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5:
                _setTitleandShowButtonBack(R.string.SCCreateMessage_CreateMessage, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHEDULE_6:
                _setTitleandShowButtonBack(R.string.SCCommon_TimeTable, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHOOL_RECORD_YEAR_7:
                _setTitleandShowButtonBack(R.string.SCCommon_FinalResults, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SCHOOL_INFORMATION_8:
                _setTitleandShowButtonBack(R.string.SCCommon_SchoolInfomation, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9:
                _setTitleandShowButtonBack(R.string.SCCommon_ListTeacher, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_10:
                _setTitleandShowButtonBack(R.string.title_screen_select_list_student, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11:
                _setTitleandShowButtonBack(R.string.SCExamResults_AddScore, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
                break;
            case LaoSchoolShared.POSITION_SCREEN_SETTING_12:
                _setTitleandShowButtonBack(R.string.SCCommon_ChangePassword, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_PROFILE_13:
                _setTitleandShowButtonBack(R.string.SCCommon_Profile, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_MESSAGE_DETAILS_14:
                _setTitleandShowButtonBack(R.string.title_screen_message_details, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENT_DETAILS_15:
                _setTitleandShowButtonBack(R.string.title_screen_announcement_details, null, DisplayButtonHome.show);
                break;
            case LaoSchoolShared.POSITION_SCREEN_CREATE_ANNOUNCEMENT_16:
                _setTitleandShowButtonBack(R.string.SCCreateAnnocement_NewAnouncement, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
                break;
            case LaoSchoolShared.POSITION_SCREEN_REQUEST_ATTENDANCE_17:
                _setTitleandShowButtonBack(R.string.SCAttendance_AbsentCreate, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);
                break;
            case LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18:
                _setTitleandShowButtonBack(R.string.SCCommon_ClassInfo, null, DisplayButtonHome.show);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                break;
            default:
                _setTitleandShowButtonBack(R.string.SCCommon_Message, null, DisplayButtonHome.hide);
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorPrimary)));
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
            // mViewPager.setAllowedSwipeDirection(SwipeDirection.left);
            mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        } else if (position > LaoSchoolShared.POSITION_SCREEN_MORE_4) {
            //  mViewPager.setAllowedSwipeDirection(SwipeDirection.none);
            mTabHost.getTabWidget().setVisibility(View.GONE);
        } else {
            mTabHost.getTabWidget().setVisibility(View.VISIBLE);
            // mViewPager.setAllowedSwipeDirection(SwipeDirection.all);
        }
        FragmentLifecycle fragmentToShow = (FragmentLifecycle) mPagerAdapter.getItem(position);
        fragmentToShow.onResumeFragment();

        FragmentLifecycle fragmentToHide = (FragmentLifecycle) mPagerAdapter.getItem(currentPosition);
        fragmentToHide.onPauseFragment();

        currentPosition = position;
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
            if (clickPressBack == 0) {
                Toast.makeText(this, "Touch back again", Toast.LENGTH_SHORT).show();
                clickPressBack = 1;
            } else if (clickPressBack == 1) {
                super.onBackPressed();
            }
        } else if (currentPage > LaoSchoolShared.POSITION_SCREEN_MORE_4) {
            if (currentPage == LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5) {
                goBackToMessage();
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_10) {
                //back to tab create message
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
                ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getSupportFragmentManager().findFragmentByTag(tag);
                if (screenCreateMessage != null) {
                    screenCreateMessage.setTestMessage(null);
                }
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11) {
                backToExamResults();
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
                } else if (beforePosition == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18) {
                    //back to tab message
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18);
                } else {
                    //back to tab attender
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_MORE_4);
                }
            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_CREATE_ANNOUNCEMENT_16) {
                //
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);

            } else if (currentPage == LaoSchoolShared.POSITION_SCREEN_REQUEST_ATTENDANCE_17) {
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_ATTENDED_3);
            } else {
                //back to tab information
                _gotoPage(LaoSchoolShared.POSITION_SCREEN_MORE_4);
            }
        } else {
            if (clickPressBack == 0) {
                Toast.makeText(this, "Touch back again", Toast.LENGTH_SHORT).show();
                clickPressBack = 1;
            } else if (clickPressBack == 1) {
                super.onBackPressed();
            }
        }
        getCurrentFocus().clearFocus();
        //Hide key board
        LaoSchoolShared.hideSoftKeyboard(this);
    }

    private void backToExamResults() {
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
        ScreenInputExamResultsStudent inputExamResultsStudent = (ScreenInputExamResultsStudent) getSupportFragmentManager().findFragmentByTag(tag);
        if (inputExamResultsStudent.selectedSubjectId > 0 || inputExamResultsStudent.selectedExamTypeId > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.SCExamResults_CancelInputExam);
            builder.setNegativeButton(R.string.SCCommon_No, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton(R.string.SCCommon_Yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    //back to tab exam
                    _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        } else {
            _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
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
    public void gotoScreenCreateMessage() {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_MESSAGE_0;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
        ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getSupportFragmentManager().findFragmentByTag(tag);
        screenCreateMessage.presetData(null, null, "");
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

    void _gotoPage(int position) {
        this.mViewPager.setCurrentItem(position, false);
    }


    @Override
    public void gotoScreenInputExamResults() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
    }

    @Override
    public void gotoMessageDetails(Message message) {
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
    public void gotoCreateAttendanceFormScreenAttendance() {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_ATTENDED_3;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_REQUEST_ATTENDANCE_17);
    }

    @Override
    public void goToCreateMessagefromScreenAttendance(List<User> students, List<User> selectedStudents, String defaultText) {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_ATTENDED_3;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
        ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getSupportFragmentManager().findFragmentByTag(tag);
        screenCreateMessage.presetData(selectedStudents, selectedStudents, defaultText);
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
//            DataAccessMessage.deleteTable();
//            DataAccessImage.deleteTable();

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
    public void cancelInputExamResults() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
        if (!getSupportActionBar().isShowing())
            getSupportActionBar().show();

    }

    @Override
    public void goBackToMessage() {
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
        ScreenMessage screenMessage = (ScreenMessage) getSupportFragmentManager().findFragmentByTag(tag);

        if (beforePosition == LaoSchoolShared.POSITION_SCREEN_MESSAGE_0) {
            //back to tab message
            _gotoPage(LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
            screenMessage.reloadSentPager();
        } else if (beforePosition == LaoSchoolShared.POSITION_SCREEN_PROFILE_13) {
            //back to tab message
            beforePosition = LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5;
            _gotoPage(LaoSchoolShared.POSITION_SCREEN_PROFILE_13);
            screenMessage.reloadSentPager();
        } else {
            //back to tab attender
            _gotoPage(LaoSchoolShared.POSITION_SCREEN_ATTENDED_3);
            screenMessage.reloadDataAfterCreateMessages();
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    public void gotoScheduleformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_SCHEDULE_6);
    }

    @Override
    public void gotoChangeLanguage() {
        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this).create();
        dialog.setView(new Languages(getApplicationContext(), new Languages.LanguagesListener() {
            @Override
            public void onChangeLanguage() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }).getView());
        dialog.show();
    }

    @Override
    public void _gotoCreateAnnouncement() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_ANNOUNCEMENT_16);
    }

    @Override
    public void _goBackAnnocements() {
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);
        ScreenAnnouncements screenAnnouncements = (ScreenAnnouncements) getSupportFragmentManager().findFragmentByTag(tag);
        screenAnnouncements.reloadAffterCreate();
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);
    }

    public void reloadApplication(View view) {
        finish();
        Intent reloadApplication = new Intent(this, SplashScreen.class);
        startActivity(reloadApplication);
    }

    public void hideBottomBar() {
        mTabHost.getTabWidget().setVisibility(View.GONE);
        mTabHost.getTabWidget().startAnimation(animHide);
    }

    public void showBottomBar() {
        mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        mTabHost.getTabWidget().startAnimation(animShow);
    }


    @Override
    public void gotoListStudentformMore() {
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18);
    }

    @Override
    public void gotoDetailsStudent(List<User> userList, User user) {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18;
        selectedStudent = user;
        selectedUserList = userList;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_PROFILE_13);
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

    public void displaySearch() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorPrimarySearch)));
        setStatusBarColor(R.color.colorPrimaryDarkSearch);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void cancelSearch() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorPrimary)));
        setStatusBarColor(R.color.colorPrimaryDark);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorId));
        }
    }

    @Override
    public void sendMessage(List<User> userList, User selectedStudent) {
        beforePosition = LaoSchoolShared.POSITION_SCREEN_PROFILE_13;
        _gotoPage(LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5);
        ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getSupportFragmentManager().findFragmentByTag(tag);
        screenCreateMessage.presetData(userList, Arrays.asList(selectedStudent), "");
    }


}
