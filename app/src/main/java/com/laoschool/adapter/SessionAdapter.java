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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 5/18/2016.
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionAdapterViewHolder> {

    private static final String TAG = SessionAdapter.class.getSimpleName();
    private Context context;
    private int position;
    private List<TimeTable> list = new ArrayList<>();

    public SessionAdapter(Context context, int position, List<TimeTable> list) {
        this.context = context;
        this.position = position;
        this.list = list;
    }

    @Override
    public SessionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_session, parent, false);
        SessionAdapterViewHolder viewHolder = new SessionAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SessionAdapterViewHolder holder, int position) {
        View view = holder.view;
        final TimeTable timeTable = list.get(position);
        try {
            if (timeTable != null) {
                ((TextView) view.findViewById(R.id.lbSubjectName)).setText(timeTable.getSubject_Name());
                Log.d(TAG, "session name:" + timeTable.getSession_Name());
                String[] session = timeTable.getSession_Name().split("\\@");
                ((TextView) view.findViewById(R.id.lbSessionName)).setText(session[0]);
                ((TextView) view.findViewById(R.id.lbSessionDuration)).setText(session[1]);
                ((TextView) view.findViewById(R.id.lbTeacherName)).setText(timeTable.getTeacherName());
                ((TextView) view.findViewById(R.id.lbSessionDesciption)).setText(timeTable.getDescription());
                // {
                if (this.position > 0)
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _showTimeTableDetails(timeTable);
                        }
                    });
                // }
            } else {
                view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder() -exception:" + e.getMessage());
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
        if (list != null)
            return list.size();
        else
            return 0;
    }

    public class SessionAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public SessionAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}

