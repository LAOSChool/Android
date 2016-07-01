package com.laoschool.screen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
    RelativeLayout txbDatePicker;
    TextView txvAttDt;
    EditText txbReason;
    ImageView imgOption;

    String fromDate = "";
    String toDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_request_attendance, container, false);
        containerView = (LinearLayout) view.findViewById(R.id.container);
        txbDatePicker = (RelativeLayout) view.findViewById(R.id.txbDatePicker);
        txvAttDt = (TextView) view.findViewById(R.id.txvAttDt);
        txbReason = (EditText) view.findViewById(R.id.txbReason);
        final LinearLayout btnOption = (LinearLayout) view.findViewById(R.id.btnOption);
        imgOption = (ImageView) view.findViewById(R.id.imgOption);

        SimpleDateFormat sdf = new SimpleDateFormat("dd - MM - yyyy");
        final String currentDateandTime = sdf.format(new Date());
        txvAttDt.setText(currentDateandTime, TextView.BufferType.EDITABLE);
        txbReason.setHint(R.string.SCAttendance_AbsentReason);
        fromDate = currentDateandTime;

        txbDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(false);
            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txvAttDt.getText().toString().contains("to")) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(thiz.getContext(), btnOption);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.menu_popup_request_attendance, popup.getMenu());
                    popup.show();

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            getDatePicker(true);
                            return true;
                        }
                    });
                }  else {
                    fromDate = currentDateandTime;
                    toDate = "";
                    txvAttDt.setText(currentDateandTime);
                    txvAttDt.setTextSize(18);
                    imgOption.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

        return view;
    }

    void getDatePicker(final boolean isSendTo) {
        //To show current date in the datepicker
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePicker = new DatePickerDialog(thiz.getContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                String formatDate;
                if(selectedmonth < 9)
                    formatDate = selectedday + " - 0" + (selectedmonth+1) + " - " + selectedyear;
                else
                    formatDate = selectedday + " - " + (selectedmonth+1) + " - " + selectedyear;
                if(!isSendTo) {
                    txvAttDt.setText(formatDate);
                    fromDate = formatDate;
                    txvAttDt.setTextSize(18);
                    imgOption.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
                else {
                    toDate = formatDate;
                    imgOption.setImageResource(R.drawable.ic_close_black_24dp);
                    txvAttDt.setText(fromDate + "  to  " + formatDate);
                    txvAttDt.setTextSize(15);
                }
            }
        },mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.setTitle(R.string.SCAttendance_SelectDate);
        mDatePicker.show();
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
            final ProgressDialog ringProgressDialog = ProgressDialog.show(this.getActivity(),
                    thiz.getContext().getString(R.string.SCCommon_PleaseWait)+ " ...",
                    thiz.getContext().getString(R.string.SCCommon_Sending)+ " ...", true);
            //Hide soft keyboard
            InputMethodManager imm = (InputMethodManager)thiz.getActivity().getSystemService(thiz.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(containerView.getWindowToken(), 0);
            //Create attendance
            Attendance attendance = new Attendance();
            attendance.setSchool_id(LaoSchoolShared.myProfile.getSchool_id());
            attendance.setClass_id(LaoSchoolShared.myProfile.getEclass().getId());

            String datePicker = fromDate.replaceAll(" ", "");
            String datePickers[] = datePicker.split("-");
            String fromDt = datePickers[2] + "-" + datePickers[1] + "-" + datePickers[0];
            String toDt = "";
            if(!toDate.equals("")) {
                datePicker = toDate.replaceAll(" ", "");
                String datePickers2[] = datePicker.split("-");
                toDt = datePickers2[2] + "-" + datePickers2[1] + "-" + datePickers2[0];
            }
            else
                toDt = fromDt;
//          attendance.setAtt_dt(att_dt);
            attendance.setStudent_id(LaoSchoolShared.myProfile.getId());
            attendance.setStudent_name(LaoSchoolShared.myProfile.getFullname());
            attendance.setState(1);
            attendance.setNotice(txbReason.getText().toString());

            LaoSchoolSingleton.getInstance().getDataAccessService().requestAttendance(attendance, fromDt, toDt, new AsyncCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ringProgressDialog.dismiss();
                    scrAttended.getAttendances();
                    homeActivity._gotoPage(LaoSchoolShared.POSITION_SCREEN_ATTENDED_3);
                }

                @Override
                public void onFailure(String message) {
                    ringProgressDialog.dismiss();
                    if(message.contains("invalid"))
                        Toast.makeText(thiz.getContext(), thiz.getContext().getString(R.string.SCAttendance_NotValid), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(thiz.getContext(), thiz.getContext().getString(R.string.SCAttendance_SendFail), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onResumeFragment() {
        txbReason.setText("");
        txbReason.requestFocus();
        txbReason.requestFocusFromTouch();

        // Open the soft_keyboard
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
