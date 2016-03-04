package com.laoschool.shared;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.laoschool.R;

/**
 * Created by Hue on 3/1/2016.
 */
public class LaoSchoolShared {
    public static final String CONTAINER_ID = "container_id";
    public static final int POSITION_SCREEN_MESSAGE_0 = 0;
    public static final int POSITION_SCREEN_ANNOUNCEMENTS_1 = 1;
    public static final int POSITION_SCREEN_ATTENDED_2 = 2;
    public static final int POSITION_SCREEN_SCHEDULE_3 = 3;
    public static final int POSITION_SCREEN_MORE_4 = 4;
    public static final int POSITION_SCREEN_EXAM_RESULTS_5 = 5;
    public static final int POSITION_SCREEN_SCHOOL_RECORD_YEAR_6 = 6;
    public static final int POSITION_SCREEN_SCHOOL_INFORMATION_7 = 7;
    public static final int POSITION_SCREEN_LIST_TEACHER_8 = 8;
    public static final int POSITION_SCREEN_CREATE_MESSAGE_9 = 9;
    public static final int POSITION_SCREEN_LIST_STUDENT_10 = 10;
    public static final int POSITION_SCREEN_MARK_SCORE_STUDENT_11 = 11;
    public static final int POSITION_SCREEN_SETTING_12 = 12;
    public static final int POSITION_SCREEN_PROFILE_13 = 13;
    public static final int POSITION_SCREEN_MESSAGE_DETAILS_14 = 14;
    public static final int POSITION_SCREEN_ANNOUNCEMENT_DETAILS_15 = 15;

    public static final String ROLE_TEARCHER = "teacher";
    public static final String ROLE_STUDENT = "student";

    public static String makeFragmentTag(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }

    public static View createTabIndicator(LayoutInflater inflater, TabHost tabHost, int textResource, int iconResource) {
        View tabIndicator = inflater.inflate(R.layout.view_tab_indicator, tabHost.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.title)).setText(textResource);
        ((ImageView) tabIndicator.findViewById(R.id.icon)).setImageResource(iconResource);
        return tabIndicator;
    }


}
