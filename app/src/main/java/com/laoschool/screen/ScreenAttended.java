package com.laoschool.screen;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.R;
import com.laoschool.adapter.ListAttendancesAdapter;
import com.laoschool.entities.Attendance;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenAttended extends Fragment implements FragmentLifecycle {

    private DataAccessInterface service;
    private ScreenAttended thiz = this;
    int containerId;
    private String currentRole;

    private List<Attendance> attendanceList = new ArrayList<>();
    private List<GroupAttendance> groupAttendances = new ArrayList<>();

    LinearLayout containerView;
    TextView txbTotalAbsent;
    TextView txbTotalExcused;
    TextView txbTotalNoExcused;
    RecyclerView groupAttendancesView;

//    PageFragment currentPage;

    public class GroupAttendance {
        List<Attendance> attendances = new ArrayList<>();
        String att_dt;

        public List<Attendance> getAttendances() {
            return attendances;
        }

        public void setAttendances(List<Attendance> attendances) {
            this.attendances = attendances;
        }

        public String getAtt_dt() {
            return att_dt;
        }

        public void setAtt_dt(String att_dt) {
            this.att_dt = att_dt;
        }
    }

    public ScreenAttended() {
        // Required empty public constructor
    }

    public interface IScreenAttended {
        void gotoCreateMessageFormScreenAttended();
    }

    private IScreenAttended iScreenAttended;

    protected void getAttendances() {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(this.getActivity(), "Please wait ...", "Loading ...", true);
        service.getMyAttendances("", "", new AsyncCallback<List<Attendance>>() {
            @Override
            public void onSuccess(List<Attendance> result) {
                ringProgressDialog.dismiss();
                attendanceList.clear();
                groupAttendances.clear();
                attendanceList.addAll(result);
                containerView.setVisibility(View.VISIBLE);
                setViewData();

//                if (currentPage != null)
//                    currentPage.setData(attendanceList);
            }

            @Override
            public void onFailure(String message) {
                ringProgressDialog.dismiss();
                if (thiz.getActivity() != null)
                    Toast.makeText(thiz.getActivity(), "Some error occur !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(thiz.getContext());
            }
        });
    }

    void setViewData() {
        int totalAbsent = attendanceList.size();
        int totalExcused = 0;
        int totalNoExcused = 0;

        for(Attendance attendance: attendanceList) {
            if(attendance.getExcused() == 1)
                totalExcused++;

            boolean onGroupe = false;
            for(GroupAttendance groupAttendance: groupAttendances) {
                if(attendance.getAtt_dt().equals(groupAttendance.getAtt_dt())) {
                    groupAttendance.getAttendances().add(attendance);
                    onGroupe = true;
                    break;

                }
            }
            if(onGroupe == false) {
                GroupAttendance groupAttendance = new GroupAttendance();
                groupAttendance.getAttendances().add(attendance);
                groupAttendance.setAtt_dt(attendance.getAtt_dt());
                groupAttendances.add(0, groupAttendance);
            }
        }

        totalNoExcused = totalAbsent - totalExcused;
        txbTotalAbsent.setText("Total Attendances: " + groupAttendances.size() + " (days)");
        txbTotalExcused.setText("Excused: " + totalExcused);
        txbTotalNoExcused.setText("No Reason: " + totalNoExcused);

        ListAttendancesAdapter mAdapter = new ListAttendancesAdapter(groupAttendances, thiz.getContext());
        groupAttendancesView.swapAdapter(mAdapter, false);
//        groupAttendancesView.setAdapter(mAdapter);
    }

//    public class MyPagerAdapter extends FragmentPagerAdapter {
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (position == 0)
//                return "HK I";
//            else if (position == 1)
//                return "HK II";
//            else
//                return "Over Year";
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            if (position == 0) {
//                currentPage = PageFragment.newInstance(0);
//                return currentPage;
//            } else if (position == 1) {
//                currentPage = PageFragment.newInstance(1);
//                return currentPage;
//            } else {
//                currentPage = PageFragment.newInstance(2);
//                return currentPage;
//            }
//        }
//
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//    }
//
//    public static class PageFragment extends Fragment {
//        // Store instance variables
//        private String title;
//        private int page;
//        private View thisView;
//
//        // newInstance constructor for creating fragment with arguments
//        public static PageFragment newInstance(int page) {
//            PageFragment fragmentFirst = new PageFragment();
//            Bundle args = new Bundle();
//            args.putInt("someInt", page);
////            args.putString("someTitle", title);
//            fragmentFirst.setArguments(args);
//            return fragmentFirst;
//        }
//
//        // Store instance variables based on arguments passed
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            page = getArguments().getInt("someInt", 0);
//            title = getArguments().getString("someTitle");
//        }
//
//        // Inflate the view for the fragment based on layout XML
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.attendance_pagefragment, container, false);
//            thisView = view;
//            return view;
//        }
//
//        public void setData(List<Attendance> attendances) {
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else {
//            // Inflate the layout for this fragment
//            View view = inflater.inflate(R.layout.screen_attenden, container, false);
//            view.findViewById(R.id.btnCreateMessge).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    iScreenAttended.gotoCreateMessageFormScreenAttended();
//                }
//            });
//            return view;
//        }

//        Inflate the layout for this fragment
//        view.findViewById(R.id.btnCreateMessge).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iScreenAttended.gotoCreateMessageFormScreenAttended();
//            }
//        });
//            ViewPager vpPager = (ViewPager) view.findViewById(R.id.viewPage);
//            MyPagerAdapter adapterViewPager = new MyPagerAdapter(this.getActivity().getSupportFragmentManager());
//            vpPager.setAdapter(adapterViewPager);
//
//            // Bind the tabs to the ViewPager
//            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
//            tabs.setViewPager(vpPager);

            View view = inflater.inflate(R.layout.screen_attendance, container, false);

            groupAttendancesView = (RecyclerView) view.findViewById(R.id.groupAttendancesView);
            containerView = (LinearLayout) view.findViewById(R.id.container);
            txbTotalAbsent = (TextView) view.findViewById(R.id.txbTotalAbsent);
            txbTotalExcused = (TextView) view.findViewById(R.id.txbTotalExcused);
            txbTotalNoExcused = (TextView) view.findViewById(R.id.txbTotalNoExcused);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            groupAttendancesView.setHasFixedSize(true);
            // use a linear layout manager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(thiz.getContext());
            groupAttendancesView.setLayoutManager(linearLayoutManager);

            containerView.setVisibility(View.INVISIBLE);

            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        service = DataAccessImpl.getInstance(this.getActivity());
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
            Log.d(getString(R.string.title_screen_attended), "-Container Id:" + containerId);
        }

        getAttendances();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_attendance, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_request_attendance:
                iScreenAttended.gotoCreateMessageFormScreenAttended();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (!alreadyExecuted) {
//                getAttendances();
//            }
//        }
//    }
}
