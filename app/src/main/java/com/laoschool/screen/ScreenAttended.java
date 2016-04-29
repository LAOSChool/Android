package com.laoschool.screen;


import android.app.Activity;
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
public class ScreenAttended extends Fragment implements FragmentLifecycle {
    int containerId;
    private String currentRole;

    public ScreenAttended() {
        // Required empty public constructor
    }

    public interface IScreenAttended {
        void gotoCreateMessageFormScreenAttended();
    }

    private IScreenAttended iScreenAttended;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.screen_attenden, container, false);
            view.findViewById(R.id.btnCreateMessge).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iScreenAttended.gotoCreateMessageFormScreenAttended();
                }
            });
            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
            Log.d(getString(R.string.title_screen_attended), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenAttended fragment = new ScreenAttended();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenAttended = (IScreenAttended) activity;
    }
}
