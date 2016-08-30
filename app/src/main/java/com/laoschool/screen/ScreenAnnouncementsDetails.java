package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.laoschool.R;
import com.laoschool.adapter.NotificationDetailsAdapter;
import com.laoschool.entities.Message;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenAnnouncementsDetails extends Fragment implements FragmentLifecycle {


    private static final String TAG = ScreenAnnouncementsDetails.class.getSimpleName();
    private int containerId;


    private RecyclerView mReclerViewListImageNotification;
    private Context context;
    private String currentRole;
    private FirebaseAnalytics mFirebaseAnalytics;

    public ScreenAnnouncementsDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
            Log.d(TAG, "-Container Id:" + containerId);
        }
        this.context = getActivity();

        //Log fire base analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(TAG, bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.screen_announcements_details, container, false);
            mReclerViewListImageNotification = (RecyclerView) view.findViewById(R.id.mReclerViewListImageNotification);
            mReclerViewListImageNotification.setLayoutManager(new LinearLayoutManager(context));

            LinearLayoutManager layoutManager = (LinearLayoutManager) mReclerViewListImageNotification
                    .getLayoutManager();
            layoutManager.scrollToPositionWithOffset(0, 0);

            return view;
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
        try {
            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_ANNOUNCEMENTS_1);
            ScreenAnnouncements screenAnnouncements = (ScreenAnnouncements) ((HomeActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(tag);
            if (screenAnnouncements != null) {
                final Message notification = screenAnnouncements.getNotification();
                
                //Log fire base analytic
                Bundle bundle = new Bundle();
                bundle.putString("view_annoucement_id", String.valueOf(notification.getId()));
                mFirebaseAnalytics.logEvent(TAG, bundle);

                //set Title Notification
                ((HomeActivity) getActivity()).getSupportActionBar().setTitle(StringEscapeUtils.unescapeJava(notification.getTitle()));

                //Log.d(TAG, "Size image:" + notification.getNotifyImages().size());
                NotificationDetailsAdapter notificationDetailsAdapter = new NotificationDetailsAdapter(getActivity(), notification);
                mReclerViewListImageNotification.setAdapter(notificationDetailsAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenAnnouncementsDetails fragment = new ScreenAnnouncementsDetails();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
