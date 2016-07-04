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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 5/18/2016.
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionAdapterViewHolder> {

    private static final String TAG = SessionAdapter.class.getSimpleName();
    private static final int TYPE_SUB_HEADER_LESSION = 0;
    private static final int TYPE_SESSION = 1;
    private Context context;
    private int position;
    private List<TimeTable> list = new ArrayList<>();
    private List<Object> objects = new ArrayList<>();

    public SessionAdapter(Context context, int position, List<TimeTable> list) {
        this.context = context;
        this.position = position;
        this.list = list;
        try {
            HashMap<Integer, List<TimeTable>> lessionMap = new LinkedHashMap<>();
            HashMap<Integer, String> lessionNameMap = new LinkedHashMap<>();

            for (TimeTable timeTable : list) {
                String[] session = timeTable.getSession_Name().split("\\@");
                int lessonId = Integer.parseInt(session[2]);
                List<TimeTable> paList;
                if (lessionMap.containsKey(lessonId)) {
                    paList = lessionMap.get(lessonId);
                    paList.add(timeTable);
                } else {
                    paList = new ArrayList<>();
                    paList.add(timeTable);
                }
                String lessonName = "";
                if (lessonId == 1)
                    lessonName = context.getString(R.string.SCSchedule_Morning);
                if (lessonId == 2)
                    lessonName = context.getString(R.string.SCSchedule_Afternoon);
                if (lessonId == 3)
                    lessonName = context.getString(R.string.SCSchedule_Evening);

                lessionNameMap.put(lessonId, lessonName);
                lessionMap.put(lessonId, paList);
            }
            //sort
            Map<Integer, List<TimeTable>> lessionTreeMap = new TreeMap<>(lessionMap);

            //objects = new ArrayList<>();
            for (Integer lession : lessionMap.keySet()) {
                Log.d(TAG, "-lession id:" + lession + ",lession name:" + lessionNameMap.get(lession));
                objects.add(lessionNameMap.get(lession));
                objects.addAll(lessionTreeMap.get(lession));
            }
        } catch (Exception e) {
            objects.addAll(list);
        }

    }

    @Override
    public SessionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_SUB_HEADER_LESSION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_session_sub_header, parent, false);
        } else if (viewType == TYPE_SESSION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_session, parent, false);
        }
        SessionAdapterViewHolder viewHolder = new SessionAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SessionAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            if (holder.viewType == TYPE_SESSION) {
                final TimeTable timeTable = (TimeTable) objects.get(position);
                if (timeTable != null) {
                    ((TextView) view.findViewById(R.id.lbSubjectName)).setText(timeTable.getSubject_Name());
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
            } else if (holder.viewType == TYPE_SUB_HEADER_LESSION) {
                String lessonName = (String) objects.get(position);
                ((TextView) view.findViewById(R.id.lbLessonName)).setText(lessonName);

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
        if (objects != null)
            return objects.size();
        else
            return 0;
    }


    @Override
    public int getItemViewType(int position) {
        Object obj = objects.get(position);
        if (obj instanceof String) {
            return TYPE_SUB_HEADER_LESSION;
        } else if (obj instanceof TimeTable) {
            return TYPE_SESSION;
        } else {
            return -1;
        }
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

