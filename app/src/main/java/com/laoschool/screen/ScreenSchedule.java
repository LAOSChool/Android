package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.TimeTablePageAdapter;
import com.laoschool.entities.TimeTable;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSchedule extends Fragment implements FragmentLifecycle {


    private static final String TAG = "ScreenSchedule";
    private static String CURRENT_ROLE = "current_role";
    private int containerId;
    private String currentRole;
    private Context context;

    private static String mime = "text/html";
    private static String encoding = "utf-8";
    private static String ASSETS = "file:///android_asset/";
    private SwipeRefreshLayout mSwipeRefeshScheduleStudent;

    //
    private ScrollView mScrollTimeTableStudent;
    private TextView lbSchoolNameStudent;
    private TextView lbGvcnStudent;
    private TextView lbTermStudent;
    private boolean alreadyExecuted = false;
    private FrameLayout mErrorStudent;
    private FrameLayout mNoDataStudent;
    private ProgressBar mProgressStudent;
    private ViewPager mPageTimeTableStudent;
    private PagerSlidingTabStrip tabStripStudent;

    MenuItem itemRefesh;


    private FragmentManager fr;


    public ScreenSchedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _defineScreenSchedulebyRole(inflater, container);
    }

    private View _defineSrceenScheduleTeacher(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_schedule_tearcher, container, false);
        //
        TextView txtSchoolName = (TextView) view.findViewById(R.id.txtSchoolName);
        TextView txtTerm = (TextView) view.findViewById(R.id.txtClassScreenExamResults);
        WebView mWebViewDetailsScheduleview = (WebView) view.findViewById(R.id.mWebViewDetailsSchedule);
        mWebViewDetailsScheduleview.loadDataWithBaseURL(ASSETS, testDataStudentSchedule, mime, encoding, null);
        return view;
    }

    private View _defineSrceenScheduleStudent(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_schedule_student, container, false);
        //
        lbSchoolNameStudent = (TextView) view.findViewById(R.id.txtSchoolName);
        lbGvcnStudent = (TextView) view.findViewById(R.id.txtGvcn);
        lbTermStudent = (TextView) view.findViewById(R.id.txtClassScreenExamResults);
        mSwipeRefeshScheduleStudent = (SwipeRefreshLayout) view.findViewById(R.id.mRefeshScheduleStudent);
        mScrollTimeTableStudent = (ScrollView) view.findViewById(R.id.mScrollTimeTableStudent);
        mProgressStudent = (ProgressBar) view.findViewById(R.id.mProgress);
        mErrorStudent = (FrameLayout) view.findViewById(R.id.mError);
        mNoDataStudent = (FrameLayout) view.findViewById(R.id.mNoData);
        mPageTimeTableStudent = (ViewPager) view.findViewById(R.id.mPageTimeTable);
        tabStripStudent = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);


        _handerSwipeRefresh();
        _handlerErrorRefresh();
        _handlerNodataRefresh();
        return view;
    }

    private void _handerSwipeRefresh() {
        mSwipeRefeshScheduleStudent.setEnabled(false);
        mSwipeRefeshScheduleStudent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefeshScheduleStudent.setRefreshing(false);
//                getInformationStudent();
//                getTimeTable();
            }
        });
        mScrollTimeTableStudent.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                int scrollX = mScrollTimeTableStudent.getScrollX(); //for horizontalScrollView
                int scrollY = mScrollTimeTableStudent.getScrollY(); //for verticalScrollView
                //Log.d(TAG, "_handerSwipeRefresh().onScrollChanged() scrollX:" + scrollX + ",scrollY:" + scrollY);
                //DO SOMETHING WITH THE SCROLL COORDINATES
