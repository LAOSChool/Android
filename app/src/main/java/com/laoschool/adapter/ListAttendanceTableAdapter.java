package com.laoschool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Attendance;
import com.laoschool.entities.AttendanceReason;
import com.laoschool.entities.TimeTable;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.screen.ScreenAttended;
import com.laoschool.screen.view.AttendanceTableAbsent;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tran An on 6/2/2016.
 */
public class ListAttendanceTableAdapter extends RecyclerView.Adapter<ListAttendanceTableAdapter.ViewHolder> {

    private List<User> mDataset;
    private List<Attendance> mDataset2;
    private TimeTable timeTable;
    private String date;
    private Context context;

    private ScreenAttended.IScreenAttended iScreenAttended;

    List<AttendanceReason> attendanceReasons = new ArrayList<>();

    boolean showdata = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAttendanceTableAdapter(ScreenAttended.IScreenAttended iScreenAttended, List<User> listStudent, List<Attendance> listAttendance, TimeTable timeTable, Context context, String date) {
        mDataset = listStudent;
        mDataset2 = listAttendance;
        this.timeTable = timeTable;
        this.date = date;
        this.context = context;
        this.iScreenAttended = iScreenAttended;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAttendanceTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendance_table, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View v = holder.mView;

        final TextView txtStudentName = (TextView) v.findViewById(R.id.txtStudentName);
        ImageView icDH = (ImageView) v.findViewById(R.id.icDH);
        ImageView icCP = (ImageView) v.findViewById(R.id.icCP);
        ImageView icKP = (ImageView) v.findViewById(R.id.icKP);
        final RelativeLayout attendance_row = (RelativeLayout) v.findViewById(R.id.attendance_row);
        LinearLayout bottom_wrapper = (LinearLayout) v.findViewById(R.id.bottom_wrapper);
        RelativeLayout btnAbsent = (RelativeLayout) v.findViewById(R.id.btnAbsent);
        RelativeLayout btnInform = (RelativeLayout) v.findViewById(R.id.btnInform);
        final TextView txvAbsent = (TextView) v.findViewById(R.id.txvAbsent);
        final TextView txvInform = (TextView) v.findViewById(R.id.txvInform);
        final ImageView imgStudent = (ImageView) v.findViewById(R.id.imgStudent);
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        final User student = mDataset.get(position);

        txtStudentName.setText(student.getFullname() + " - " + student.getNickname());

//        Random rand = new Random();;
//        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
//        if(showdata) {
//            if(randomNum == 1)
//                icDH.setVisibility(View.VISIBLE);
//            if(randomNum == 2)
//                icCP.setVisibility(View.VISIBLE);
//            if(randomNum == 3)
//                icKP.setVisibility(View.VISIBLE);
//        }

        attendance_row.setBackgroundColor(context.getResources().getColor(R.color.color_bg_un_read));
        txvInform.setText(context.getString(R.string.SCAttendance_Inform));
        txvAbsent.setText(context.getString(R.string.SCAttendance_Absent));
        btnAbsent.setBackgroundColor(Color.parseColor("#37000000"));
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 141, context.getResources().getDisplayMetrics());
        bottom_wrapper.setLayoutParams(new FrameLayout.LayoutParams(width,
                LinearLayout.LayoutParams.MATCH_PARENT));
        btnInform.setVisibility(View.VISIBLE);

