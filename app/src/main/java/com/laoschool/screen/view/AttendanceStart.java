package com.laoschool.screen.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.User;

/**
 * Created by Tran An on 6/3/2016.
 */
public class AttendanceStart extends View {

    Context context;
    User thisStudent;

    TextView txtStudentName;
    TextView txtPhone;
    RelativeLayout formExcused;

    public interface AttendanceStartListener {
        void onPresentClick(User student);
    }

    private AttendanceStartListener listener;

    public AttendanceStart(Context context, AttendanceStartListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public View getView(String title) {
        final View v = View.inflate(context, R.layout.view_attendance_start, null);

        TextView txtHeaderTitle = (TextView) v.findViewById(R.id.txtHeaderTitle);
        txtStudentName = (TextView) v.findViewById(R.id.txtStudentName);
        txtPhone = (TextView) v.findViewById(R.id.txtPhone);
        LinearLayout btnPresent = (LinearLayout) v.findViewById(R.id.btnPresent);
        LinearLayout btnAbsence = (LinearLayout) v.findViewById(R.id.btnAbsence);
        formExcused = (RelativeLayout) v.findViewById(R.id.formExcused);

        txtHeaderTitle.setText(title);

        btnPresent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onPresentClick(thisStudent);
            }
        });

        btnAbsence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                formExcused.setVisibility(VISIBLE);
            }
        });

        return v;
    }

    public void setStudent(User student) {
        thisStudent = student;
        formExcused.setVisibility(GONE);
        txtStudentName.setText(student.getFullname());
        txtPhone.setText("Phone: "+ student.getPhone());
    }

}
