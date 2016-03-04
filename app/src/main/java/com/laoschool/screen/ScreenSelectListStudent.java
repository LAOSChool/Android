package com.laoschool.screen;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;


import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSelectListStudent extends Fragment implements FragmentLifecycle {


    private int containerId;

    public ScreenSelectListStudent() {
        // Required empty public constructor
    }




    interface IScreenListStudent {
        void goBackScreenCreateMessage();
    }

    private IScreenListStudent iScreenListStudent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_select_list_student, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_select_list_student), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
        inflater.inflate(R.menu.menu_screen_list_student, menu);

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenSelectListStudent fragment = new ScreenSelectListStudent();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done:
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_9);
                Log.d(getString(R.string.title_screen_select_list_student), "-TAG Screen Create Message:" + tag);
                ScreenCreateMessage screenCreateMessage = (ScreenCreateMessage) getActivity().getSupportFragmentManager().findFragmentByTag(tag);
                if (screenCreateMessage != null) {
                    screenCreateMessage.setTestMessage("selected list on List Student");
                    iScreenListStudent.goBackScreenCreateMessage();
                } else {
                   // Toast.makeText(getActivity(), "Get Create Message null", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenListStudent = (IScreenListStudent) activity;
    }

    @Override
    public void onPauseFragment() {
        Log.d(getString(R.string.title_screen_select_list_student), "onPauseFragment()");
       // Toast.makeText(getActivity(), "onPauseFragment():" + getString(R.string.title_screen_list_student), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        Log.d(getString(R.string.title_screen_select_list_student), "onPauseFragment()");
       // Toast.makeText(getActivity(), "onResumeFragment():" + getString(R.string.title_screen_list_student), Toast.LENGTH_SHORT).show();
    }



}
