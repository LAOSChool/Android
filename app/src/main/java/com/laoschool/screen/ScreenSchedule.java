package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

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
        TextView txtSchoolName = (TextView) view.findViewById(R.id.txtSchoolName);
        TextView txtGvcn = (TextView) view.findViewById(R.id.txtGvcn);
        TextView txtTerm = (TextView) view.findViewById(R.id.txtClassScreenExamResults);
        WebView mWebViewDetailsScheduleview = (WebView) view.findViewById(R.id.mWebViewDetailsSchedule);
        mWebViewDetailsScheduleview.loadDataWithBaseURL(ASSETS, testDataStudentSchedule, mime, encoding, null);
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
