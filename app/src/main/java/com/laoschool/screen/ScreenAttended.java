package com.laoschool.screen;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.adapter.ListAttendanceTableAdapter;
import com.laoschool.adapter.ListAttendancesAdapter;
import com.laoschool.entities.Attendance;
import com.laoschool.entities.AttendanceRollup;
import com.laoschool.entities.TimeTable;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.screen.view.AttendanceStart;
import com.laoschool.screen.view.Languages;
import com.laoschool.screen.view.TableSubject;
import com.laoschool.screen.widgets.MyEditText;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenAttended extends Fragment implements FragmentLifecycle {

    private DataAccessInterface service;
    private ScreenAttended thiz = this;
    int containerId;
    private String currentRole;

    //Data for student
    private List<Attendance> attendanceList = new ArrayList<>();
    private List<GroupAttendance> groupAttendances = new ArrayList<>();

    //Data for teacher
    private AttendanceRollup attendanceRollup;
    private TimeTable selectedTimetable;

    ListAttendancesAdapter mAdapter;
    LinearLayout containerView;
    LinearLayout emptyView;
    LinearLayout btnReload;
    TextView txbTotalFullday;
    TextView txbTotalExcused1;
    TextView txbTotalNoExcused1;
    TextView txbTotalSession;
    TextView txbTotalExcused2;
    TextView txbTotalNoExcused2;
    RecyclerView groupAttendancesView;
    SwipeRefreshLayout attendancesRefreshLayout;
    ProgressDialog ringProgressDialog;

    RecyclerView tableStudentView;
    ListAttendanceTableAdapter mAdapterTeacherAttendance;
    TextView txtAttendanceDate;
    LinearLayout formHeader;
    RelativeLayout btnScrolldown;

    boolean isLoad = false;

    AttendanceStart attendanceStart;

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
        void gotoCreateAttendanceFormScreenAttendance();
        void goToCreateMessagefromScreenAttendance(List<User> students, List<User> selectedStudents, String defaultText);
    }

    private IScreenAttended iScreenAttended;

    protected void getAttendances() {
        isLoad = true;
        ringProgressDialog = new ProgressDialog(thiz.getContext());
        if(attendancesRefreshLayout != null && attendancesRefreshLayout.isRefreshing() == false)
            ringProgressDialog = ringProgressDialog.show(this.getActivity(),
                    thiz.getContext().getString(R.string.SCCommon_PleaseWait)+ " ...",
                    thiz.getContext().getString(R.string.SCCommon_Loading)+ " ...", true);
        service.getMyAttendances("", "", new AsyncCallback<List<Attendance>>() {
            @Override
            public void onSuccess(List<Attendance> result) {
                ringProgressDialog.dismiss();
                attendancesRefreshLayout.setRefreshing(false);
                attendanceList.clear();
                groupAttendances.clear();

                if(!result.isEmpty()) {
                    attendanceList.addAll(result);
                    containerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    setViewData();
                }
                else {
                    containerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }

//                if (currentPage != null)
//                    currentPage.setData(attendanceList);
            }

            @Override
            public void onFailure(String message) {
                ringProgressDialog.dismiss();
                attendancesRefreshLayout.setRefreshing(false);
                containerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                if (thiz.getActivity() != null)
                    Toast.makeText(thiz.getActivity(), thiz.getContext().getString(R.string.SCCommon_UnknowError), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(thiz.getContext());
            }
        });
    }

    protected void getAttendanceRollup(int class_id, String date) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(this.getActivity(),
                thiz.getContext().getString(R.string.SCCommon_PleaseWait)+ " ...",
                thiz.getContext().getString(R.string.SCCommon_Sending)+ " ...", true);
        service.rollupAttendance(class_id, date, new AsyncCallback<AttendanceRollup>() {
            @Override
            public void onSuccess(AttendanceRollup result) {
                ringProgressDialog.dismiss();
                attendanceRollup = result;
                if(mAdapterTeacherAttendance == null) {
                    mAdapterTeacherAttendance = new ListAttendanceTableAdapter(iScreenAttended, result.getStudents(), null, null, thiz.getContext(), txtAttendanceDate.getText().toString());
                    tableStudentView.setAdapter(mAdapterTeacherAttendance);
                } else
                    mAdapterTeacherAttendance.swap(result.getStudents(), null, null, txtAttendanceDate.getText().toString());
            }

            @Override
            public void onFailure(String message) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(thiz.getContext());
            }
        });
    }

    void setViewData() {
        int totalFullday = 0;
        int totalExcused1 = 0;
        int totalNoExcused1 = 0;
        int totalSession = 0;
        int totalExcused2 = 0;
        int totalNoExcused2 = 0;

        for(Attendance attendance: attendanceList) {
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

        for(GroupAttendance groupAttendance: groupAttendances) {
            if(groupAttendance.getAttendances().size() == 1) {
                totalFullday++;
                if(groupAttendance.getAttendances().get(0).getExcused() == 1)
                    totalExcused1++;
                else
                    totalNoExcused1++;
            } else {
                for(Attendance attendance: groupAttendance.getAttendances()) {
                    totalSession++;
                    if(attendance.getExcused() == 1)
                        totalExcused2++;
                    else
                        totalNoExcused2++;
                }
            }
        }

        txbTotalFullday.setText(thiz.getContext().getString(R.string.SCAttendance_Fulldays)+ " (" + totalFullday + ")");
        txbTotalExcused1.setText(totalExcused1 + "");
        txbTotalNoExcused1.setText(totalNoExcused1 + "");
        txbTotalSession.setText(thiz.getContext().getString(R.string.SCAttendance_Sessions)+ " (" + totalSession + ")");
        txbTotalExcused2.setText(totalExcused2 + "");
        txbTotalNoExcused2.setText(totalNoExcused2 + "");

        if(mAdapter == null) {
            ListAttendancesAdapter mAdapter = new ListAttendancesAdapter(groupAttendances, thiz.getContext());
            groupAttendancesView.setAdapter(mAdapter);
        } else
            mAdapter.swap(groupAttendances);
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
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                return _defineScreenTeacher(inflater, container);
            } else {
                return _defineScreenStudent(inflater, container);
            }
        }
    }

    private View _defineScreenTeacher(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_attendance_teacher, container, false);

        txtAttendanceDate = (TextView) view.findViewById(R.id.txtAttendanceDate);
        final TextView txtClassName = (TextView) view.findViewById(R.id.txtClassName);
        final TextView txtSession = (TextView) view.findViewById(R.id.txtSession);
        ImageView imgClass = (ImageView) view.findViewById(R.id.imgClass);
        ImageView imgAttDt = (ImageView) view.findViewById(R.id.imgAttDt);
        ImageView imgSession = (ImageView) view.findViewById(R.id.imgSession);
        ImageView imgDropdown1 = (ImageView) view.findViewById(R.id.imgDropdown1);
        ImageView imgDropdown2 = (ImageView) view.findViewById(R.id.imgDropdown2);
        RelativeLayout btnAttendanceDate = (RelativeLayout) view.findViewById(R.id.btnAttendanceDate);
        RelativeLayout btnSession = (RelativeLayout) view.findViewById(R.id.btnSession);
        tableStudentView = (RecyclerView) view.findViewById(R.id.tableStudentView);
        formHeader = (LinearLayout) view.findViewById(R.id.formHeader);
        btnScrolldown = (RelativeLayout) view.findViewById(R.id.btnScrolldown);
        final TextView txtClassN = (TextView) view.findViewById(R.id.txtClassN);
        final TextView txtSubjectN = (TextView) view.findViewById(R.id.txtSubjectN);
        final RelativeLayout btnStartAttendance = (RelativeLayout) view.findViewById(R.id.btnStartAttendance);
        final MyEditText edtSearch = (MyEditText) view.findViewById(R.id.edtSearch);
        TextView txvListStudent = (TextView) view.findViewById(R.id.txvListStudent);

