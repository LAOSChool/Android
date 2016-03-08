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
public class ScreenAnnouncements extends Fragment implements FragmentLifecycle {


    private int containerId;
    private String announcementId;

    public interface IScreenAnnouncements {
        void gotoScreenAnnouncementDetails();
    }

    private IScreenAnnouncements iScreenAnnouncements;

    public ScreenAnnouncements() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_announcements, container, false);
        view.findViewById(R.id.announcementdetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnnouncementId("1000");
                iScreenAnnouncements.gotoScreenAnnouncementDetails();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_attended), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenAnnouncements fragment = new ScreenAnnouncements();
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

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenAnnouncements = (IScreenAnnouncements) activity;
    }
}
