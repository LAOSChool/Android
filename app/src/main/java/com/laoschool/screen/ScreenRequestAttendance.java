package com.laoschool.screen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Attendance;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tran An on 5/10/2016.
 */
public class ScreenRequestAttendance extends Fragment implements FragmentLifecycle {

    static HomeActivity homeActivity;
    static ScreenAttended scrAttended;

    ScreenRequestAttendance thiz = this;
    LinearLayout containerView;
    EditText txbDatePicker;
    EditText txbReason;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_request_attendance, container, false);
        containerView = (LinearLayout) view.findViewById(R.id.container);
        txbDatePicker = (EditText) view.findViewById(R.id.txbDatePicker);
        txbReason = (EditText) view.findViewById(R.id.txbReason);

        SimpleDateFormat sdf = new SimpleDateFormat("dd - MM - yyyy");
        String currentDateandTime = sdf.format(new Date());
        txbDatePicker.setText(currentDateandTime, TextView.BufferType.EDITABLE);

        txbDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(thiz.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        String formatDate;
                        if(selectedmonth < 9)
                            formatDate = selectedday + " - 0" + (selectedmonth+1) + " - " + selectedyear;
                        else
                            formatDate = selectedday + " - " + (selectedmonth+1) + " - " + selectedyear;
                        txbDatePicker.setText(formatDate, TextView.BufferType.EDITABLE);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

        return view;
    }

    public static Fragment instantiate(int containerId, String currentRole, HomeActivity activity, ScreenAttended screenAttended) {
        homeActivity = activity;
        scrAttended = screenAttended;
        ScreenRequestAttendance fragment = new ScreenRequestAttendance();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_create_message, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send_message:
                submitForm();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submitForm() {
        if(LaoSchoolShared.myProfile != null) {
            final ProgressDialog ringProgressDialog = ProgressDialog.show(thiz.getActivity(), "Please wait ...", "Sending ...", true);
            //Hide soft keyboard
            InputMethodManager imm = (InputMethodManager)thiz.getActivity().getSystemService(thiz.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(containerView.getWindowToken(), 0);
            //Create attendance
            Attendance attendance = new Attendance();
            attendance.setSchool_id(LaoSchoolShared.myProfile.getSchool_id());
            attendance.setClass_id(LaoSchoolShared.myProfile.getEclass().getId());
            String datePicker = txbDatePicker.getText().toString().replaceAll(" ", "");
            String datePickers[] = datePicker.split("-");
            String att_dt = datePickers[2]+"-"+datePickers[1]+"-"+datePickers[0]+" 00:00:00.0";
            attendance.setAtt_dt(att_dt);
            attendance.setStudent_id(LaoSchoolShared.myProfile.getId());
            attendance.setStudent_name(LaoSchoolShared.myProfile.getFullname());
            attendance.setState(1);
            attendance.setNotice(txbReason.getText().toString());

            LaoSchoolSingleton.getInstance().getDataAccessService().requestAttendance(attendance, new AsyncCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ringProgressDialog.dismiss();
                    scrAttended.getAttendances();
                    homeActivity._gotoPage(LaoSchoolShared.POSITION_SCREEN_ATTENDED_3);
                }

                @Override
                public void onFailure(String message) {
                    ringProgressDialog.dismiss();
                    Toast.makeText(thiz.getContext(), "Some error occur", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthFail(String message) {
                    ringProgressDialog.dismiss();
                    LaoSchoolShared.goBackToLoginPage(thiz.getContext());
                }
            });
        } else {}
    }

    @Override
    public void onPauseFragment() {
//        Toast.makeText(thiz.getContext(), "Fragment pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
//        Toast.makeText(thiz.getContext(), "Fragment resume", Toast.LENGTH_SHORT).show();
        txbReason.setText("");
        txbReason.requestFocus();
        txbReason.requestFocusFromTouch();

        //Open the soft_keyboard
//        Handler mHandler= new Handler();
//        mHandler.post(
//                new Runnable() {
//                    public void run() {
//                        InputMethodManager inputMethodManager =  (InputMethodManager)thiz.getActivity().getSystemService(thiz.getActivity().INPUT_METHOD_SERVICE);
//                        inputMethodManager.toggleSoftInputFromWindow(txbReason.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//                        txbReason.requestFocus();
//                    }
//                });
    }
}
