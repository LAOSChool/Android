package com.laoschool.screen.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ListAttendanceReasonAdapter;
import com.laoschool.adapter.ListStudentsAdapter;
import com.laoschool.entities.Attendance;
import com.laoschool.entities.AttendanceReason;
import com.laoschool.entities.TimeTable;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 6/14/2016.
 */
public class AttendanceTableAbsent extends View {

    AttendanceTableAbsent thiz = this;
    Context context;
    User student;
    String date;
    TimeTable timeTable;

    RecyclerView tableAttReasonView;
    ListAttendanceReasonAdapter mAdapter;
    List<AttendanceReason> attendanceReasons = new ArrayList<>();

    ScrollView scrollView;
    LinearLayout linearContent;

//    ImageView imgReason1;
//    ImageView imgReason2;
//    ImageView imgReason3;
//    ImageView imgReason4;
//    ImageView imgReason5;

    int selectedIndex = 0;

    public interface AttendanceTableAbsentListener {
        void onAttendanceRequestSuccess(Attendance attendance);
    }

    private AttendanceTableAbsentListener listener;

    public AttendanceTableAbsent(final Context context, User student, String date, TimeTable timeTable, AttendanceTableAbsentListener listener) {
        super(context);
        this.context = context;
        this.student = student;
        this.date = date;
        this.timeTable = timeTable;
        this.listener = listener;
    }

    public View getView() {
        View view = View.inflate(context, R.layout.view_attendance_table_absent, null);

        TextView txvCreateAbsent = (TextView) view.findViewById(R.id.txvCreateAbsent);
//        TextView txvNoReason = (TextView) view.findViewById(R.id.txvNoReason);
//        RelativeLayout btnReason1 = (RelativeLayout) view.findViewById(R.id.btnReason1);
//        RelativeLayout btnReason2 = (RelativeLayout) view.findViewById(R.id.btnReason2);
//        RelativeLayout btnReason3 = (RelativeLayout) view.findViewById(R.id.btnReason3);
//        RelativeLayout btnReason4 = (RelativeLayout) view.findViewById(R.id.btnReason4);
//        RelativeLayout btnReason5 = (RelativeLayout) view.findViewById(R.id.btnReason5);
//        imgReason1 = (ImageView) view.findViewById(R.id.imgReason1);
//        imgReason2 = (ImageView) view.findViewById(R.id.imgReason2);
//        imgReason3 = (ImageView) view.findViewById(R.id.imgReason3);
//        imgReason4 = (ImageView) view.findViewById(R.id.imgReason4);
//        imgReason5 = (ImageView) view.findViewById(R.id.imgReason5);
        final CheckBox cbFulldays = (CheckBox) view.findViewById(R.id.cbFulldays);
        Button btnSend = (Button) view.findViewById(R.id.btnSend);
        tableAttReasonView = (RecyclerView) view.findViewById(R.id.tableAttReason);
        final EditText edtOtherReason = (EditText) view.findViewById(R.id.edtOtherReason);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        linearContent = (LinearLayout) view.findViewById(R.id.linearContent);

        // use a linear layout manager
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        tableAttReasonView.setLayoutManager(mLayoutManager);

//        imgReason1.setColorFilter(Color.parseColor("#0099cc"));
//        imgReason2.setColorFilter(Color.parseColor("#0099cc"));
//        imgReason3.setColorFilter(Color.parseColor("#0099cc"));
//        imgReason4.setColorFilter(Color.parseColor("#0099cc"));
//        imgReason5.setColorFilter(Color.parseColor("#0099cc"));

        txvCreateAbsent.setText(context.getString(R.string.SCAttendance_TeacherCreate));
//        txvNoReason.setText(context.getString(R.string.SCAttendance_NoReason));
        cbFulldays.setText(context.getString(R.string.SCAttendance_Fulldays));
        btnSend.setText(context.getString(R.string.SCCommon_Send));

//        imgReason1.setVisibility(VISIBLE);
//
//        btnReason1.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearTable();
//                imgReason1.setVisibility(VISIBLE);
//                selected = 1;
//            }
//        });
//
//        btnReason2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearTable();
//                imgReason2.setVisibility(VISIBLE);
//                selected = 2;
//            }
//        });
//
//        btnReason3.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearTable();
//                imgReason3.setVisibility(VISIBLE);
//                selected = 3;
//            }
//        });
//
//        btnReason4.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearTable();
//                imgReason4.setVisibility(VISIBLE);
//                selected = 4;
//            }
//        });
//
//        btnReason5.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearTable();
//                imgReason5.setVisibility(VISIBLE);
//                selected = 5;
//            }
//        });

        edtOtherReason.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                selectedIndex = attendanceReasons.size();
                mAdapter.swap(attendanceReasons, selectedIndex);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 400);