//        imgClass.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
//        imgAttDt.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
//        imgSession.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
        imgDropdown1.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
        imgDropdown2.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));

        SimpleDateFormat sdf = new SimpleDateFormat("dd - MM - yyyy");
        final String currentDateandTime = sdf.format(new Date());
        txtAttendanceDate.setText(currentDateandTime);
        txtClassName.setText(thiz.getContext().getString(R.string.SCCommon_Class)+ " "+ LaoSchoolShared.selectedClass.getTitle());
        txtSession.setText(R.string.SCAttendance_ChoseSubjects);
        txvListStudent.setText(R.string.SCAttendance_ListStudents);
        SharedPreferences prefs = thiz.getActivity().getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String language = prefs.getString(Languages.PREFERENCES_NAME, null);
        if(language != null && language.equals(Languages.LANGUAGE_LAOS)) {
            txvListStudent.setTextSize(13);
        } else {
            txvListStudent.setTextSize(14);
        }
        edtSearch.setHint(R.string.SCCommon_Search);

        // use a linear layout manager
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(thiz.getContext());
        tableStudentView.setLayoutManager(mLayoutManager);
        tableStudentView.setHasFixedSize(true);

        btnAttendanceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DateFormat df = new SimpleDateFormat("dd - MM - yyyy");
                    Calendar mcurrentDate = Calendar.getInstance();
                    mcurrentDate.setTime(df.parse(txtAttendanceDate.getText().toString()));
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker=new DatePickerDialog(thiz.getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // TODO Auto-generated method stub
                            String formatDate;
                            String sendDate;
                            if(selectedmonth < 9) {
                                formatDate = selectedday + " - 0" + (selectedmonth + 1) + " - " + selectedyear;
                                sendDate = selectedyear + "-0" + (selectedmonth + 1) + "-" + selectedday;
                            }
                            else {
                                formatDate = selectedday + " - " + (selectedmonth + 1) + " - " + selectedyear;
                                sendDate = selectedyear + "-" + (selectedmonth + 1) + "-" + selectedday;
                            }
                            if(!formatDate.equals(txtAttendanceDate.getText().toString())) {
                                txtAttendanceDate.setText(formatDate);
                                txtSession.setText(R.string.SCAttendance_ChoseSubjects);
                                edtSearch.getText().clear();
                                getAttendanceRollup(LaoSchoolShared.selectedClass.getId(), sendDate);
                            }
                        }
                    },mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    mDatePicker.setTitle(R.string.SCAttendance_SelectDate);
                    mDatePicker.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(thiz.getContext()).create();
                final TableSubject tableSubject = new TableSubject(thiz.getContext(), new TableSubject.TableSubjectListener() {
                    @Override
                    public void onSelectSubject(TimeTable timeTable) {
                        selectedTimetable = timeTable;
                        txtSession.setText(thiz.getContext().getString(R.string.SCAttendance_Sessions)+ " "+ (attendanceRollup.getTimetables().indexOf(timeTable)+1)+ " - "+ timeTable.getSubject_Name());
//                        formHeader.setVisibility(View.GONE);
//                        btnScrolldown.setVisibility(View.VISIBLE);
                        txtClassN.setText(txtClassName.getText());
                        txtSubjectN.setText(thiz.getContext().getString(R.string.SCAttendance_Subjects)+ " "+ timeTable.getSubject_Name());
                        dialog.dismiss();
                        mAdapterTeacherAttendance.swap(attendanceRollup.getStudents(), attendanceRollup.getAttendances(), timeTable, txtAttendanceDate.getText().toString());
                        tableStudentView.smoothScrollToPosition(0);
                        edtSearch.getText().clear();
                    }
                });

                dialog.setView(tableSubject.getView());
                tableSubject.setListSubject(attendanceRollup.getTimetables());
                dialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 380, getResources().getDisplayMetrics());
                lp.height = height;
