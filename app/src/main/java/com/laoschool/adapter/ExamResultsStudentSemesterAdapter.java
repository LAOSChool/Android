package com.laoschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 3/11/2016.
 */
public class ExamResultsStudentSemesterAdapter extends RecyclerView.Adapter<ExamResultsStudentSemesterAdapter.ExamResultsStudentSemesterAdapterViewHolder> {
    private Fragment screen;
    private Context context;
    private List<ExamResult> examResults;
    private List<String> subjects = new ArrayList<>();
    private List<Integer> subjectIds = new ArrayList<>();

    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;
    Map<Integer, Map<Integer, ArrayList<String>>> listMap;

    public ExamResultsStudentSemesterAdapter(Fragment screen, List<ExamResult> examResults) {
        this.screen = screen;
        this.context = screen.getActivity();
        this.examResults = examResults;
        HashMap<Integer, String> hashterms = new LinkedHashMap<Integer, String>();
        listMap = new HashMap<>();
        for (ExamResult examResult : examResults) {
            hashterms.put(examResult.getSubject_id(), examResult.getSubject());

        }
        for (Integer subId : hashterms.keySet()) {
            //defile subject list
            subjects.add(hashterms.get(subId));
            subjectIds.add(subId);


            Map<Integer, ArrayList<String>> scoresByMonthList = new HashMap<>();
            for (int i = 0; i < examResults.size(); i++) {
                ExamResult examResult = examResults.get(i);
                int exam_month = _getMonthFormStringDate(examResult.getExam_dt());
                String score = String.valueOf(examResult.getIresult());
                if (examResult.getSubject_id() == subId) {

                    ArrayList tempList = null;
                    if (scoresByMonthList.containsKey(exam_month)) {

                        tempList = scoresByMonthList.get(exam_month);
                        if (tempList == null)
                            tempList = new ArrayList();
                        tempList.add(score);
                    } else {
                        tempList = new ArrayList();
                        tempList.add(score);
                    }
                    scoresByMonthList.put(exam_month, tempList);
                }

                listMap.put(subId, scoresByMonthList);
            }

        }
    }

    private int _getMonthFormStringDate(String exam_dt) {
        int month = 0;
        DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1;
        try {
            if (!exam_dt.trim().isEmpty()) {
                date1 = inputFormatter1.parse(exam_dt);
            } else {
                date1 = new Date();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            month = cal.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            Log.e("", "_getMonthFormStringDate() ParseException:" + e.getMessage());
        }
        return month;
    }

    @Override
    public ExamResultsStudentSemesterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_results_student, parent, false); //Inflating the layout
        } else if (viewType == TYPE_SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        ExamResultsStudentSemesterAdapterViewHolder viewHolder = new ExamResultsStudentSemesterAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExamResultsStudentSemesterAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            if (holder.viewType == TYPE_TITLE) {
                final String title = subjects.get(position);
                Map<Integer, ArrayList<String>> scores = listMap.get(subjectIds.get(position));
                Map<Integer, ArrayList<String>> treeScores = new TreeMap<>(scores);
//                //Define and set data
                TextView txtSubjectScreenResultsStudent = (TextView) view.findViewById(R.id.txtSubjectScreenResultsStudent);
                RecyclerView mListScoreBySemester = (RecyclerView) view.findViewById(R.id.mListScoreBySemester);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                mListScoreBySemester.setLayoutManager(linearLayoutManager);

                txtSubjectScreenResultsStudent.setText(title);

                ScoreStudentSemesterAdapter scoreStudentSemesterAdapter = new ScoreStudentSemesterAdapter(context, treeScores);
                mListScoreBySemester.setAdapter(scoreStudentSemesterAdapter);
                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();

                    }
                });
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    @Override
    public int getItemViewType(int position) {
//        String item = strings.get(position);
//        if (item.equals(context.getString(R.string.row_sub_header)))
//            return TYPE_SUB_HEADER;
//        else if (!item.equals(context.getString(R.string.row_line)))
        return TYPE_TITLE;
//        else
//            return TYPE_LINE;

    }

    public class ExamResultsStudentSemesterAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ExamResultsStudentSemesterAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}