        if (mDataset2 != null) {
            for (Attendance attendance : mDataset2) {
                if (timeTable != null) {
                    if (attendance.getStudent_id() == student.getId() &&
                            (attendance.getSession_id() == null || attendance.getSession_id().equals(String.valueOf(timeTable.getSession_id())))) {
                        attendance_row.setBackgroundColor(context.getResources().getColor(R.color.color_bg_read));
                        txvAbsent.setText(context.getString(R.string.SCCommon_Cancel));
                        txvAbsent.setTextColor(Color.parseColor("#80000000"));
                        btnAbsent.setBackgroundColor(context.getResources().getColor(R.color.color_bg_un_read));
                        btnInform.setVisibility(View.GONE);
                        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 71, context.getResources().getDisplayMetrics());
                        bottom_wrapper.setLayoutParams(new FrameLayout.LayoutParams(width,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        break;
                    }
                }
            }
        }

        attendance_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataset2 != null) {
                    for (Attendance attendance : mDataset2) {
                        if (timeTable != null) {
                            if (attendance.getStudent_id() == student.getId() &&
                                    (attendance.getSession_id() == null || attendance.getSession_id().equals(String.valueOf(timeTable.getSession_id())))) {
                                openAttendanceDetail(attendance);
                                break;
                            }
                        }
                    }
                }
            }
        });

        btnAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeTable != null) {
                    if (txvAbsent.getText().equals(context.getString(R.string.SCAttendance_Absent))) {
                        final AlertDialog dialog = new AlertDialog.Builder(context).create();
                        final AttendanceTableAbsent attendanceTableAbsent = new AttendanceTableAbsent(context, student, date, timeTable, new AttendanceTableAbsent.AttendanceTableAbsentListener() {
                            @Override
                            public void onAttendanceRequestSuccess(Attendance attendance) {
                                dialog.dismiss();
                                mDataset2.add(attendance);
                                notifyDataSetChanged();
                            }
                        });
                        dialog.setView(attendanceTableAbsent.getView());
                        if (!attendanceReasons.isEmpty()) {
                            attendanceTableAbsent.setListAttendanceReason(attendanceReasons);
                            dialog.show();
                        } else {
                            LaoSchoolSingleton.getInstance().getDataAccessService().getAttendanceReason(new AsyncCallback<List<AttendanceReason>>() {
                                @Override
                                public void onSuccess(List<AttendanceReason> result) {
                                    attendanceReasons.addAll(result);
                                    attendanceTableAbsent.setListAttendanceReason(attendanceReasons);
                                    dialog.show();
                                }

                                @Override
                                public void onFailure(String message) {
                                    Toast.makeText(context, context.getString(R.string.SCCommon_UnknowError), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onAuthFail(String message) {
                                    LaoSchoolShared.goBackToLoginPage(context);
                                }
                            });
                        }
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("")
                                .setMessage(context.getString(R.string.SCAttendance_CancelAbsentConfirm))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (Attendance attendance : mDataset2) {
                                            if (attendance.getStudent_id() == student.getId() &&
                                                    (attendance.getSession_id() == null || attendance.getSession_id().equals(String.valueOf(timeTable.getSession_id())))) {
                                                cancelAttendance(attendance);
                                                break;
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.SCAttendance_NeedChoseSubject), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeTable != null) {
                    List<User> selectedStudents = new ArrayList<>();
                    selectedStudents.add(student);
                    String defaultText = "* " + context.getString(R.string.SCCommon_Date) + " " +
                            date + " * " + "\r\n \r\n" +
                            context.getString(R.string.SCAttendance_Subjects) + " " + timeTable.getSubject_Name() + ", \r\n \r\n" +
                            context.getString(R.string.SCAttendance_DefaultMessage2);
                    iScreenAttended.goToCreateMessagefromScreenAttendance(mDataset, selectedStudents, defaultText);
                } else
                    Toast.makeText(context, context.getString(R.string.SCAttendance_NeedChoseSubject), Toast.LENGTH_SHORT).show();
            }
        });

        if (student.getUserPhoto() == null) {
//            Log.i("ListStudent", "positon running: "+position);
            LaoSchoolSingleton.getInstance().getDataAccessService().getImageBitmap(student.getPhoto(), new AsyncCallback<Bitmap>() {
                @Override
                public void onSuccess(Bitmap result) {
                    try {
                        if (result != null) {
                            student.setUserPhoto(result);
                            imgStudent.setImageBitmap(student.getUserPhoto());
                            imgStudent.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.RIGHT_OF, imgStudent.getId());
                            params.addRule(RelativeLayout.END_OF, imgStudent.getId());
                            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                            params.setMargins(30, 0, 0, 0);
                            txtStudentName.setLayoutParams(params);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                @Override
                public void onFailure(String message) {
                    Bitmap icon;
                    if(student.getGender() == null)
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.ic_account_circle_black_36dp);
                    else if(student.getGender().equals("male"))
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.ic_male);
                    else
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.ic_female);
                    student.setUserPhoto(icon);
                    imgStudent.setImageBitmap(student.getUserPhoto());
                    imgStudent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.RIGHT_OF, imgStudent.getId());
//                    params.addRule(RelativeLayout.END_OF, imgStudent.getId());
                    params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    params.setMargins(30, 0, 0, 0);
                    txtStudentName.setLayoutParams(params);
                }

                @Override
                public void onAuthFail(String message) {
                    LaoSchoolShared.goBackToLoginPage(context);
                }
            });
        } else {
            imgStudent.setImageBitmap(student.getUserPhoto());
            imgStudent.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, imgStudent.getId());
//            params.addRule(RelativeLayout.END_OF, imgStudent.getId());
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            params.setMargins(30, 0, 0, 0);
            txtStudentName.setLayoutParams(params);
        }
    }

    void openAttendanceDetail(Attendance attendance) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View v = View.inflate(context, R.layout.view_attendance_detail, null);
        dialog.setView(v);
        dialog.show();

        TextView txbAttDt = (TextView) v.findViewById(R.id.txbAttDt);
        TextView txbSession = (TextView) v.findViewById(R.id.txbSession);
        TextView txbReason = (TextView) v.findViewById(R.id.txbReason);
        RelativeLayout formBtnClose = (RelativeLayout) v.findViewById(R.id.formBtnClose);
        ImageView imgBtnClose = (ImageView) v.findViewById(R.id.imgBtnClose);
        TextView btnClose = (TextView) v.findViewById(R.id.btnClose);
        TextView txvHeader = (TextView) v.findViewById(R.id.txvProfile);

        imgBtnClose.setColorFilter(Color.parseColor("#9E9E9E"));

        String[] shortAttDt = attendance.getAtt_dt().split(" ");
        String[] attdts = shortAttDt[0].split("-");
        txbAttDt.setText(attdts[0] + " - " + attdts[1] + " - " + attdts[2]);
        txvHeader.setText(context.getString(R.string.SCAttendance_Absent));
        btnClose.setText(context.getString(R.string.SCCommon_Close));

        if (attendance.getSession_id() == null)
            txbSession.setText(context.getString(R.string.SCAttendance_Fulldays));
        else
            txbSession.setText(attendance.getSession() + " - " + attendance.getSubject());

        if (attendance.getExcused() == 1) {
            txbReason.setText(attendance.getNotice());
            txbReason.setTextColor(context.getResources().getColor(R.color.colorAttendanceHasReason));
        } else {
            txbReason.setText(context.getString(R.string.SCAttendance_NoReason));
            txbReason.setTextColor(context.getResources().getColor(R.color.colorAttendanceNoReason));
        }

        formBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void cancelAttendance(final Attendance attendance) {
        LaoSchoolSingleton.getInstance().getDataAccessService().deleteAttendance(attendance.getId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mDataset2.remove(attendance);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(context);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap(List<User> listStudents, List<Attendance> attendanceList, TimeTable timeTable, String date) {
//        showdata = true;
        mDataset = listStudents;
        mDataset2 = attendanceList;
        this.timeTable = timeTable;
        this.date = date;
        notifyDataSetChanged();
    }

}