                layoutParams.setMargins(0, 0, 0, 140);
                scrollView.setLayoutParams(layoutParams);
                return false;
            }
        });

        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create attendance
                final Attendance attendance = new Attendance();
                attendance.setSchool_id(LaoSchoolShared.myProfile.getSchool_id());
                attendance.setClass_id(LaoSchoolShared.myProfile.getEclass().getId());
                //Format date
                String datePicker = date.replaceAll(" ", "");
                String datePickers[] = datePicker.split("-");
                String fromDt = datePickers[2] + "-" + datePickers[1] + "-" + datePickers[0];
                attendance.setAtt_dt(fromDt);
                attendance.setStudent_id(student.getId());
                attendance.setStudent_name(student.getFullname());
                attendance.setState(1);
                if(selectedIndex == 0)
                    attendance.setExcused(0);
                else if(selectedIndex == attendanceReasons.size()) {
                    attendance.setExcused(1);
                    attendance.setNotice(edtOtherReason.getText().toString());
                }
                else {
                    attendance.setExcused(1);
                    attendance.setNotice(attendanceReasons.get(selectedIndex).getSval());
                }
//                switch (selected) {
//                    case 1:
//                        attendance.setExcused(0);
//                        break;
//                    case 2:
//                        attendance.setExcused(1);
//                        attendance.setNotice("Reason 1");
//                        break;
//                    case 3:
//                        attendance.setExcused(1);
//                        attendance.setNotice("Reason 2");
//                        break;
//                    case 4:
//                        attendance.setExcused(1);
//                        attendance.setNotice("Reason 3");
//                        break;
//                    case 5:
//                        attendance.setExcused(1);
//                        attendance.setNotice("Reason 4");
//                        break;
//                }
                if(!cbFulldays.isChecked()) {
                    attendance.setSession_id(String.valueOf(timeTable.getSession_id()));
                    attendance.setSession(timeTable.getSession_Name());
                    attendance.setSubject_id(timeTable.getSubject_id());
                    attendance.setSubject(timeTable.getSubject_Name());
                }

//                Log.i("RequestAttendance", attendance.toJson());

                final ProgressDialog ringProgressDialog = ProgressDialog.show(context,
                        context.getString(R.string.SCCommon_PleaseWait)+ " ...",
                        context.getString(R.string.SCCommon_Sending)+ " ...", true);
                LaoSchoolSingleton.getInstance().getDataAccessService().createAttendance(attendance, new AsyncCallback<Attendance>() {
                    @Override
                    public void onSuccess(Attendance result) {
                        ringProgressDialog.dismiss();
                        if(listener != null)
                            listener.onAttendanceRequestSuccess(result);
                    }

                    @Override
                    public void onFailure(String message) {
                        ringProgressDialog.dismiss();
                        if(message.contains("invalid"))
                            Toast.makeText(context, context.getString(R.string.SCAttendance_NotValid), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getString(R.string.SCCommon_UnknowError), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthFail(String message) {
                        ringProgressDialog.dismiss();
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                });
            }
        });

        return view;
    }

    public void setListAttendanceReason(List<AttendanceReason> listAttendanceReason) {
        attendanceReasons.clear();
        attendanceReasons.addAll(listAttendanceReason);
        mAdapter = new ListAttendanceReasonAdapter(thiz, attendanceReasons, selectedIndex, context);
        tableAttReasonView.setAdapter(mAdapter);
    }

    public void setReasonSelected(int index) {
        selectedIndex = index;
        mAdapter.swap(attendanceReasons, selectedIndex);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 690);

        layoutParams.setMargins(0, 0, 0, 140);
        scrollView.setLayoutParams(layoutParams);

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearContent.getWindowToken(), 0);
        linearContent.requestFocus();
    }

//    public void clearTable() {
//        imgReason1.setVisibility(INVISIBLE);
//        imgReason2.setVisibility(INVISIBLE);
//        imgReason3.setVisibility(INVISIBLE);
//        imgReason4.setVisibility(INVISIBLE);
//        imgReason5.setVisibility(INVISIBLE);
//    }

}
