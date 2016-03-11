package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenProfile extends Fragment implements FragmentLifecycle {

    HomeActivity activity;
    int fromPosition = -1;

    public ScreenProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_profile, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (fromPosition > -1) {
            if (fromPosition == LaoSchoolShared.POSITION_SCREEN_MORE_4) {
                inflater.inflate(R.menu.menu_screen_profile, menu);
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_8) {

            }
        }else {

        }
    }

    @Override
    public void onPauseFragment() {


    }

    @Override
    public void onResumeFragment() {
        try {
            activity = (HomeActivity) getActivity();
            fromPosition = activity.beforePosition;
            if (fromPosition == LaoSchoolShared.POSITION_SCREEN_MORE_4) {

            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_8) {
                Toast.makeText(activity, activity.selectedTeacher, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenProfile();
    }
}
