package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.TimeTableStudentAdapter;
import com.laoschool.entities.TimeTable;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.ArrayList;
import java.util.Arrays;
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

    private RecyclerView mTimeTableStudentGrid;
    private SwipeRefreshLayout mRefeshScheduleStudent;

    //
    TextView lbSchoolNameStudent;
    TextView lbGvcnStudent;
    TextView lbTermStudent;

    String testDataStudentSchedule = "<table class=\"MsoTableGrid\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"400\" style=\"width: 50pt; border-collapse: collapse; border: none;\">\n" +
            "    <tbody>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border: 1pt solid windowtext; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THỨ\n" +
            "            HAI</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THỨ\n" +
            "            BA</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THỨ\n" +
            "            TƯ</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THỨ\n" +
            "            NĂM</strong></p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: solid solid solid none; border-top-color: windowtext; border-right-color: windowtext; border-bottom-color: windowtext; border-top-width: 1pt; border-right-width: 1pt; border-bottom-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><strong>THỨ\n" +
            "            SÁU</strong></p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">SHDC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">THỂ DỤC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÁN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">THỂ DỤC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÁN </p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÁN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÁN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KĨ THUẬT</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TOÁN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: justify; line-height: 150%;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; LỊCH SỬ</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">ĐẠO ĐỨC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">CHÍNH TẢ</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TẬP ĐỌC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KHOA HỌC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TL VĂN</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td colspan=\"5\" valign=\"top\" style=\"width: 455.4pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\"><em>RA\n" +
            "            CHƠI</em></p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr style=\"height: 24.25pt;\">\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TẬP ĐỌC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"line-height: 150%;\">&nbsp;&nbsp;&nbsp;&nbsp; LT &amp; CÂU</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">ĐỊA LÝ</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">LT &amp; CÂU</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt; height: 24.25pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">MĨ THUẬT</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid; border-right-color: windowtext; border-bottom-color: windowtext; border-left-color: windowtext; border-right-width: 1pt; border-bottom-width: 1pt; border-left-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">&nbsp;ÂM NHẠC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KHOA HỌC</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">TL VĂN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 88.55pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">KỂ CHUYỆN</p>\n" +
            "            </td>\n" +
            "            <td valign=\"top\" style=\"width: 101.2pt; border-style: none solid solid none; border-bottom-color: windowtext; border-bottom-width: 1pt; border-right-color: windowtext; border-right-width: 1pt; padding: 0in 5.4pt;\">\n" +
            "            <p class=\"MsoNormal\" style=\"text-align: center; line-height: 150%;\">SH LỚP</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "    </tbody>\n" +
            "</table>";

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
        mRefeshScheduleStudent = (SwipeRefreshLayout) view.findViewById(R.id.mRefeshScheduleStudent);
        mTimeTableStudentGrid = (RecyclerView) view.findViewById(R.id.mTimeTableStudentGrid);

        //new GridLayoutManager(context, 8, GridLayoutManager.VERTICAL, false)
        //new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        mTimeTableStudentGrid.setLayoutManager(new GridLayoutManager(context, 8, GridLayoutManager.VERTICAL, false));

//        WebView mWebViewDetailsScheduleview = (WebView) view.findViewById(R.id.mWebViewDetailsSchedule);
//        mWebViewDetailsScheduleview.loadDataWithBaseURL(ASSETS, testDataStudentSchedule, mime, encoding, null);

        mRefeshScheduleStudent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getTimeTable(LaoSchoolShared.myProfile.getEclass().getId());
                mRefeshScheduleStudent.setRefreshing(false);
            }
        });
        return view;
    }

    private View _defineScreenSchedulebyRole(LayoutInflater inflater, ViewGroup container) {
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            return _defineSrceenScheduleTeacher(inflater, container);
        } else {
            return _defineSrceenScheduleStudent(inflater, container);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        getTimeTable(LaoSchoolShared.myProfile.getEclass().getId());
    }

    private void getTimeTable(int classId) {
        LaoSchoolSingleton.getInstance().getDataAccessService().getTimeTables(classId, new AsyncCallback<List<TimeTable>>() {
            @Override
            public void onSuccess(List<TimeTable> result) {
                Log.d(TAG, "getTimeTable().onSuccess() -results size:" + result.size());
                _defineTimeTableStudent(result);
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(getActivity());
            }
        });
    }

    private void _defineTimeTableStudent(List<TimeTable> result) {
        try {
            //set infomation
            lbSchoolNameStudent.setText(LaoSchoolShared.myProfile.getSchoolName());
            lbGvcnStudent.setText(LaoSchoolShared.myProfile.getEclass().getHeadTeacherName());
            lbTermStudent.setText(LaoSchoolShared.myProfile.getEclass().getTerm() + "/" + LaoSchoolShared.myProfile.getEclass().getYears());

            //get current Term
            //int currentTerm = 1;
            List<Integer> dayOfweek = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
            Map<Integer, List<TimeTable>> timeTablebyDayMap = new HashMap<>();
            for (Integer day : dayOfweek) {
                List<TimeTable> timeTables = new ArrayList<>();
                for (TimeTable timeTable : result) {
                    if (timeTable.getWeekday_id() == day) {
                        timeTables.add(timeTable);
                    }
                }
                Log.d(TAG, "_defineTimeTableStudent() -size:" + timeTables.size());
                timeTablebyDayMap.put(day, timeTables);
            }
            List<TimeTable> session = new ArrayList<>();
            session.add(new TimeTable("S1"));
            session.add(new TimeTable("S2"));
            session.add(new TimeTable("S3"));
            session.add(new TimeTable("S4"));
            session.add(new TimeTable("S5"));
            session.add(new TimeTable("S6"));
            timeTablebyDayMap.put(0, session);
            Log.d(TAG, "_defineTimeTableStudent() - session size:" + session.size());
            TimeTableStudentAdapter timeTableStudentAdapter = new TimeTableStudentAdapter(context, timeTablebyDayMap);
            mTimeTableStudentGrid.setAdapter(timeTableStudentAdapter);
        } catch (Exception e) {
        }
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenSchedule fragment = new ScreenSchedule();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }


}
