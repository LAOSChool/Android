package com.laoschool.screen.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Attendance;
import com.laoschool.entities.TimeTable;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;

/**
 * Created by Tran An on 6/14/2016.
 */
public class AttendanceTableAbsent extends View {

    Context context;
    User student;
    String date;
    TimeTable timeTable;

    ImageView imgReason1;
    ImageView imgReason2;
    ImageView imgReason3;
    ImageView imgReason4;
    ImageView imgReason5;

    int selected = 1;

    public interface AttendanceTableAbsentListener {
        void onAttendanceRequestSuccess(Attendance attendance);
    }

    private AttendanceTableAbsentListener listener;

    public AttendanceTableAbsent(Context context, User student, String date, TimeTable timeTable, AttendanceTableAbsentListener listener) {
        super(context);
        this.context = context;
        this.student = student;
        this.date = date;
        this.timeTable = timeTable;
        this.listener = listener;
    }

    public View getView() {
        View view = View.inflate(context, R.layout.view_attendance_table_absent, null);

        RelativeLayout btnReason1 = (RelativeLayout) view.findViewById(R.id.btnReason1);
        RelativeLayout btnReason2 = (RelativeLayout) view.findViewById(R.id.btnReason2);
        RelativeLayout btnReason3 = (RelativeLayout) view.findViewById(R.id.btnReason3);
        RelativeLayout btnReason4 = (RelativeLayout) view.findViewById(R.id.btnReason4);
        RelativeLayout btnReason5 = (RelativeLayout) view.findViewById(R.id.btnReason5);
        imgReason1 = (ImageView) view.findViewById(R.id.imgReason1);
        imgReason2 = (ImageView) view.findViewById(R.id.imgReason2);
        imgReason3 = (ImageView) view.findViewById(R.id.imgReason3);
        imgReason4 = (ImageView) view.findViewById(R.id.imgReason4);
        imgReason5 = (ImageView) view.findViewById(R.id.imgReason5);
        final CheckBox cbFulldays = (CheckBox) view.findViewById(R.id.cbFulldays);
        Button btnSend = (Button) view.findViewById(R.id.btnSend);

        imgReason1.setColorFilter(Color.parseColor("#0099cc"));
        imgReason2.setColorFilter(Color.parseColor("#0099cc"));
        imgReason3.setColorFilter(Color.parseColor("#0099cc"));
        imgReason4.setColorFilter(Color.parseColor("#0099cc"));
        imgReason5.setColorFilter(Color.parseColor("#0099cc"));

        imgReason1.setVisibility(VISIBLE);

        btnReason1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTable();
                imgReason1.setVisibility(VISIBLE);
                selected = 1;
            }
        });

        btnReason2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTable();
                imgReason2.setVisibility(VISIBLE);
                selected = 2;
            }
        });

        btnReason3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTable();
                imgReason3.setVisibility(VISIBLE);
                selected = 3;
            }
        });

        btnReason4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTable();
                imgReason4.setVisibility(VISIBLE);
                selected = 4;
            }
        });

        btnReason5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTable();
                imgReason5.setVisibility(VISIBLE);
                selected = 5;
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
                switch (selected) {
                    case 1:
                        attendance.setExcused(0);
                        break;
                    case 2:
                        attendance.setExcused(1);
                        attendance.setNotice("Ly do 1");
                        break;
                    case 3:
                        attendance.setExcused(1);
                        attendance.setNotice("Ly do 2");
                        break;
                    case 4:
                        attendance.setExcused(1);
                        attendance.setNotice("Ly do 3");
                        break;
                    case 5:
                        attendance.setExcused(1);
                        attendance.setNotice("Ly do 4");
                        break;
                }
                if(!cbFulldays.isChecked()) {
                    attendance.setSession_id(String.valueOf(timeTable.getSession_id()));
                    attendance.setSession(timeTable.getSession_Name());
                    attendance.setSubject_id(timeTable.getSubject_id());
                    attendance.setSubject(timeTable.getSubject_Name());
                }

//                Log.i("RequestAttendance", attendance.toJson());

                final ProgressDialog ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Sending ...", true);
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
                            Toast.makeText(context, "Absent date is not valid!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, "Some error occur!", Toast.LENGTH_SHORT).show();
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

    public void clearTable() {
        imgReason1.setVisibility(INVISIBLE);
        imgReason2.setVisibility(INVISIBLE);
        imgReason3.setVisibility(INVISIBLE);
        imgReason4.setVisibility(INVISIBLE);
        imgReason5.setVisibility(INVISIBLE);
    }

}
