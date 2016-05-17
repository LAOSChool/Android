package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.TimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 5/16/2016.
 */
public class TimeTableStudentAdapter extends RecyclerView.Adapter<TimeTableStudentAdapter.TimeTableStudentAdapterViewHolder> {
    private static final String TAG = TimeTableStudentAdapter.class.getSimpleName();
    private Map<Integer, List<TimeTable>> timeTableList;
    private Context context;
    List<Integer> dayOfWeek = new ArrayList<>();

    public TimeTableStudentAdapter(Context context, Map<Integer, List<TimeTable>> timeTableList) {
        this.context = context;
        Map<Integer, List<TimeTable>> timeTableTreeMap = new TreeMap<>(timeTableList);
        this.timeTableList = timeTableTreeMap;
        for (Integer day : timeTableTreeMap.keySet()) {
            dayOfWeek.add(day);
        }
    }

    @Override
    public TimeTableStudentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_time_table, parent, false);
        TimeTableStudentAdapterViewHolder viewHolder = new TimeTableStudentAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimeTableStudentAdapterViewHolder holder, int position) {
        int day = dayOfWeek.get(position);
        List<TimeTable> results = timeTableList.get(day);

        String dayofw = getDayOfWeek(dayOfWeek.get(position));
        View view = holder.view;
        ((TextView) view.findViewById(R.id.lbDayofWeek)).setText(dayofw);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mSubjectbyDayofWeek);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Log.d(TAG, "onBindViewHolder() -size:" + results.size());
        recyclerView.setAdapter(new SubjectbyDayofWeekAdapter(context, results));
    }

    private String getDayOfWeek(Integer day) {
        switch (day) {
            case 0:
                return "";
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thu";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            case 7:
                return "Sun";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return dayOfWeek.size();
    }

    public class TimeTableStudentAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public TimeTableStudentAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
