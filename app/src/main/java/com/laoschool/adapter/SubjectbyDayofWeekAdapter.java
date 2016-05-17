package com.laoschool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.TimeTable;
import com.laoschool.shared.LaoSchoolShared;

import java.util.List;

/**
 * Created by Hue on 5/16/2016.
 */
public class SubjectbyDayofWeekAdapter extends RecyclerView.Adapter<SubjectbyDayofWeekAdapter.SubjectbyDayofWeekAdapterViewHolder> {

    private static final String TAG = SubjectbyDayofWeekAdapter.class.getSimpleName();
    private Context context;
    private List<TimeTable> list;

    public SubjectbyDayofWeekAdapter(Context context, List<TimeTable> list) {
        this.context = context;
        this.list = list;
        Log.d(TAG, " -size:" + list.size());
    }

    @Override
    public SubjectbyDayofWeekAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject_by_day_of_week, parent, false);
        SubjectbyDayofWeekAdapterViewHolder viewHolder = new SubjectbyDayofWeekAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SubjectbyDayofWeekAdapterViewHolder holder, int position) {
        View view = holder.view;
        final TimeTable timeTable = list.get(position);
        try {
            if (!timeTable.getSubject_Name().equals("Empty")) {
                ((TextView) view.findViewById(R.id.lbSubject)).setText(timeTable.getSubject_Name());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _showTimeTableDetails(timeTable);
                    }
                });
            } else {
                ((TextView) view.findViewById(R.id.lbSubject)).setText("");
            }
        } catch (Exception e) {
        }
    }

    private void _showTimeTableDetails(TimeTable timeTable) {
        AlertDialog.Builder bDetails = new AlertDialog.Builder(context);
        View examResultDetails = View.inflate(context, R.layout.view_time_table_details, null);
//        ((TextView) examResultDetails.findViewById(R.id.lbTimeTableDayOfWeek)).setText(String.valueOf(timeTable.getWeekday_Name()));
//
//        ((TextView) examResultDetails.findViewById(R.id.lbTimeTableDescription)).setText(timeTable.getDescription());
//        ((TextView) examResultDetails.findViewById(R.id.lbTimeTableSession)).setText(String.valueOf(timeTable.getSession_id()));
//        ((TextView) examResultDetails.findViewById(R.id.lbTimeTableSubject)).setText(timeTable.getSubject_id());
//        ((TextView) examResultDetails.findViewById(R.id.lbExamTecherName)).setText(timeTable.getTeacher_id());
        bDetails.setCustomTitle(examResultDetails);
        final Dialog dialog = bDetails.create();
        ((TextView) examResultDetails.findViewById(R.id.lbExamClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubjectbyDayofWeekAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public SubjectbyDayofWeekAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