//                lp.dimAmount = 1.0f;
                dialog.getWindow().setAttributes(lp);
            }
        });

        btnScrolldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScrolldown.setVisibility(View.GONE);
                formHeader.setVisibility(View.VISIBLE);
            }
        });

        btnStartAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(thiz.getContext()).create();
                attendanceStart = new AttendanceStart(thiz.getContext(), new AttendanceStart.AttendanceStartListener() {
                    @Override
                    public void onPresentClick(User student) {
                        dialog.dismiss();
                        int index = attendanceRollup.getStudents().indexOf(student);
                        if(index != attendanceRollup.getStudents().size()-1) {
                            attendanceStart.setStudent(attendanceRollup.getStudents().get(index + 1));
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            }, 300);
                        }
                        else {
                            btnStartAttendance.setVisibility(View.GONE);
//                            mAdapterTeacherAttendance.swap();

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            params.setMargins(0, 0, 0, 0);
                            tableStudentView.setLayoutParams(params);
                        }
                    }
                });
                dialog.setView(attendanceStart.getView(txtClassName.getText()+ " - Tiet "+ selectedTimetable.getSubject_Name()));
                attendanceStart.setStudent(attendanceRollup.getStudents().get(0));
                dialog.show();
            }
        });

        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if((event.getRawX() - 30) <= (edtSearch.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        edtSearch.getText().clear();
                        formHeader.setVisibility(View.VISIBLE);
                        formHeader.requestFocus();
                        InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        return true;
                    } else {
//                        HomeActivity homeActivity = (HomeActivity) thiz.getActivity();
//                        homeActivity.hideBottomBar();
                        formHeader.setVisibility(View.GONE);
                    }
                }

                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 /*This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after. It is an error to attempt to make changes to s from this callback.*/
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                 Log.i("edtSearch", "key press!");
                if(!edtSearch.getText().toString().isEmpty())
                    edtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                else
                    edtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);

                List<User> findUsers = new ArrayList<User>();
                for(User student: attendanceRollup.getStudents()) {
                    if(student.getFullname().toLowerCase().contains(edtSearch.getText().toString()))
                        findUsers.add(student);
                }
                mAdapterTeacherAttendance.swap(findUsers, attendanceRollup.getAttendances(), selectedTimetable, txtAttendanceDate.getText().toString());
            }
        });

        edtSearch.setListener(new MyEditText.MyEditTextListener() {
            @Override
            public void onHideKeyboard() {
                formHeader.setVisibility(View.VISIBLE);
                formHeader.requestFocus();
            }
        });

        return view;
    }

    private View _defineScreenStudent(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_attendance, container, false);

        groupAttendancesView = (RecyclerView) view.findViewById(R.id.groupAttendancesView);
        containerView = (LinearLayout) view.findViewById(R.id.container);
        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        btnReload = (LinearLayout) view.findViewById(R.id.btnReload);
        txbTotalFullday = (TextView) view.findViewById(R.id.txbTotalFullday);
        txbTotalExcused1 = (TextView) view.findViewById(R.id.txbTotalExcused1);
        txbTotalNoExcused1 = (TextView) view.findViewById(R.id.txbTotalNoExcused1);
        txbTotalSession = (TextView) view.findViewById(R.id.txbTotalSession);
        txbTotalExcused2 = (TextView) view.findViewById(R.id.txbTotalExcused2);
        txbTotalNoExcused2 = (TextView) view.findViewById(R.id.txbTotalNoExcused2);
        attendancesRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.attendancesRefreshLayout);
        TextView txvExcused = (TextView) view.findViewById(R.id.txvExcused);
        TextView txvNoExcused = (TextView) view.findViewById(R.id.txvNoExcused);
        TextView txvNoData = (TextView) view.findViewById(R.id.txvNoData);
        TextView txvReload = (TextView) view.findViewById(R.id.txvReload);

        txbTotalFullday.setText(R.string.SCAttendance_Fulldays);
        txbTotalSession.setText(R.string.SCAttendance_Sessions);
        txvExcused.setText(R.string.SCAttendance_Excused);
        txvNoExcused.setText(R.string.SCAttendance_NoExcused);
        txvNoData.setText(R.string.SCAttendance_NoAbsent);
        txvReload.setText(R.string.SCCommon_Reload);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        groupAttendancesView.setHasFixedSize(false);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(thiz.getContext());
        groupAttendancesView.setLayoutManager(linearLayoutManager);

        containerView.setVisibility(View.INVISIBLE);

        attendancesRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAttendances();
            }
        });

        return view;
    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                getAttendances();
            }
        }, 500);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        service = DataAccessImpl.getInstance(this.getActivity());
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
            Log.d(getString(R.string.SCCommon_Attendance), "-Container Id:" + containerId);
        }

