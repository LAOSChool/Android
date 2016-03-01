package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenRollUp extends Fragment implements FragmentLifecycle {
    int containerId;

    public ScreenRollUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_rollup, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_rollup), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
//        menu.findItem(R.id.action_create_message).setVisible(false);
//        menu.findItem(R.id.action_cancel).setVisible(false);
//        menu.findItem(R.id.action_send_message).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static Fragment instantiate(int containerId) {
        ScreenRollUp fragment = new ScreenRollUp();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}