//                if (scrollY == 0) {
//                    mSwipeRefeshScheduleStudent.setEnabled(true);
//                } else {
//                    mSwipeRefeshScheduleStudent.setEnabled(false);
//                }


            }
        });
    }

    private void _handlerErrorRefresh() {
        mErrorStudent.findViewById(R.id.mReloadData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformationStudent();
                getTimeTable();
            }
        });
    }

    private void _handlerNodataRefresh() {
        mNoDataStudent.findViewById(R.id.mReloadData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformationStudent();
                getTimeTable();
            }
        });
    }

    private View _defineScreenSchedulebyRole(LayoutInflater inflater, ViewGroup container) {
//        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
//            return _defineSrceenScheduleTeacher(inflater, container);
//        } else {
        return _defineSrceenScheduleStudent(inflater, container);
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (currentRole != null) {
            inflater.inflate(R.menu.menu_screen_schedule, menu);
            itemRefesh = menu.findItem(R.id.action_refersh_time_table);
            itemRefesh.setVisible(false);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (currentRole != null) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_refersh_time_table:
                    getTimeTable();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(CURRENT_ROLE);
            Log.d(TAG, "-Container Id:" + containerId);
            Log.d(TAG, "-Role:" + currentRole);
        }
        this.fr = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        if (!alreadyExecuted && getUserVisibleHint()) {
            if (currentRole == null) {
                Log.d(TAG, "onResumeFragment() - current role null");
                currentRole = LaoSchoolShared.ROLE_STUDENT;
            }
//            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
//            } else {
            getInformationStudent();
            getTimeTable();
//            }

        }
    }

    private void getInformationStudent() {
        try {
            if (LaoSchoolShared.myProfile != null) {
                //set infomation
                String termName = String.valueOf(context.getString(R.string.SCCommon_Term) + " " + LaoSchoolShared.myProfile.getEclass().getTerm());
                String year = String.valueOf(LaoSchoolShared.myProfile.getEclass().getYears());

                lbSchoolNameStudent.setText(LaoSchoolShared.myProfile.getEclass().getTitle());
                lbGvcnStudent.setText(LaoSchoolShared.myProfile.getEclass().getHeadTeacherName());
                lbTermStudent.setText(year + "   " + termName);
            }
        } catch (Exception e) {
            Log.e(TAG, "getInformationStudent() -exception:" + e.getMessage());
        }
    }

    private void getTimeTable() {
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        _showProgressLoadingStudent(true);
        LaoSchoolSingleton.getInstance().getDataAccessService().getTimeTables(classId, new AsyncCallback<List<TimeTable>>() {
            @Override
            public void onSuccess(List<TimeTable> result) {
                if (result.size() > 0) {
                    _defineTimeTableStudent(result);
                } else {
                    _showNoData();
                }
            }

            @Override
            public void onFailure(String message) {
                _showError();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(getActivity());
            }
        });
    }


    private void _showError() {
        try {
            mScrollTimeTableStudent.setVisibility(View.GONE);
            mProgressStudent.setVisibility(View.GONE);
            mErrorStudent.setVisibility(View.VISIBLE);
            mNoDataStudent.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "_showError() -exception:" + e.getMessage());
        }
    }

    private void _showNoData() {
        try {
            mScrollTimeTableStudent.setVisibility(View.GONE);
            mProgressStudent.setVisibility(View.GONE);
            mErrorStudent.setVisibility(View.GONE);
            mNoDataStudent.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "_showNoData() -exception:" + e.getMessage());
        }
    }

    private void _showProgressLoadingStudent(boolean show) {
        try {
            mScrollTimeTableStudent.setVisibility(show ? View.GONE : View.VISIBLE);
            mProgressStudent.setVisibility(show ? View.VISIBLE : View.GONE);
            mErrorStudent.setVisibility(View.GONE);
            mNoDataStudent.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "_showProgressLoadingStudent() -exception:" + e.getMessage());
        }

    }

    private void _defineTimeTableStudent(List<TimeTable> result) {
        try {
            //get current Term
            ArrayList<Integer> dayOfweek = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
            Map<Integer, ArrayList<TimeTable>> timeTablebyDayMap = new HashMap<>();

            for (Integer day : dayOfweek) {
                ArrayList<TimeTable> timeTables = new ArrayList<>();
                for (TimeTable timeTable : result) {
                    if (timeTable.getWeekday_id() == (day + 1)) {
                        timeTables.add(timeTable);
                    }
                }
                timeTablebyDayMap.put(day, timeTables);
            }
            TimeTablePageAdapter timeTablePageAdapter = new TimeTablePageAdapter(getActivity().getSupportFragmentManager(), context, timeTablebyDayMap);
            mPageTimeTableStudent.setAdapter(timeTablePageAdapter);
            tabStripStudent.setViewPager(mPageTimeTableStudent);
            itemRefesh.setVisible(true);
            _scrollToTopStudent();
            _setTargetDisplayDay();
            _showProgressLoadingStudent(false);
        } catch (Exception e) {
            Log.e(TAG, "_defineTimeTableStudent() -exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void _setTargetDisplayDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "_setTargetDisplayDay() -day:" + day);
        switch (day) {
            case Calendar.MONDAY:
                mPageTimeTableStudent.setCurrentItem(0);
                break;
            case Calendar.TUESDAY:
                mPageTimeTableStudent.setCurrentItem(1);
                break;
            case Calendar.WEDNESDAY:
                mPageTimeTableStudent.setCurrentItem(2);
                break;
            case Calendar.THURSDAY:
                mPageTimeTableStudent.setCurrentItem(3);
                break;
            case Calendar.FRIDAY:
                mPageTimeTableStudent.setCurrentItem(4);
                break;
            case Calendar.SATURDAY:
                mPageTimeTableStudent.setCurrentItem(5);
                break;
            case Calendar.SUNDAY:
                mPageTimeTableStudent.setCurrentItem(6);
                break;
        }
    }


    private void _scrollToTopStudent() {
        mScrollTimeTableStudent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ready, move up
                mScrollTimeTableStudent.fullScroll(View.FOCUS_UP);
            }
        });
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenSchedule fragment = new ScreenSchedule();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLowMemory() {
        Log.e(TAG, "onLowMemory()");
        super.onLowMemory();
    }

    String testDataStudentSchedule = "<table class=\"MsoTableGrid\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"400\" style=\"width: 50pt; border-collapse: collapse; border: none;\">\n" +
            "    <tbody>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border: 1pt solid windowtext; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THá»¨\n" +
            "            HAI</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THá»¨\n" +
            "            BA</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THá»¨\n" +
            "            TÆ¯</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THá»¨\n" +
            "            NÄ‚M</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THá»¨\n" +
            "            SÃU</strong></p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">SHDC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">THá»‚ Dá»¤C</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÃN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">THá»‚ Dá»¤C</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÃN </p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÃN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÃN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KÄ¨ THUáº¬T</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÃN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: justify; line-height: 150%;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Lá»ŠCH Sá»¬</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">Äáº O Äá»¨C</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">CHÃNH Táº¢</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">Táº¬P Äá»ŒC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KHOA Há»ŒC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TL VÄ‚N</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td colspan=\"5\" valign=\"top\" style=\"width: 455.4pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><em>RA\n" +
            "            CHÆ I</em></p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr style=\"height: 24.25pt;\">\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">Táº¬P Äá»ŒC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"line-height: 150%;\">&nbsp;&nbsp;&nbsp;&nbsp; LT &amp; CÃ‚U</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">Äá»ŠA LÃ</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">LT &amp; CÃ‚U</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">MÄ¨ THUáº¬T</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">&nbsp;Ã‚M NHáº C</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KHOA Há»ŒC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TL VÄ‚N</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">Ká»‚ CHUYá»†N</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">SH Lá»šP</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "    </tbody>\n" +
            "</table>";

}
