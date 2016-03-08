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
public class ScreenAnnouncementDetails extends Fragment implements FragmentLifecycle {


    private int containerId;

    public ScreenAnnouncementDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_announcement_details, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_announcement_details), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);
        ScreenAnnouncements screenAnnouncements = (ScreenAnnouncements) ((HomeActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(tag);
        if (screenAnnouncements != null) {
            String messageId = screenAnnouncements.getAnnouncementId();
            Toast.makeText(getActivity(), messageId, Toast.LENGTH_SHORT).show();
        }

    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenAnnouncementDetails fragment = new ScreenAnnouncementDetails();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

}
