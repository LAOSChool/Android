package com.laoschool.shared;

/**
 * Created by Hue on 3/1/2016.
 */
public class LaoSchoolShared {
    public static final String CONTAINER_ID = "container_id";
    public static final int POSITION_SCREEN_MESSAGE_0 = 0;
    public static final int POSITION_SCREEN_ROLL_UP_1 = 1;
    public static final int POSITION_SCREEN_EXAM_RESULTS_2 = 2;
    public static final int POSITION_SCREEN_SCHEDULE_3 = 3;
    public static final int POSITION_SCREEN_INFORMATION_4 = 4;
    public static final int POSITION_SCREEN_SCHOOL_RECORD_YEAR_5 = 5;
    public static final int POSITION_SCREEN_SCHOOL_INFORMATION_6 = 6;
    public static final int POSITION_SCREEN_LIST_TEACHER_7 = 7;
    public static final int POSITION_SCREEN_CREATE_MESSAGE_8 = 8;
    public static final int POSITION_SCREEN_LIST_STUDENT_9 = 9;
    public static final int POSITION_SCREEN_MARK_SCORE_STUDENT_10 = 10;
    public static final int POSITION_SCREEN_SETTING_11 = 11;
    public static final int POSITION_SCREEN_PROFILE_12 = 12;

    public static String makeFragmentTag(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }


}