//        getAttendances();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if(currentRole.equals(LaoSchoolShared.ROLE_STUDENT))
            inflater.inflate(R.menu.menu_screen_attendance, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_request_attendance:
                if(currentRole.equals(LaoSchoolShared.ROLE_STUDENT))
                    iScreenAttended.gotoCreateAttendanceFormScreenAttendance();
                else {
                    if(selectedTimetable != null) {
                        if(attendanceRollup != null) {
                            List<User> selectedStudents = new ArrayList<>();
                            selectedStudents.addAll(attendanceRollup.getStudents());
                            for (User student : attendanceRollup.getStudents()) {
                                for (Attendance attendance : attendanceRollup.getAttendances())
                                    if (attendance.getStudent_id() == student.getId() &&
                                            (attendance.getSession_id() == null || attendance.getSession_id().equals(String.valueOf(selectedTimetable.getSession_id())))) {
                                        selectedStudents.remove(student);
                                        break;
                                    }
                            }
                            String defaultText = "* " + thiz.getContext().getString(R.string.SCCommon_Date) + " " +
                                    txtAttendanceDate.getText().toString() + " * " + "\r\n \r\n" +
                                    thiz.getContext().getString(R.string.SCAttendance_Subjects)+ " " + selectedTimetable.getSubject_Name() + ", \r\n \r\n" +
                                    thiz.getContext().getString(R.string.SCAttendance_DefaultMessage1);
                            iScreenAttended.goToCreateMessagefromScreenAttendance(attendanceRollup.getStudents(), selectedStudents, defaultText);
                        } else
                            Toast.makeText(thiz.getContext(), thiz.getContext().getString(R.string.SCCommon_UnknowError), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(thiz.getContext(), thiz.getContext().getString(R.string.SCAttendance_NeedChoseSubject), Toast.LENGTH_SHORT).show();
                }
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
        if (!isLoad && getUserVisibleHint()) {
            if(currentRole.equals(LaoSchoolShared.ROLE_STUDENT))
                getAttendances();
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDateandTime = sdf.format(new Date());
                if(attendanceRollup == null)
                    getAttendanceRollup(LaoSchoolShared.selectedClass.getId(), currentDateandTime);
            }
        }
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
